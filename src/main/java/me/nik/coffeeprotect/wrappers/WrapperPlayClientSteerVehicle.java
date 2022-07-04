package me.nik.coffeeprotect.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayClientSteerVehicle extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Client.STEER_VEHICLE;

    public WrapperPlayClientSteerVehicle(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve Sideways.
     * <p>
     * Notes: positive to the left of the player
     *
     * @return The current Sideways
     */
    public float getSideways() {
        return handle.getFloat().read(0);
    }

    /**
     * Retrieve Forward.
     * <p>
     * Notes: positive forward
     *
     * @return The current Forward
     */
    public float getForward() {
        return handle.getFloat().read(1);
    }

    public boolean isJump() {
        return handle.getBooleans().read(0);
    }

    public boolean isUnmount() {
        return handle.getBooleans().read(1);
    }
}