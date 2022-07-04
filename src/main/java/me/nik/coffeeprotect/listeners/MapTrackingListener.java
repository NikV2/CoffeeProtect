package me.nik.coffeeprotect.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

public class MapTrackingListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEntityEvent e) {

        Entity clickedEntity = e.getRightClicked();

        //Not an item frame, return.
        if (!(clickedEntity instanceof ItemFrame)) return;

        ItemFrame itemFrame = (ItemFrame) clickedEntity;

        //Non empty item frame, return.
        if (!itemFrame.isEmpty()) return;

        PlayerInventory inventory = e.getPlayer().getInventory();
        ItemStack item = inventory.getItem(inventory.getHeldItemSlot());

        //Null or no item meta, return.
        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();

        //Not a map, return.
        if (!(meta instanceof MapMeta)) return;

        MapMeta mapMeta = (MapMeta) meta;
        MapView mapView = mapMeta.getMapView();

        if (mapView.isTrackingPosition()) {

            mapView.setTrackingPosition(false);

            mapMeta.setMapView(mapView);

            item.setItemMeta(mapMeta);
        }
    }
}