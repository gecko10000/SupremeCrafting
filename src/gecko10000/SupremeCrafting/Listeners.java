package gecko10000.SupremeCrafting;

import gecko10000.SupremeCrafting.misc.Utils;
import gecko10000.SupremeCrafting.recipes.CustomRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.Map;

public class Listeners implements Listener {

    public Listeners() {
        Bukkit.getPluginManager().registerEvents(this, SupremeCrafting.getInstance());
    }

    @EventHandler
    public void onPrepareRecipe(PrepareItemCraftEvent evt) {
        CraftingInventory inv = evt.getInventory();
        ItemStack[] matrix = inv.getMatrix();
        for (Map.Entry<String, CustomRecipe> recipe : SupremeCrafting.getInstance().recipes.entrySet()) {
            if (!recipe.getValue().test(matrix)) {
                continue;
            }
            inv.setResult(recipe.getValue().getResult());
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onCraft(InventoryClickEvent evt) {
        Inventory clickedInv = evt.getClickedInventory();
        if (!(clickedInv instanceof CraftingInventory inv) || evt.getSlot() != 0) {
            return;
        }
        ItemStack result = inv.getResult();
        if (Utils.isEmpty(result)) {
            return;
        }
        evt.setCancelled(true);
        Player player = (Player) evt.getWhoClicked();
        PlayerInventory playerInv = player.getInventory();
        InventoryView view = player.getOpenInventory();
        switch (evt.getClick()) {
            case LEFT, RIGHT -> {
                ItemStack alreadyOnCursor = view.getCursor();
                if (!Utils.isEmpty(alreadyOnCursor) && !alreadyOnCursor.isSimilar(result)) {
                    break;
                }
                view.setCursor(new ItemBuilder(result)
                        .setCount(Utils.isEmpty(alreadyOnCursor)
                                ? result.getAmount()
                                : alreadyOnCursor.getAmount() + result.getAmount()));
                decrementMatrix(inv);
            }
            case SHIFT_LEFT, SHIFT_RIGHT -> {
                int iterations = timesToCraft(inv);
                for (int i = 0; i < iterations; i++) {
                    int remaining = shiftCraft(result.clone(), playerInv);
                    if (remaining == result.getAmount()) {
                        break;
                    }
                    decrementMatrix(inv);
                    if (remaining == 0) {
                        continue;
                    }
                    Location location = player.getLocation();
                    Item item = location.getWorld().dropItem(location, new ItemBuilder(result).setCount(remaining));
                    item.setPickupDelay(20);
                    item.setVelocity(location.getDirection());
                    break;
                }
            }
        }
        inv.setResult(null);
        Bukkit.getPluginManager().callEvent(new PrepareItemCraftEvent(inv ,view, false));
    }

    public void decrementMatrix(CraftingInventory inv) {
        for (ItemStack item : inv.getMatrix()) {
            if (Utils.isEmpty(item)) {
                continue;
            }
            item.setAmount(item.getAmount() - 1);
        }
    }

    public int timesToCraft(CraftingInventory inv) {
        int min = 64;
        for (ItemStack item : inv.getMatrix()) {
            if (Utils.isEmpty(item)) {
                continue;
            }
            min = Math.min(min, item.getAmount());
        }
        return min;
    }

    public int shiftCraft(ItemStack result, PlayerInventory playerInv) {
        Map<Integer, ItemStack> extra = Utils.shiftClickAddItem(playerInv, result.clone());
        return extra.size() == 0 ? 0 : extra.get(0).getAmount();
    }

}
