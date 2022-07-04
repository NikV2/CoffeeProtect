package me.nik.coffeeprotect.files;

import me.nik.coffeeprotect.CoffeeProtect;
import me.nik.coffeeprotect.files.commentedfiles.CommentedFileConfiguration;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Config {

    private static final String[] HEADER = new String[]{
            "+----------------------------------------------------------------------------------------------+",
            "|                                                                                              |",
            "|                                         CoffeeProtect                                        |",
            "|                                                                                              |",
            "|                               Discord: https://discord.gg/m7j2Y9H                            |",
            "|                                                                                              |",
            "|                                           Author: Nik                                        |",
            "|                                                                                              |",
            "+----------------------------------------------------------------------------------------------+"
    };

    private final JavaPlugin plugin;
    private CommentedFileConfiguration configuration;
    private static boolean exists;

    public Config(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setup() {

        File configFile = new File(this.plugin.getDataFolder(), "config.yml");

        exists = configFile.exists();

        boolean setHeaderFooter = !exists;

        boolean changed = setHeaderFooter;

        this.configuration = CommentedFileConfiguration.loadConfiguration(this.plugin, configFile);

        if (setHeaderFooter) this.configuration.addComments(HEADER);

        for (Setting setting : Setting.values()) {

            setting.reset();

            changed |= setting.setIfNotExists(this.configuration);
        }

        if (changed) this.configuration.save();

        for (Setting setting : Setting.values()) setting.loadValue();
    }

    public void reset() {
        for (Setting setting : Setting.values()) setting.reset();
    }

    /**
     * @return the config.yml as a CommentedFileConfiguration
     */
    public CommentedFileConfiguration getConfig() {
        return this.configuration;
    }

    public enum Setting {
        SERVER_NAME("server_name", "Server", "The server name that will be shown in Player Logs"),

        TOGGLE_ALERTS_ON_JOIN("toggle_alerts_on_join", true, "Should we enable alerts for admins when they join?"),

        CACHE_INTERVAL("cache_interval", 10, "How often should we clear invalid player cache? (In minutes)"),

        ALERT_CONSOLE("alert_console", true, "Should we also send alerts in console?"),

        LOGS("logs", "", "Log Settings"),
        LOGS_ENABLED("logs.enabled", true, "Should we enable logging?"),
        LOGS_TYPE("logs.type", "YAML", "What type of Database should we use for logging?"),
        LOGS_CLEAR_DAYS("logs.clear_days", 5, "Logs older than this value of Days will be cleared"),

        SETTINGS("settings", "", "General Settings"),
        DISABLE_MAP_TRACKING("settings.disable_map_tracking", true, "Should map tracking be cancelled?", "This fixes certain crash exploits"),
        DISABLE_INVALID_PLACEMENTS("settings.disable_invalid_placements", true, "Should invalid block placements be cancelled?", "This fixes a block placing exploit found in the newer versions of Minecraft"),
        DISABLE_VEHICLE_DISCONNECT("settings.disable_vehicle_disconnect", true, "Should disconnecting force the player to eject?", "This fixes a rare issue that occurs with certain Protocol plugins and can lead to exploits"),
        DISABLE_NULL_ADDRESS("settings.disable_null_address", true, "Should login attempts with null addresses be cancelled?", "This fixes certain login exploits"),
        DISABLE_UUID_SPOOF("settings.disable_uuid_spoof", true, "Should UUID spoofing attempts be cancelled?", "This fixes certain login exploits"),

        PUNISH("punish", "", "Punish Settings"),
        PUNISH_MAX_VIOLATIONS("punish.max_violations", 5, "The maximum violation amount in order to punish the player"),
        PUNISH_VIOLATION_RESET_INTERVAL("punish.violation_reset_interval", 5, "How often should we clear the player violations? (In minutes)"),
        PUNISH_COMMAND("punish.punish_command", "kick %player% Invalid Packet", "The command that will be executed once a player reaches the maximum violation amount", "(%nl% for new line)"),

        CHECKS("checks", "", "Checks Settings"),

        CHECKS_INVALID_ITEM("checks.invalid_item", "", "Invalid Item Check", "This checks for invalid items"),
        CHECKS_INVALID_ITEM_ENABLED("checks.invalid_item.enabled", true, "Should we enable this check?"),
        CHECKS_INVALID_ITEM_MAX_DISPLAYNAME_LENGTH("checks.invalid_item.max_displayname_length", 256),
        CHECKS_INVALID_ITEM_MAX_LORE_SIZE("checks.invalid_item.max_lore_size", 64),
        CHECKS_INVALID_ITEM_MAX_BOOK_TITLE_LENGTH("checks.invalid_item.max_book_title_length", 32),
        CHECKS_INVALID_ITEM_MAX_BOOK_AUTHOR_LENGTH("checks.invalid_item.max_book_author_length", 16),
        CHECKS_INVALID_ITEM_MAX_BOOK_PAGE_COUNT("checks.invalid_item.max_book_page_count", 50),
        CHECKS_INVALID_ITEM_MAX_BOOK_PAGE_BYTES("checks.invalid_item.max_book_page_bytes", 300),

        CHECKS_INVALID_PACKET("checks.invalid_packet", "", "Invalid Packet Check", "This checks for invalid packet size"),
        CHECKS_INVALID_PACKET_ENABLED("checks.invalid_packet.enabled", true, "Should we enable this check?"),
        CHECKS_INVALID_PACKET_MAX_PACKET_SIZE("checks.invalid_packet.max_packet_size", 65535),

        CHECKS_OFFLINE_PLAYER("checks.offline_player", "", "Offline Player Check", "This checks if an offline player is sending packets"),
        CHECKS_OFFLINE_PLAYER_ENABLED("checks.offline_player.enabled", true, "Should we enable this check?"),

        CHECKS_INVALID_POSITION("checks.invalid_position", "", "Invalid Position Check", "This checks for invalid position packets"),
        CHECKS_INVALID_POSITION_ENABLED("checks.invalid_position.enabled", true, "Should we enable this check?"),
        CHECKS_INVALID_POSITION_MAX_XYZ("checks.invalid_position.max_xyz", 3.0E7D),
        CHECKS_INVALID_POSITION_MAX_YAW("checks.invalid_position.max_yaw", 3.4028235e+35F),
        CHECKS_INVALID_POSITION_MAX_PITCH("checks.invalid_position.max_pitch", 90.0F),

        CHECKS_LECTERN_CRASHER("checks.lectern_crasher", "", "Lectern Crasher Check", "This checks for a lectern exploit"),
        CHECKS_LECTERN_CRASHER_ENABLED("checks.lectern_crasher.enabled", true, "Should we enable this check?"),

        CHECKS_SELF_DAMAGE("checks.self_damage", "", "Self Damage Check", "This checks if the player damages himself"),
        CHECKS_SELF_DAMAGE_ENABLED("checks.self_damage.enabled", true, "Should we enable this check?");

        private final String key;
        private final Object defaultValue;
        private boolean excluded;
        private final String[] comments;
        private Object value = null;

        Setting(String key, Object defaultValue, String... comments) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments != null ? comments : new String[0];
        }

        Setting(String key, Object defaultValue, boolean excluded, String... comments) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments != null ? comments : new String[0];
            this.excluded = excluded;
        }

        /**
         * Gets the setting as a boolean
         *
         * @return The setting as a boolean
         */
        public boolean getBoolean() {
            this.loadValue();
            return (boolean) this.value;
        }

        public String getKey() {
            return this.key;
        }

        /**
         * @return the setting as an int
         */
        public int getInt() {
            this.loadValue();
            return (int) this.getNumber();
        }

        /**
         * @return the setting as a long
         */
        public long getLong() {
            this.loadValue();
            return (long) this.getNumber();
        }

        /**
         * @return the setting as a double
         */
        public double getDouble() {
            this.loadValue();
            return this.getNumber();
        }

        /**
         * @return the setting as a float
         */
        public float getFloat() {
            this.loadValue();
            return (float) this.getNumber();
        }

        /**
         * @return the setting as a String
         */
        public String getString() {
            this.loadValue();
            return String.valueOf(this.value);
        }

        private double getNumber() {
            if (this.value instanceof Integer) {
                return (int) this.value;
            } else if (this.value instanceof Short) {
                return (short) this.value;
            } else if (this.value instanceof Byte) {
                return (byte) this.value;
            } else if (this.value instanceof Float) {
                return (float) this.value;
            } else if (this.value instanceof Long) {
                return (long) this.value;
            }

            return (double) this.value;
        }

        /**
         * @return the setting as a string list
         */
        @SuppressWarnings("unchecked")
        public List<String> getStringList() {
            this.loadValue();
            return (List<String>) this.value;
        }

        private boolean setIfNotExists(CommentedFileConfiguration fileConfiguration) {
            this.loadValue();

            if (exists && this.excluded) return false;

            if (fileConfiguration.get(this.key) == null) {
                List<String> comments = Stream.of(this.comments).collect(Collectors.toList());
                if (this.defaultValue != null) {
                    fileConfiguration.set(this.key, this.defaultValue, comments.toArray(new String[0]));
                } else {
                    fileConfiguration.addComments(comments.toArray(new String[0]));
                }

                return true;
            }

            return false;
        }

        /**
         * Resets the cached value
         */
        public void reset() {
            this.value = null;
        }

        /**
         * @return true if this setting is only a section and doesn't contain an actual value
         */
        public boolean isSection() {
            return this.defaultValue == null;
        }

        /**
         * Loads the value from the config and caches it if it isn't set yet
         */
        public void loadValue() {
            if (this.value != null) return;
            this.value = CoffeeProtect.getInstance().getConfiguration().getConfig().get(this.key);
        }
    }
}