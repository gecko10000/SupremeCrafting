package gecko10000.SupremeCrafting.recipes;

import gecko10000.SupremeCrafting.selections.RecipeSelection;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class ShapedCustomRecipe extends CustomRecipe {

    public RecipeSelection[][] ingredients = new RecipeSelection[3][3];

    public ShapedCustomRecipe(ItemStack result) {
        super(result);
    }

    public boolean test(ItemStack... items) {
        if (items.length != 9) {
            return false;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                RecipeSelection recipeSelection = ingredients[i][j];
                ItemStack testingAgainst = items[3*i+j];
                if (recipeSelection == null && testingAgainst == null) {
                    continue;
                }
                if (recipeSelection == null || testingAgainst == null) {
                    return false;
                }
                if (!recipeSelection.test(testingAgainst)) {
                    return false;
                }
            }
        }
        return true;
    }

}
