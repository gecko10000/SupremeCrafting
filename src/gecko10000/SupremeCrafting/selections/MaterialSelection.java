package gecko10000.SupremeCrafting.selections;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialSelection implements RecipeSelection {

    public MaterialSelection(Material m) {
        this(new ItemStack(m));
    }

    public MaterialSelection(ItemStack item) {
        validItems.add(item);
    }

    @Override
    public boolean test(ItemStack item) {
        Material itemType = item.getType();
        for (ItemStack valid : validItems) {
            if (itemType == valid.getType()) {
                return true;
            }
        }
        return false;
    }

}
