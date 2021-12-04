package gecko10000.SupremeCrafting.recipes;

import gecko10000.SupremeCrafting.selections.RecipeSelection;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.configmanager.ConfigManager;
import redempt.redlib.configmanager.annotations.ConfigValue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ShapedCustomRecipe extends CustomRecipe {

    @ConfigValue
    private List<RecipeSelection> savedIngredients = ConfigManager.list(RecipeSelection.class,
            (RecipeSelection) null, null, null, null, null, null, null, null, null);

    private RecipeSelection[][] ingredients = new RecipeSelection[3][3];

    public ShapedCustomRecipe(ItemStack result) {
        super(result);
    }

    public ShapedCustomRecipe set(int i, RecipeSelection selection) {
        savedIngredients.set(i, selection);
        saveToArray();
        return this;
    }

    private void saveToArray() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ingredients[i][j] = savedIngredients.get(i*3+j);
            }
        }
        shave();
    }

    public void shave() {
        ingredients = shaveNull(ingredients, (w, h) -> new RecipeSelection[h][w]);
    }

    @Override
    public boolean test(ItemStack... items) {
        if (items.length == 0) {
            return false;
        }
        int sqrt = (int) Math.sqrt(items.length);
        ItemStack[][] twoD = new ItemStack[sqrt][sqrt];
        for (int i = 0; i < sqrt; i++) {
            System.arraycopy(items, i * 3, twoD[i], 0, sqrt);
        }
        return test(twoD);
    }

    private boolean test(ItemStack[][] items) {
        ItemStack[][] shaved = shaveNull(items, (w, h) -> new ItemStack[h][w]);
        if (shaved.length != ingredients.length || shaved[0].length != ingredients[0].length) {
            return false;
        }
        for (int i = 0; i < shaved.length; i++) {
            for (int j = 0; j < shaved[0].length; j++) {
                RecipeSelection recipeSelection = ingredients[i][j];
                ItemStack testingAgainst = shaved[i][j];
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
    /* column (same index from all subarrays)
       [[0, 1, 2], row (subarray)
       [3, 4, 5],
       [6, 7, 8]]

       goal: shave empty rows/columns off the left, right, top, and bottom
    */
    private <T> T[][] shaveNull(T[][] input, BiFunction<Integer, Integer, T[][]> consumer) {
        // left, top, right, bottom
        int[] offSides = new int[4];
        boolean[] foundSides = new boolean[4];
        for (int i = 0; i < 4; i++) {
            while (!foundSides[i] && offSides[i] < (i % 2 == 0 ? input[0] : input).length) {
                for (int j = 0; j < input.length; j++) {
                    T sel = switch (i) {
                        case 0 -> input[j][offSides[0]]; //left
                        case 1 -> input[offSides[1]][j]; //top
                        case 2 -> input[j][input[0].length-1-offSides[2]]; //right
                        case 3 -> input[input.length-1-offSides[3]][j]; //bottom
                        default -> null;
                    };
                    if (sel != null) {
                        foundSides[i] = true;
                        break;
                    }
                }
                if (!foundSides[i]) {
                    offSides[i]++;
                }
            }
        }
        if (offSides[0] >= input[0].length || offSides[1] >= input.length) {
            return consumer.apply(0, 0);
        }
        int width = input[0].length - offSides[2] - offSides[0];
        int height = input.length - offSides[3] - offSides[1];
        T[][] output = consumer.apply(width, height);
        // go through every row
        for (int i = 0; i < height; i++) {
            // copy values from left to right from array that's down i+top rows
            System.arraycopy(input[i + offSides[1]], offSides[0], output[i], 0, width);
        }
        return output;
    }

}
