package me.nik.coffeeprotect.listeners;

import me.nik.coffeeprotect.utils.MiscUtils;
import me.nik.coffeeprotect.utils.ServerUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class InvalidPlacementsListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent e) {

        Block block = e.getBlockPlaced();

        Material blockType = block.getType();

        if (blockType == null || !blockType.isSolid()) return;

        Block blockAgainst = e.getBlockAgainst();

        BlockFace face = block.getFace(blockAgainst);

        //This condition is always true if a block is placed against air
        if (face != BlockFace.SELF) return;

        Player player = e.getPlayer();

        ItemStack mainHand = player.getItemInHand();
        ItemStack offHand = ServerUtils.isElytraUpdate() ? player.getInventory().getItemInOffHand() : MiscUtils.EMPTY_ITEM;

        boolean holding = mainHand.getType() == blockType || offHand.getType() == blockType;

        //Make sure the player is not using any type of tools and that was actually a block placement on an empty block.
        if (block.getRelative(BlockFace.DOWN).getType().isSolid() && holding) {

            e.setCancelled(true);
        }
    }
}