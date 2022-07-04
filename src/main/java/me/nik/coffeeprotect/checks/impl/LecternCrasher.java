package me.nik.coffeeprotect.checks.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import me.nik.coffeeprotect.api.custom.CheckResult;
import me.nik.coffeeprotect.checks.Check;
import me.nik.coffeeprotect.checks.annotations.CheckInfo;
import me.nik.coffeeprotect.files.Config;
import me.nik.coffeeprotect.managers.profile.Profile;
import me.nik.coffeeprotect.wrappers.WrapperPlayClientWindowClick;

@CheckInfo(name = "Lectern Crasher", description = "Checks for a lectern exploit")
public class LecternCrasher extends Check {
    public LecternCrasher(Profile profile) {
        super(profile, Config.Setting.CHECKS_LECTERN_CRASHER_ENABLED.getBoolean());
    }

    @Override
    public CheckResult handle(PacketEvent e) {
        return e.getPacketType() == PacketType.Play.Client.WINDOW_CLICK
                && e.getPlayer().getOpenInventory().getTopInventory().getType().name().equals("LECTERN")
                && new WrapperPlayClientWindowClick(e.getPacket()).getSlot() == 1
                ? new CheckResult(this, "Lectern Inventory")
                : null;
    }
}