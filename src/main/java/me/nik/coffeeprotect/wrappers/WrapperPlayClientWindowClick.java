package me.nik.coffeeprotect.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import me.nik.coffeeprotect.utils.ServerUtils;

public class WrapperPlayClientWindowClick extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Client.WINDOW_CLICK;

    public WrapperPlayClientWindowClick(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * @return The current Slot
     */
    public int getSlot() {
        return handle.getIntegers().read(ServerUtils.isCavesUpdate() ? 2 : 1);
    }
}