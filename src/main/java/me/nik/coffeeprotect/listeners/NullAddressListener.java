package me.nik.coffeeprotect.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class NullAddressListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLogin(PlayerLoginEvent e) {
        if (e.getAddress() == null) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Invalid Address");
        }
    }
}