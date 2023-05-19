package net.starly.waiter.listener;

import net.starly.waiter.WaiterMain;
import net.starly.waiter.manager.WaitingManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.net.InetAddress;

public class ServerListPingListener implements Listener {
    @EventHandler
    public void onListServerPing(ServerListPingEvent event) {
        WaitingManager waitingManager = WaitingManager.getInstance();
        FileConfiguration config = WaiterMain.getInstance().getConfig();
        InetAddress address = event.getAddress();

        if (!waitingManager.has(address)) return;

        String formattedMessage;
        if (waitingManager.get(address) == 0) {
            if (waitingManager.isCanJoin()) {
                formattedMessage = ChatColor.translateAlternateColorCodes('&', config.getString("enterMOTD"));
                formattedMessage = formattedMessage.replaceAll("%remainTime%", waitingManager.getRemainTime().getSeconds() + "");
            } else {
                formattedMessage = ChatColor.translateAlternateColorCodes('&', config.getString("joinBlockedMOTD"));
            }
        } else {
            formattedMessage = ChatColor.translateAlternateColorCodes('&', config.getString("waitLineMOTD"));
            formattedMessage = formattedMessage.replaceAll("%remainPlayer%", waitingManager.get(address) + "");
        }
        event.setMotd(formattedMessage);
    }
}
