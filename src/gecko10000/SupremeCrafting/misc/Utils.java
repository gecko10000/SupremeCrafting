package gecko10000.SupremeCrafting.misc;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTListCompound;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class Utils {

    public static boolean isEmpty(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }

    public static Map<Enchantment, Integer> getEnchants(ItemStack item) {
        if (isEmpty(item)) {
            return new HashMap<>();
        }
        if (item.getItemMeta() instanceof EnchantmentStorageMeta storageMeta) {
            return storageMeta.getStoredEnchants();
        }
        return item.getEnchantments();
    }

    public static List<PotionEffect> getPotionEffects(PotionMeta meta) {
        return meta.hasCustomEffects() ? meta.getCustomEffects() : new ArrayList<>();
    }

    public static int getCustomModelData(ItemMeta meta) {
        return meta.hasCustomModelData() ? meta.getCustomModelData() : -1;
    }

    public static String getTexture(ItemStack head) {
        return Optional.ofNullable(new NBTItem(head).getCompound("SkullOwner"))
                .map(c -> c.getCompound("Properties"))
                .map(c -> c.getCompoundList("textures"))
                .map(l -> l.stream()
                        .filter(c -> c.getString("Value") != null)
                        .findFirst()
                        .orElse(null))
                .map(t -> t.getString("Value")).orElse("");
    }

    public static PersistentDataContainer getPDC(ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer();
    }

    // definitely not adapted from Spigot
    public static Map<Integer, ItemStack> shiftClickAddItem(Inventory inv, ItemStack... items) {
        Validate.noNullElements(items, "Item cannot be null");
        HashMap<Integer, ItemStack> leftover = new HashMap<>();
        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            while (true) {
                // Do we already have a stack of it?
                int firstPartial = firstPartial(inv, item);

                // Drat! no partial stack
                if (firstPartial == -1) {
                    // Find a free spot!
                    int firstFree = Utils.shiftClickFirstEmpty(inv);

                    if (firstFree == -1) {
                        // No space at all!
                        leftover.put(i, item);
                        break;
                    } else {
                        // More than a single stack!
                        if (item.getAmount() > getMaxItemStack(inv)) {
                            ItemStack stack = new ItemStack(item);
                            stack.setAmount(getMaxItemStack(inv));
                            inv.setItem(firstFree, stack);
                            item.setAmount(item.getAmount() - getMaxItemStack(inv));
                        } else {
                            // Just store it
                            inv.setItem(firstFree, item);
                            break;
                        }
                    }
                } else {
                    // So, apparently it might only partially fit, well lets do just that
                    ItemStack partialItem = inv.getItem(firstPartial);

                    int amount = item.getAmount();
                    int partialAmount = partialItem.getAmount();
                    int maxAmount = partialItem.getMaxStackSize();

                    // Check if it fully fits
                    if (amount + partialAmount <= maxAmount) {
                        partialItem.setAmount(amount + partialAmount);
                        // To make sure the packet is sent to the client
                        inv.setItem(firstPartial, partialItem);
                        break;
                    }

                    // It fits partially
                    partialItem.setAmount(maxAmount);
                    // To make sure the packet is sent to the client
                    inv.setItem(firstPartial, partialItem);
                    item.setAmount(amount + partialAmount - maxAmount);
                }
            }
        }
        return leftover;
    }

    private static int firstPartial(Inventory inv, ItemStack item) {
        ItemStack[] inventory = inv.getStorageContents();
        if (item == null) {
            return -1;
        }
        ItemStack filteredItem = new ItemStack(item);
        for (int i = 0; i < inventory.length; i++) {
            ItemStack cItem = inventory[i];
            if (cItem != null && cItem.getAmount() < cItem.getMaxStackSize() && cItem.isSimilar(filteredItem)) {
                return i;
            }
        }
        return -1;
    }

    private static int getMaxItemStack(Inventory inv) {
        return inv.getMaxStackSize();
    }

    private static int shiftClickFirstEmpty(Inventory inv) {
        ItemStack[] contents = inv.getContents();
        int index = 9;
        do {
            if (index == 0) {
                index = 36;
            }
            index--;
            if (isEmpty(contents[index])) {
                return index;
            }
        } while (index != 9);
        return -1;
    }

}
