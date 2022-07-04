package me.nik.coffeeprotect.listeners;

import me.nik.coffeeprotect.CoffeeProtect;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class LoginListener implements Listener {

    private final CoffeeProtect plugin;

    public LoginListener(CoffeeProtect plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onJoin(PlayerLoginEvent e) {
    }
}