package gecko10000.SupremeCrafting.recipes;

import org.bukkit.inventory.ItemStack;
import redempt.redlib.configmanager.annotations.ConfigMappable;
import redempt.redlib.configmanager.annotations.ConfigValue;

@ConfigMappable
public abstract class CustomRecipe {

    @ConfigValue
    private ItemStack result;

    private CustomRecipe() {}

    public CustomRecipe(ItemStack result) {
        this.result = result;
    }

    public abstract boolean test(ItemStack... items);

    public ItemStack getResult() {
        return result;
    }
}
