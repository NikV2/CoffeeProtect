package me.nik.coffeeprotect.tasks;

import me.nik.coffeeprotect.CoffeeProtect;
import me.nik.coffeeprotect.managers.profile.Profile;
import org.bukkit.scheduler.BukkitRunnable;

public class ViolationTask extends BukkitRunnable {

    private final CoffeeProtect plugin;

    public ViolationTask(CoffeeProtect plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        this.plugin.getProfileManager().getProfileMap().values().forEach(Profile::resetViolations);
    }
}