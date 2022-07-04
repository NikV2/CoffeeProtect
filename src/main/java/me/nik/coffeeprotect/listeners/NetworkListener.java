package me.nik.coffeeprotect.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.nik.coffeeprotect.CoffeeProtect;
import me.nik.coffeeprotect.api.custom.CheckResult;
import me.nik.coffeeprotect.api.events.PlayerExploitEvent;
import me.nik.coffeeprotect.files.Config;
import me.nik.coffeeprotect.managers.profile.Profile;
import me.nik.coffeeprotect.utils.TaskUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class NetworkListener extends PacketAdapter {

    private final CoffeeProtect plugin;

    public NetworkListener(CoffeeProtect plugin) {
        super(plugin,
                ListenerPriority.LOWEST,
                PacketType.Play.Client.getInstance()
                        .values()
                        .stream()
                        .filter(packetType ->
                                /*
                                Make sure the packet is supported in this version
                                And ignore the keep alive packet so it won't get delayed.
                                 */
                                packetType.isSupported() && packetType != PacketType.Play.Client.KEEP_ALIVE)
                        .collect(Collectors.toList()),
                ListenerOptions.ASYNC,
                ListenerOptions.INTERCEPT_INPUT_BUFFER);

        this.plugin = plugin;
    }

    @Override
    public void onPacketReceiving(PacketEvent e) {
        if (e.isPlayerTemporary() || e.getPlayer() == null) return;

        Player player = e.getPlayer();

        Profile profile = this.plugin.getProfileManager().getProfile(player);

        CheckResult checkResult = profile.getCheckHolder().runChecks(e);

        //All checks passed successfully.
        if (checkResult == null) return;

        //Cancel the packet here.
        e.setCancelled(true);

        //Increment the violations here.
        int violations = profile.incrementViolations();

        Bukkit.getPluginManager().callEvent(
                new PlayerExploitEvent(
                        player,
                        checkResult.getCheckName(),
                        checkResult.getDescription(),
                        checkResult.getInformation(),
                        violations
                )
        );

        if (violations > Config.Setting.PUNISH_MAX_VIOLATIONS.getInt()) {

            //Execute the command on the main thread.
            TaskUtils.task(() -> Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    Config.Setting.PUNISH_COMMAND.getString().replace("%player%", player.getName())
            ));

            //Reset the violations to prevent issues while the command above hasn't been executed yet.
            profile.resetViolations();
        }
    }

    @Override
    public void onPacketSending(PacketEvent e) {
    }
}