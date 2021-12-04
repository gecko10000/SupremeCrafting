package gecko10000.SupremeCrafting.selections;

import gecko10000.SupremeCrafting.misc.Utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public enum ComparisonType {

    MATERIAL((validItems, item) -> {
        Material type = item.getType();
        return validItems.map(ItemStack::getType).anyMatch(m -> type == m);
    }),
    ENCHANTMENT((validItems, item) -> {
        Map<Enchantment, Integer> enchants = Utils.getEnchants(item);
        return validItems.map(Utils::getEnchants).anyMatch(e -> e.equals(enchants));
    }),
    POTION((validItems, item) -> {
        if (!(item.getItemMeta() instanceof PotionMeta meta)) {
            return true;
        }
        PotionData data = meta.getBasePotionData();
        List<PotionEffect> effects = Utils.getPotionEffects(meta);
        return validItems.map(ItemStack::getItemMeta)
                .filter(PotionMeta.class::isInstance)
                .map(PotionMeta.class::cast)
                .anyMatch(m -> data.equals(m.getBasePotionData()) && effects.containsAll(Utils.getPotionEffects(m)));
    }),
    CUSTOM_MODEL_DATA((validItems, item) -> {
        int data = Utils.getCustomModelData(item.getItemMeta());
        return validItems.map(ItemStack::getItemMeta)
                .map(Utils::getCustomModelData)
                .anyMatch(i -> i == data);
    }),
    EXACT((validItems, item) -> validItems.anyMatch(item::isSimilar));

    BiFunction<Stream<ItemStack>, ItemStack, Boolean> comparison;

    ComparisonType(BiFunction<Stream<ItemStack>, ItemStack, Boolean> comparison) {
        this.comparison = comparison;
    }

    boolean test(List<ItemStack> validItems, ItemStack item) {
        return comparison.apply(validItems.stream(), item);
    }

}
