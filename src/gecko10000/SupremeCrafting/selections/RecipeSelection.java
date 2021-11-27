package gecko10000.SupremeCrafting.selections;

import org.bukkit.inventory.ItemStack;
import redempt.redlib.configmanager.ConfigManager;
import redempt.redlib.configmanager.annotations.ConfigMappable;
import redempt.redlib.configmanager.annotations.ConfigValue;

import java.util.Set;
import java.util.function.Predicate;

@ConfigMappable
public interface RecipeSelection extends Predicate<ItemStack> {

    @ConfigValue
    Set<ItemStack> validItems = ConfigManager.set(ItemStack.class);

    @Override
    boolean test(ItemStack itemStack);

}
