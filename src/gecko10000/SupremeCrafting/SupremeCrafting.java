package gecko10000.SupremeCrafting;

import gecko10000.SupremeCrafting.recipes.CustomRecipe;
import gecko10000.SupremeCrafting.recipes.ShapedCustomRecipe;
import gecko10000.SupremeCrafting.selections.ComparisonType;
import gecko10000.SupremeCrafting.selections.RecipeSelection;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.configmanager.annotations.ConfigValue;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.HashMap;
import java.util.Map;

public class SupremeCrafting extends JavaPlugin {

    private static SupremeCrafting instance;

    @ConfigValue
    public final Map<String, CustomRecipe> recipes = new HashMap<>();

    public void onEnable() {
        instance = this;
        new Listeners();
        ShapedCustomRecipe recipe = new ShapedCustomRecipe(new ItemBuilder(Material.BEDROCK).setCount(64));
        recipe.set(0, new RecipeSelection(new ItemBuilder(Material.BEACON).addEnchant(Enchantment.DURABILITY, 1)));
        recipe.set(8, new RecipeSelection(new ItemBuilder(Material.PLAYER_HEAD)));
        recipe.shave();
        recipes.put("test", recipe);
    }

    public static SupremeCrafting getInstance() {
        return instance;
    }
}
