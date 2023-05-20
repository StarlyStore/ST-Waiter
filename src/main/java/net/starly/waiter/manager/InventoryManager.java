package net.starly.waiter.manager;

import lombok.Getter;
import net.starly.waiter.page.PageData;
import net.starly.waiter.page.PaginationManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class InventoryManager {

    private static InventoryManager instance;

    public static InventoryManager getInstance() {
        if (instance == null) instance = new InventoryManager();
        return instance;
    }

    @Getter
    private final HashMap<Player, PaginationManager> openPlayers = new HashMap<>();


    public void openInventory(PaginationManager paginationManager, Player player) {
        Inventory inventory = Bukkit.createInventory(null, 45, "대기열 목록");
        PageData pageData = paginationManager.getCurrentPageData();
        for (ItemStack itemStack : pageData.getItems()) {
            inventory.addItem(itemStack);
        }
        player.openInventory(inventory);
        addPlayer(player, paginationManager);
    }

    public void addPlayer(Player player, PaginationManager paginationManager) {
        if (openPlayers.containsKey(player)) return;
        openPlayers.put(player, paginationManager);
    }

    public void removePlayer(Player player) {
        if (!openPlayers.containsKey(player)) return;
        openPlayers.remove(player);
    }

    public boolean has(Player player) {
        return openPlayers.containsKey(player);
    }

}
