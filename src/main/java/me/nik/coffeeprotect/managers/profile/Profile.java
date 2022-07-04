package me.nik.coffeeprotect.managers.profile;

import me.nik.coffeeprotect.CoffeeProtect;
import me.nik.coffeeprotect.enums.Permissions;
import me.nik.coffeeprotect.files.Config;
import me.nik.coffeeprotect.utils.custom.CheckHolder;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Profile {

    private final UUID uuid;

    private final CheckHolder checkHolder = new CheckHolder(this);

    private int violations = 0;

    public Profile(Player player) {

        //UUID
        this.uuid = player.getUniqueId();

        //Check if we should enable alerts for this player
        if (Config.Setting.TOGGLE_ALERTS_ON_JOIN.getBoolean() && player.hasPermission(Permissions.ALERTS.getPermission())) {
            CoffeeProtect.getInstance().getAlertManager().addPlayerToAlerts(this.uuid);
        }
    }

    public void resetViolations() {
        this.violations = 0;
    }

    public int incrementViolations() {
        return this.violations++;
    }

    public CheckHolder getCheckHolder() {
        return checkHolder;
    }

    public UUID getUUID() {
        return uuid;
    }
}