package me.nik.coffeeprotect.checks.impl;

import com.comphenix.protocol.events.PacketEvent;
import me.nik.coffeeprotect.api.custom.CheckResult;
import me.nik.coffeeprotect.checks.Check;
import me.nik.coffeeprotect.checks.annotations.CheckInfo;
import me.nik.coffeeprotect.files.Config;
import me.nik.coffeeprotect.managers.profile.Profile;

@CheckInfo(name = "Offline Player", description = "Checks if an offline player is sending packets")
public class OfflinePlayer extends Check {
    public OfflinePlayer(Profile profile) {
        super(profile, Config.Setting.CHECKS_OFFLINE_PLAYER_ENABLED.getBoolean());
    }

    @Override
    public CheckResult handle(PacketEvent e) {
        return e.getPlayer().isOnline() ? null : new CheckResult(this, "Offline State");
    }
}