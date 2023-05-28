package net.starly.waiter.listener;

import net.starly.waiter.manager.InventoryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        InventoryManager manager = InventoryManager.getInstance();
        Player player = (Player) event.getPlayer();
        if (manager.has(player)) manager.getOpenPlayers().remove(player);
    }
}
