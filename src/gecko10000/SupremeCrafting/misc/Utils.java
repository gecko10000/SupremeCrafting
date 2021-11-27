package gecko10000.SupremeCrafting.misc;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Utils {

    public static boolean isEmpty(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }

}
