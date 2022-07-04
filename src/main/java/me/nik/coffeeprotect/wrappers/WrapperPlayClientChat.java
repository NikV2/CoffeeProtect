package me.nik.coffeeprotect.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayClientChat extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Client.CHAT;

    public WrapperPlayClientChat(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve Message.
     *
     * @return The current Message
     */
    public String getMessage() {
        return handle.getStrings().read(0);
    }
}