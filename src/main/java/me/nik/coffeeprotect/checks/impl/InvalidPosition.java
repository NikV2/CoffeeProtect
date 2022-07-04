package me.nik.coffeeprotect.checks.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import me.nik.coffeeprotect.api.custom.CheckResult;
import me.nik.coffeeprotect.checks.Check;
import me.nik.coffeeprotect.checks.annotations.CheckInfo;
import me.nik.coffeeprotect.files.Config;
import me.nik.coffeeprotect.managers.profile.Profile;
import me.nik.coffeeprotect.wrappers.WrapperPlayClientFlyingGeneral;

@CheckInfo(name = "Invalid Position", description = "Checks for invalid position packets")
public class InvalidPosition extends Check {
    public InvalidPosition(Profile profile) {
        super(profile, Config.Setting.CHECKS_INVALID_POSITION_ENABLED.getBoolean());
    }

    @Override
    public CheckResult handle(PacketEvent e) {

        PacketType packetType = e.getPacketType();

        if (packetType == PacketType.Play.Client.POSITION
                || packetType == PacketType.Play.Client.POSITION_LOOK
                || packetType == PacketType.Play.Client.LOOK
                || packetType == PacketType.Play.Client.VEHICLE_MOVE) {

            WrapperPlayClientFlyingGeneral wrapper = new WrapperPlayClientFlyingGeneral(e.getPacket());

            double x = Math.abs(wrapper.getX());
            double y = Math.abs(wrapper.getY());
            double z = Math.abs(wrapper.getZ());
            float yaw = Math.abs(wrapper.getYaw());
            float pitch = Math.abs(wrapper.getPitch());

            //This potentially causes damage to the server.
            final boolean invalid = x > Config.Setting.CHECKS_INVALID_POSITION_MAX_XYZ.getDouble()
                    || y > Config.Setting.CHECKS_INVALID_POSITION_MAX_XYZ.getDouble()
                    || z > Config.Setting.CHECKS_INVALID_POSITION_MAX_XYZ.getDouble()
                    || yaw > Config.Setting.CHECKS_INVALID_POSITION_MAX_YAW.getFloat()
                    || pitch > Config.Setting.CHECKS_INVALID_POSITION_MAX_PITCH.getFloat();

            //It's impossible for these values to be NaN or Infinite.
            final boolean impossible = !Double.isFinite(x)
                    || !Double.isFinite(y)
                    || !Double.isFinite(z)
                    || !Float.isFinite(yaw)
                    || !Float.isFinite(pitch);

            if (invalid || impossible) {

                return new CheckResult(this,
                        "Invalid Position, X: " + x + " Y: " + y + " Z: " + z + " Yaw: " + yaw + " Pitch: " + pitch
                );
            }
        }

        return null;
    }
}