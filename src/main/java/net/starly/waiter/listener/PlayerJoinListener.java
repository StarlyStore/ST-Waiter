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
import util.MessageUtil;

import java.net.InetAddress;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        WaitingManager waitingManager = WaitingManager.getInstance();
        FileConfiguration config = WaiterMain.getInstance().getConfig();
        if (!waitingManager.isFull()) return; // 정원이 가득 찼는지 확인

        InetAddress address = event.getPlayer().getAddress().getAddress();

        if (!waitingManager.has(address)) {
            waitingManager.add(address, event.getPlayer().getUniqueId());
            event.getPlayer().kickPlayer(MessageUtil.format(config.getString("kickMessage"),address));
            event.setJoinMessage(null);
            return;
        }

        if (!waitingManager.validate(address, event.getPlayer().getUniqueId())) { // UUID 대조
            event.getPlayer().kickPlayer("대기열에 등록된 UUID 와 달라 접속이 거부되었습니다.");
            event.setJoinMessage(null);
            return;
        }

        if (waitingManager.get(address) != 0 || !waitingManager.isCanJoin()) { // 접속 가능여부 확인
            event.getPlayer().kickPlayer(MessageUtil.format(config.getString("kickMessageWaiting"),address));
            event.setJoinMessage(null);
            return;
        }

        waitingManager.next(); // 접속 성공 시 대기열 넘기기

    }
}
