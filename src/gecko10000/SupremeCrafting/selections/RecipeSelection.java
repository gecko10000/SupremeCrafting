package gecko10000.SupremeCrafting.selections;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.configmanager.ConfigManager;
import redempt.redlib.configmanager.annotations.ConfigMappable;
import redempt.redlib.configmanager.annotations.ConfigValue;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ConfigMappable
public class RecipeSelection implements Predicate<ItemStack> {

    @ConfigValue
    public final List<ItemStack> validItems = ConfigManager.list(ItemStack.class);
    @ConfigValue
    public Set<ComparisonType> comparisons = ConfigManager.set(ComparisonType.class, ComparisonType.MATERIAL);
    @ConfigValue
    private String data = null;

    public RecipeSelection() {}

    public RecipeSelection(Material material) {
        this(new ItemStack(material));
    }

    public RecipeSelection(ItemStack item) {
        add(item);
    }

    public RecipeSelection add(ItemStack item) {
        validItems.add(new ItemStack(item));
        return this;
    }

    public RecipeSelection remove(ItemStack item) {
        validItems.remove(item);
        return this;
    }

    public RecipeSelection add(ComparisonType comparison) {
        comparisons.add(comparison);
        return this;
    }

    public RecipeSelection remove(ComparisonType comparison) {
        comparisons.remove(comparison);
        return this;
    }

    public String getData() {
        return data;
    }

    public RecipeSelection setData(String data) {
        this.data = data;
        return this;
    }

    @Override
    public boolean test(ItemStack item) {
        return comparisons.contains(ComparisonType.EXACT)
                ? ComparisonType.EXACT.test(validItems, item, data)
                : comparisons.stream().allMatch(t -> t.test(validItems, item, data));
    }

    @Override
    public String toString() {
        return "RecipeSelection{" +
                validItems.stream()
                .map(ItemStack::toString)
                .collect(Collectors.joining(", "))
                + "}";
    }

}
