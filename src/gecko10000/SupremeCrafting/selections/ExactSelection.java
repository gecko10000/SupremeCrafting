package gecko10000.SupremeCrafting.selections;

import org.bukkit.inventory.ItemStack;

public class ExactSelection implements RecipeSelection {

    @Override
    public boolean test(ItemStack item) {
        for (ItemStack valid : validItems) {
            if (item.isSimilar(valid)) {
                return true;
            }
        }
        return false;
    }

}
