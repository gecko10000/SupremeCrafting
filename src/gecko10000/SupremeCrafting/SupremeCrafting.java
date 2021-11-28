package gecko10000.SupremeCrafting;

import gecko10000.SupremeCrafting.recipes.CustomRecipe;
import gecko10000.SupremeCrafting.recipes.ShapedCustomRecipe;
import gecko10000.SupremeCrafting.selections.MaterialSelection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.HashMap;
import java.util.Map;

public class SupremeCrafting extends JavaPlugin {

    private static SupremeCrafting instance;

    public final Map<String, CustomRecipe> recipes = new HashMap<>();

    public void onEnable() {
        instance = this;
        new Listeners();
        ShapedCustomRecipe recipe = new ShapedCustomRecipe(new ItemBuilder(Material.BEDROCK).setCount(64));
        recipe.ingredients[0][0] = new MaterialSelection(Material.YELLOW_DYE);
        recipe.ingredients[1][0] = new MaterialSelection(Material.BEACON);
        recipes.put("test", recipe);
    }

    public static SupremeCrafting getInstance() {
        return instance;
    }
}
