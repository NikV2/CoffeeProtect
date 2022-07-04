package me.nik.coffeeprotect.tasks;

import me.nik.coffeeprotect.CoffeeProtect;
import org.bukkit.scheduler.BukkitRunnable;

public class LogsTask extends BukkitRunnable {

    private final CoffeeProtect plugin;

    public LogsTask(CoffeeProtect plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (this.plugin.getLogManager().isLogging() || this.plugin.getLogManager().getLogsQueue().isEmpty()) return;

        this.plugin.getLogManager().getLogExporter().logMultiple(this.plugin.getLogManager().getLogsQueue());

        this.plugin.getLogManager().clearQueuedLogs();

        this.plugin.getLogManager().setLogging(false);
    }
}