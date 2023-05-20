package net.starly.waiter.listener;

import net.starly.waiter.WaiterMain;
import net.starly.waiter.manager.WaitingManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import util.MessageUtil;

import java.net.InetAddress;

public class ServerListPingListener implements Listener {
    @EventHandler
    public void onListServerPing(ServerListPingEvent event) {
        WaitingManager waitingManager = WaitingManager.getInstance();
        FileConfiguration config = WaiterMain.getInstance().getConfig();
        InetAddress address = event.getAddress();

        event.setMaxPlayers(config.getInt("maxPlayer"));

        if (!waitingManager.has(address)) return;

        if (waitingManager.get(address) > 0) {
            event.setMotd(MessageUtil.format(config.getString("waitLineMOTD"), address));
            return;
        }

        if (waitingManager.isCanJoin()) event.setMotd(MessageUtil.format(config.getString("enterMOTD"), address, true));
        else event.setMotd(MessageUtil.format(config.getString("joinBlockedMOTD"), address));
    }
}
