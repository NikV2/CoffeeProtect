package me.nik.coffeeprotect;

import com.comphenix.protocol.ProtocolLibrary;
import me.nik.coffeeprotect.commands.CommandManager;
import me.nik.coffeeprotect.files.Config;
import me.nik.coffeeprotect.files.Lang;
import me.nik.coffeeprotect.listeners.ExploitListener;
import me.nik.coffeeprotect.listeners.InvalidPlacementsListener;
import me.nik.coffeeprotect.listeners.MapTrackingListener;
import me.nik.coffeeprotect.listeners.NetworkListener;
import me.nik.coffeeprotect.listeners.NullAddressListener;
import me.nik.coffeeprotect.listeners.UUIDSpoofListener;
import me.nik.coffeeprotect.listeners.VehicleDisconnectListener;
import me.nik.coffeeprotect.managers.AlertManager;
import me.nik.coffeeprotect.managers.logs.LogManager;
import me.nik.coffeeprotect.managers.profile.ProfileManager;
import me.nik.coffeeprotect.tasks.CacheTask;
import me.nik.coffeeprotect.tasks.LogsTask;
import me.nik.coffeeprotect.tasks.ViolationTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoffeeProtect extends JavaPlugin {

    private final String[] STARTUP_MESSAGE = new String[]{
            " ",
            ChatColor.GOLD + "Coffee Protect v" + this.getDescription().getVersion(),
            " ",
            ChatColor.WHITE + "  Author: Nik",
            " "
    };

    private static CoffeeProtect instance;

    private final Config config = new Config(this);
    private final Lang lang = new Lang();

    private final ProfileManager profileManager = new ProfileManager();
    private final LogManager logManager = new LogManager(this);
    private final AlertManager alertManager = new AlertManager();

    @Override
    public void onEnable() {

        instance = this;

        this.getServer().getConsoleSender().sendMessage(STARTUP_MESSAGE);

        //Load Files
        this.config.setup();
        this.lang.setup(this);
        this.lang.addDefaults();
        this.lang.get().options().copyDefaults(true);
        this.lang.save();

        //Initialize Managers
        this.profileManager.initialize();
        this.logManager.initialize();
        this.alertManager.initialize();

        //Load Commands
        getCommand("coffeeprotect").setExecutor(new CommandManager(this));

        //Register Listeners
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new ExploitListener(this), this);

        //General Checks
        if (Config.Setting.DISABLE_MAP_TRACKING.getBoolean()) pm.registerEvents(new MapTrackingListener(), this);
        if (Config.Setting.DISABLE_INVALID_PLACEMENTS.getBoolean()) pm.registerEvents(new InvalidPlacementsListener(), this);
        if (Config.Setting.DISABLE_VEHICLE_DISCONNECT.getBoolean()) pm.registerEvents(new VehicleDisconnectListener(), this);
        if (Config.Setting.DISABLE_NULL_ADDRESS.getBoolean()) pm.registerEvents(new NullAddressListener(), this);
        if (Config.Setting.DISABLE_UUID_SPOOF.getBoolean()) pm.registerEvents(new UUIDSpoofListener(), this);

        ProtocolLibrary.getProtocolManager().addPacketListener(new NetworkListener(this));

        //Register Tasks
        new CacheTask(this).runTaskTimerAsynchronously(
                this,
                Config.Setting.CACHE_INTERVAL.getLong() * 1200L,
                Config.Setting.CACHE_INTERVAL.getLong() * 1200L
        );

        new ViolationTask(this).runTaskTimerAsynchronously(
                this,
                Config.Setting.PUNISH_VIOLATION_RESET_INTERVAL.getLong() * 1200L,
                Config.Setting.PUNISH_VIOLATION_RESET_INTERVAL.getLong() * 1200L
        );

        if (Config.Setting.LOGS_ENABLED.getBoolean()) {
            new LogsTask(this).runTaskTimerAsynchronously(this, 6000L, 6000L);
        }
    }

    @Override
    public void onDisable() {

        this.profileManager.shutdown();
        this.logManager.shutdown();
        this.alertManager.shutdown();

        this.config.reset();
        this.lang.reload();
        this.lang.save();

        HandlerList.unregisterAll(this);
        ProtocolLibrary.getProtocolManager().removePacketListeners(this);
        Bukkit.getScheduler().cancelTasks(this);

        instance = null;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public LogManager getLogManager() {
        return logManager;
    }

    public AlertManager getAlertManager() {
        return alertManager;
    }

    public Config getConfiguration() {
        return config;
    }

    public Lang getLang() {
        return lang;
    }

    public static CoffeeProtect getInstance() {
        return instance;
    }
}