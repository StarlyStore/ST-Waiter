package net.starly.waiter.listener;

import net.starly.waiter.WaiterMain;
import net.starly.waiter.manager.InventoryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryManager manager = InventoryManager.getInstance();
        Player player = (Player) event.getWhoClicked();
        if (!manager.has(player)) return;
        event.setCancelled(true);
    }
}
