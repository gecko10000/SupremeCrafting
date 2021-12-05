package gecko10000.SupremeCrafting.recipes;

import gecko10000.SupremeCrafting.misc.Utils;
import gecko10000.SupremeCrafting.selections.RecipeSelection;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.configmanager.ConfigManager;
import redempt.redlib.configmanager.annotations.ConfigValue;

import java.util.ArrayList;
import java.util.List;

public class ShapelessCustomRecipe extends CustomRecipe {

    @ConfigValue
    private List<RecipeSelection> requiredSelections = ConfigManager.list(RecipeSelection.class);

    public ShapelessCustomRecipe(ItemStack result) {
        super(result);
    }

    public ShapelessCustomRecipe add(RecipeSelection selection) {
        requiredSelections.add(selection);
        return this;
    }

    public ShapelessCustomRecipe remove(RecipeSelection selection) {
        requiredSelections.remove(selection);
        return this;
    }

    @Override
    public boolean test(ItemStack... items) {
        List<RecipeSelection> requiredClone = new ArrayList<>(requiredSelections);
        for (ItemStack item : items) {
            if (Utils.isEmpty(item)) {
                continue;
            }
            // find the item in the remaining required
            boolean foundMatch = false;
            for (RecipeSelection required : requiredClone) {
                if (!required.test(item)) {
                    continue;
                }
                requiredClone.remove(required);
                foundMatch = true;
                break;
            }
            // item is in crafting matrix that shouldn't be there
            if (!foundMatch) {
                return false;
            }
            if (requiredClone.isEmpty()) {
                break;
            }
        }
        return requiredClone.isEmpty();
    }

}
