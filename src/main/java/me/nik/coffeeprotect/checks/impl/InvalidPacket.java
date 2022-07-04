package me.nik.coffeeprotect.checks.impl;

import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.netty.WirePacket;
import me.nik.coffeeprotect.api.custom.CheckResult;
import me.nik.coffeeprotect.checks.Check;
import me.nik.coffeeprotect.checks.annotations.CheckInfo;
import me.nik.coffeeprotect.files.Config;
import me.nik.coffeeprotect.managers.profile.Profile;

@CheckInfo(name = "Invalid Packet", description = "Checks for invalid packet size")
public class InvalidPacket extends Check {
    public InvalidPacket(Profile profile) {
        super(profile, Config.Setting.CHECKS_INVALID_PACKET_ENABLED.getBoolean());
    }

    @Override
    public CheckResult handle(PacketEvent e) {

        int size = WirePacket.bytesFromPacket(e.getPacket()).length;

        //https://www.baeldung.com/cs/tcp-max-packet-size
        return size > Config.Setting.CHECKS_INVALID_PACKET_MAX_PACKET_SIZE.getInt()
                ? new CheckResult(this, "Packet Size: " + size)
                : null;
    }
}