package me.nik.coffeeprotect.checks.impl;

import com.comphenix.protocol.events.PacketEvent;
import me.nik.coffeeprotect.api.custom.CheckResult;
import me.nik.coffeeprotect.checks.Check;
import me.nik.coffeeprotect.checks.annotations.CheckInfo;
import me.nik.coffeeprotect.files.Config;
import me.nik.coffeeprotect.managers.profile.Profile;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.nio.charset.StandardCharsets;

@CheckInfo(name = "Invalid Item", description = "Checks for invalid items")
public class InvalidItem extends Check {
    public InvalidItem(Profile profile) {
        super(profile, Config.Setting.CHECKS_INVALID_ITEM_ENABLED.getBoolean());
    }

    @Override
    public CheckResult handle(PacketEvent e) {
        return e.getPacket().getItemModifier().getValues()
                .stream()
                .anyMatch(this::invalidItemStack)
                ? new CheckResult(this, "Invalid ItemStack")
                : null;
    }

    private boolean invalidItemStack(ItemStack item) {

        if (item != null && item.hasItemMeta()) {

            ItemMeta itemMeta = item.getItemMeta();

            if (itemMeta.hasDisplayName()
                    && itemMeta.getDisplayName().length() > Config.Setting.CHECKS_INVALID_ITEM_MAX_DISPLAYNAME_LENGTH.getInt()) {
                return true;
            }

            if (itemMeta.hasLore() && itemMeta.getLore().size() > Config.Setting.CHECKS_INVALID_ITEM_MAX_LORE_SIZE.getInt()) {
                return true;
            }

            if (itemMeta instanceof BookMeta) {

                BookMeta bookMeta = (BookMeta) itemMeta;

                if (bookMeta.hasTitle()
                        && bookMeta.getTitle().length() > Config.Setting.CHECKS_INVALID_ITEM_MAX_BOOK_TITLE_LENGTH.getInt()) {
                    return true;
                }

                if (bookMeta.hasAuthor() && bookMeta.getAuthor().length() > Config.Setting.CHECKS_INVALID_ITEM_MAX_BOOK_AUTHOR_LENGTH.getInt()) {
                    return true;
                }

                if (bookMeta.getPageCount() > Config.Setting.CHECKS_INVALID_ITEM_MAX_BOOK_PAGE_COUNT.getInt()) {
                    return true;
                }

                int pageBytes;

                for (String page : bookMeta.getPages()) {

                    pageBytes = page.getBytes(StandardCharsets.UTF_8).length;

                    if (pageBytes > Config.Setting.CHECKS_INVALID_ITEM_MAX_BOOK_PAGE_BYTES.getInt()) return true;
                }

            } else if (itemMeta instanceof BlockStateMeta) {

                return invalidBlockState((BlockStateMeta) itemMeta);
            }
        }

        return false;
    }

    private boolean invalidBlockState(BlockStateMeta meta) {

        if (meta.hasBlockState()) {

            BlockState blockState = meta.getBlockState();

            if (blockState instanceof InventoryHolder) {

                InventoryHolder inventoryHolder = (InventoryHolder) blockState;

                for (ItemStack item : inventoryHolder.getInventory().getContents()) {

                    if (invalidItemStack(item)) return true;
                }
            }
        }

        return false;
    }
}