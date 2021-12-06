package gecko10000.SupremeCrafting.selections;

import de.tr7zw.changeme.nbtapi.NBTItem;
import gecko10000.SupremeCrafting.misc.TriFunction;
import gecko10000.SupremeCrafting.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public enum ComparisonType {

    MATERIAL((validItems, item, nbt) -> {
        Material type = item.getType();
        return validItems.map(ItemStack::getType).anyMatch(m -> type == m);
    }),
    ENCHANTMENT((validItems, item, nbt) -> {
        Map<Enchantment, Integer> enchants = Utils.getEnchants(item);
        return validItems.map(Utils::getEnchants).anyMatch(e -> e.equals(enchants));
    }),
    POTION((validItems, item, nbt) -> {
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
    CM_DATA((validItems, item, nbt) -> {
        int data = Utils.getCustomModelData(item.getItemMeta());
        return validItems.map(ItemStack::getItemMeta)
                .map(Utils::getCustomModelData)
                .anyMatch(i -> i == data);
    }),
    SKULL((validItems, item, nbt) -> {
        String texture = Utils.getTexture(item);
        return validItems
                .filter(i -> i.getType() == Material.PLAYER_HEAD)
                .map(Utils::getTexture)
                .anyMatch(texture::equals);
    }),
    PDC((validItems, item, nbt) -> {
        PersistentDataContainer pdc = Utils.getPDC(item);
        return validItems.map(Utils::getPDC).anyMatch(pdc::equals);
    }),
    PDC_KEYS((validItems, item, nbt) -> {
        Set<NamespacedKey> keys = Utils.getPDC(item).getKeys();
        return validItems.map(Utils::getPDC)
                .map(PersistentDataContainer::getKeys)
                .anyMatch(keys::equals);
    }),
    EXACT((validItems, item, nbt) -> validItems.anyMatch(item::isSimilar));

    TriFunction<Stream<ItemStack>, ItemStack, String, Boolean> comparison;

    ComparisonType(TriFunction<Stream<ItemStack>, ItemStack, String, Boolean> comparison) {
        this.comparison = comparison;
    }

    boolean test(List<ItemStack> validItems, ItemStack item, String nbt) {
        return comparison.apply(validItems.stream(), item, nbt);
    }

}
