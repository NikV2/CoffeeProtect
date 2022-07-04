package me.nik.coffeeprotect.tasks;

import me.nik.coffeeprotect.CoffeeProtect;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class CacheTask extends BukkitRunnable {

    private final CoffeeProtect plugin;

    public CacheTask(CoffeeProtect plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        this.plugin.getProfileManager().getProfileMap().keySet().removeIf(uuid -> Bukkit.getPlayer(uuid) == null);
    }
}