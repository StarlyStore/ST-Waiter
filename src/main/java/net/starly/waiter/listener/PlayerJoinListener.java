package net.starly.waiter.listener;

import net.starly.waiter.WaiterMain;
import net.starly.waiter.manager.WaitingManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import net.starly.waiter.util.MessageUtil;

import java.net.InetAddress;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerLoginEvent event) {
        if (event.getPlayer().hasPermission("starly.st-waiter.bypass")) return;

        WaitingManager waitingManager = WaitingManager.getInstance();
        FileConfiguration config = WaiterMain.getInstance().getConfig();
        JavaPlugin plugin = WaiterMain.getInstance();
        if (waitingManager.getLength() <= 0 && plugin.getServer().getOnlinePlayers().size() < plugin.getConfig().getInt("maxPlayer"))
            return;

        if (event.getPlayer().isBanned()) return;

        InetAddress address = event.getAddress();

        if (!waitingManager.has(address)) {
            waitingManager.add(address, event.getPlayer());
            event.disallow(PlayerLoginEvent.Result.KICK_FULL, MessageUtil.formatWithoutPrefix(config.getString("errorMessage.kickMessage"), address));
            return;
        }

        if (!waitingManager.validate(address, event.getPlayer().getUniqueId())) { // UUID 대조
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, MessageUtil.formatWithoutPrefix(config.getString("errorMessage.wrongUUID"),address));
            return;
        }

        if (waitingManager.get(address) != 0 || !waitingManager.isCanJoin()) { // 접속 가능여부 확인
            event.disallow(PlayerLoginEvent.Result.KICK_FULL, MessageUtil.formatWithoutPrefix(config.getString("errorMessage.kickMessageWaiting"), address));
            return;
        }

        waitingManager.next(); // 접속 성공 시 대기열 넘기기

    }
}
