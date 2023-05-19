package net.starly.waiter.listener;

import net.starly.waiter.WaiterMain;
import net.starly.waiter.util.WaitingUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (WaitingUtil.isFull()) {

        }
    }
}
