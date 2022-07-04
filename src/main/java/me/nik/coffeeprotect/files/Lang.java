package me.nik.coffeeprotect.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Lang {
    private File file;
    private FileConfiguration lang;

    public void setup(JavaPlugin plugin) {

        this.file = new File(plugin.getDataFolder(), "lang.yml");

        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException ignored) {
            }
        }

        this.lang = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfiguration get() {
        return lang;
    }

    public void save() {
        try {
            this.lang.save(file);
        } catch (IOException ignored) {
        }
    }

    public void reload() {
        this.lang = YamlConfiguration.loadConfiguration(file);
    }

    public void addDefaults() {
        get().addDefault("prefix", "&f&l[&6CoffeeProtect&f&l]&f»&r ");
        get().addDefault("no_perm", "&cYou do not have permission to do that!");
        get().addDefault("reloaded", "&fYou have successfully reloaded the plugin!");
        get().addDefault("console_commands", "&c&lYou cannot run this command through the console :(");
        get().addDefault("alerts_enabled", "&fYou have enabled alerts.");
        get().addDefault("alerts_disabled", "&fYou have disabled alerts.");
        get().addDefault("alert_message", "&7%player% &ffailed &6%check% &f(&6x%vl%&f)");
        get().addDefault("alert_hover",
                Arrays.asList(
                        "&7Description:&r",
                        "%description%",
                        "",
                        "&7Information:&r",
                        "%information%",
                        "",
                        "&fClick to teleport"
                ));
    }
}