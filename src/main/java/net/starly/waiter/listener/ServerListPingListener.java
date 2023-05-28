package net.starly.waiter.listener;

import net.starly.waiter.WaiterMain;
import net.starly.waiter.manager.WaitingManager;
import net.starly.waiter.util.MessageUtil;
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

        event.setMaxPlayers(config.getInt("maxPlayer"));

        if (!waitingManager.has(address)) return;

        if (waitingManager.get(address) > 0) {
            event.setMotd(MessageUtil.formatWithoutPrefix(config.getString("motd.waitLineMOTD"), address));
            return;
        }

        if (waitingManager.isCanJoin()) event.setMotd(MessageUtil.formatWithoutPrefix(config.getString("motd.enterMOTD"), address, true));
        else event.setMotd(MessageUtil.formatWithoutPrefix(config.getString("motd.joinBlockedMOTD"), address));
    }
}
