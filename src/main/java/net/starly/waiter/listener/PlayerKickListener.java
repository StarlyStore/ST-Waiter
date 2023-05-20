package net.starly.waiter.listener;

import net.starly.waiter.WaiterMain;
import net.starly.waiter.manager.WaitingManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class PlayerKickListener implements Listener {
    @EventHandler
    public void onQuit(PlayerKickEvent event) {
        if (WaitingManager.getInstance().has(event.getPlayer().getAddress().getAddress())) {
            event.setLeaveMessage(null);
        }
    }
}
