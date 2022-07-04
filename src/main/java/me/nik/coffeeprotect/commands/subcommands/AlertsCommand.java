package me.nik.coffeeprotect.commands.subcommands;

import me.nik.coffeeprotect.CoffeeProtect;
import me.nik.coffeeprotect.commands.SubCommand;
import me.nik.coffeeprotect.enums.MsgType;
import me.nik.coffeeprotect.enums.Permissions;
import me.nik.coffeeprotect.managers.profile.Profile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AlertsCommand extends SubCommand {

    private final CoffeeProtect plugin;

    public AlertsCommand(CoffeeProtect plugin) {
        this.plugin = plugin;
    }

    @Override
    protected String getName() {
        return "alerts";
    }

    @Override
    protected String getDescription() {
        return "Toggle the alerts";
    }

    @Override
    protected String getSyntax() {
        return "/coffeeprotect alerts";
    }

    @Override
    protected String getPermission() {
        return Permissions.ALERTS.getPermission();
    }

    @Override
    protected int maxArguments() {
        return 1;
    }

    @Override
    protected boolean canConsoleExecute() {
        return false;
    }

    @Override
    protected void perform(CommandSender sender, String[] args) {

        UUID uuid = ((Player) sender).getUniqueId();

        if (this.plugin.getAlertManager().getPlayersWithAlerts().contains(uuid)) {

            this.plugin.getAlertManager().removePlayerFromAlerts(uuid);

            sender.sendMessage(MsgType.ALERTS_DISABLED.getMessage());

        } else {

            this.plugin.getAlertManager().addPlayerToAlerts(uuid);

            sender.sendMessage(MsgType.ALERTS_ENABLED.getMessage());
        }
    }
}