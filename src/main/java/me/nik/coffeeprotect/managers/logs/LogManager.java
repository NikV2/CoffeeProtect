package me.nik.coffeeprotect.managers.logs;

import me.nik.coffeeprotect.CoffeeProtect;
import me.nik.coffeeprotect.files.Config;
import me.nik.coffeeprotect.managers.AbstractManager;
import me.nik.coffeeprotect.managers.logs.impl.FileExporter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LogManager implements AbstractManager {

    private final Queue<PlayerLog> logsQueue = new ConcurrentLinkedQueue<>();

    private LogExporter logExporter;

    private final CoffeeProtect plugin;

    private boolean logging;

    public LogManager(CoffeeProtect plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize() {

        switch (Config.Setting.LOGS_TYPE.getString().toLowerCase()) {

            /*case "mysql":

                this.logExporter = new MySQLExporter(plugin);

                break;

            case "sqlite":

                this.logExporter = new SQLiteExporter(plugin);

                break;*/

            default:

                this.logExporter = new FileExporter(this.plugin);

                break;
        }

        this.logExporter.initialize();
    }

    public Queue<PlayerLog> getLogsQueue() {
        return this.logsQueue;
    }

    public void addLogToQueue(PlayerLog playerLog) {

        if (!Config.Setting.LOGS_ENABLED.getBoolean()) return;

        this.logsQueue.add(playerLog);
    }

    public void clearQueuedLogs() {
        this.logsQueue.clear();
    }

    public LogExporter getLogExporter() {
        return this.logExporter;
    }

    public boolean isLogging() {
        return this.logging;
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }

    @Override
    public void shutdown() {
        this.logsQueue.clear();
        this.logExporter.shutdown();
    }
}