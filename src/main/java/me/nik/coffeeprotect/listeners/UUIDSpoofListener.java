package me.nik.coffeeprotect.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class UUIDSpoofListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLogin(PlayerLoginEvent e) {
        //TODO: Check and return on geyser.

        Player player = e.getPlayer();

        String playerName = player.getName();

        //Get the original UUID
        String originalUUID = player.getUniqueId().toString().replace("-", "");

        //Get the offline UUID
        String offlineUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName)
                        .getBytes())
                .toString()
                .replace("-", "");

        //Get the online UUID
        String onlineUUID = null;

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {

            String uuid = "";

            try {

                URLConnection connection = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName).openConnection();

                connection.setDoOutput(true);
                connection.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();

                String inputLine;

                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine).append("\n");
                }

                bufferedReader.close();
                uuid = response.toString();

            } catch (IOException ignored) {
            }

            return uuid;
        });

        try {
            onlineUUID = future.get();
        } catch (InterruptedException | ExecutionException ignored) {
        }

        //Mojang is down, Disable the check to avoid perfomance impact.
        if (onlineUUID == null) {
            HandlerList.unregisterAll(this);
        } else {
            if (!originalUUID.contains(offlineUUID) && !onlineUUID.contains(originalUUID)) {
                e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            }
        }
    }
}