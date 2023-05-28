package net.starly.waiter.listener;

import net.starly.waiter.WaiterMain;
import net.starly.waiter.manager.WaiterManager;
import net.starly.waiter.util.MessageUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.net.InetAddress;

public class ServerListPingListener implements Listener {
    @EventHandler
    public void onListServerPing(ServerListPingEvent event) {
        WaiterManager waiterManager = WaiterManager.getInstance();
        FileConfiguration config = WaiterMain.getInstance().getConfig();
        InetAddress address = event.getAddress();

        event.setMaxPlayers(config.getInt("maxPlayer"));

        if (!waiterManager.has(address)) return;

        if (waiterManager.get(address) > 0) {
            event.setMotd(MessageUtil.formatWithoutPrefix(config.getString("motd.waitLineMOTD"), address));
            return;
        }

        if (waiterManager.isCanJoin()) event.setMotd(MessageUtil.formatWithoutPrefix(config.getString("motd.enterMOTD"), address, true));
        else event.setMotd(MessageUtil.formatWithoutPrefix(config.getString("motd.joinBlockedMOTD"), address));
    }
}
