package me.nik.coffeeprotect.managers.profile;

import me.nik.coffeeprotect.managers.AbstractManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProfileManager implements AbstractManager {

    private final Map<UUID, Profile> profiles = new ConcurrentHashMap<>();

    public Profile getProfile(Player player) {
        return this.profiles.computeIfAbsent(player.getUniqueId(), uuid -> new Profile(player));
    }

    public Map<UUID, Profile> getProfileMap() {
        return profiles;
    }

    @Override
    public void initialize() {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(Objects::nonNull)
                .forEach(this::getProfile);
    }

    @Override
    public void shutdown() {
        this.profiles.clear();
    }
}