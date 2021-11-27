package gecko10000.SupremeCrafting;

import gecko10000.SupremeCrafting.misc.Utils;
import gecko10000.SupremeCrafting.recipes.CustomRecipe;
import org.bukkit.Bukkit;
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
    public void on(PrepareItemCraftEvent evt) {
        Bukkit.broadcastMessage("prepareitemcraft");
        CraftingInventory inv = evt.getInventory();
        ItemStack[] matrix = inv.getMatrix();
        for (ItemStack item : matrix) {
            if (item == null) {
                continue;
            }
            Bukkit.broadcastMessage(item.toString());
        }
        for (Map.Entry<String, CustomRecipe> recipe : SupremeCrafting.getInstance().recipes.entrySet()) {
            if (!recipe.getValue().test(matrix)) {
                continue;
            }
            inv.setResult(recipe.getValue().getResult());
        }
    }

    @EventHandler
    public void onCraft(InventoryClickEvent evt) {
        Bukkit.broadcastMessage("inventoryclick");
        Inventory clickedInv = evt.getClickedInventory();
        if (!(clickedInv instanceof CraftingInventory inv) || evt.getSlot() != 0) {
            return;
        }
        ItemStack result = inv.getResult();
        if (result == null) {
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
                view.setCursor(new ItemBuilder(result).setCount(Utils.isEmpty(alreadyOnCursor) ? 1 : alreadyOnCursor.getAmount() + 1));
                decrementMatrix(inv);
                inv.setResult(null);
            }
        }
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

}
