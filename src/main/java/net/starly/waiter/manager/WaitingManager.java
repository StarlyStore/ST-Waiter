package net.starly.waiter.manager;

import lombok.Getter;
import lombok.Setter;

import net.starly.core.data.Time;
import net.starly.waiter.WaiterMain;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class WaitingManager {
    private static WaitingManager instance;

    public static WaitingManager getInstance() {
        if (instance == null) instance = new WaitingManager();
        return instance;
    }

    private final FileConfiguration configuration = WaiterMain.getInstance().getConfig();
    private final List<InetAddress> waitingList = new ArrayList<>();
    private final HashMap<InetAddress, UUID> inetAddressMap = new HashMap<>();

    @Getter
    private final Time remainTime = new Time(configuration.getInt("enterTime"));

    @Getter
    @Setter
    private boolean canJoin = false;

    public Integer get(InetAddress inetAddress) {
        return waitingList.indexOf(inetAddress);
    }

    public void add(InetAddress inetAddress, UUID uuid) {
        if (waitingList.contains(inetAddress)) return;
        waitingList.add(inetAddress);
        inetAddressMap.put(inetAddress, uuid);
    }

    public boolean has(InetAddress inetAddress) {
        return waitingList.contains(inetAddress);
    }

    public void next() {
        inetAddressMap.remove(waitingList.get(0));
        waitingList.remove(0);
        remainTime.setSeconds(configuration.getInt("enterTime"));
        canJoin = false;
    }

    public int getLength() {
        return waitingList.size();
    }

    public boolean isFull() {
        JavaPlugin plugin = WaiterMain.getInstance();
        return plugin.getServer().getOnlinePlayers().size() >= plugin.getConfig().getInt("maxPlayer");
    }

    public boolean validate(InetAddress inetAddress, UUID uuid) {
        if (!inetAddressMap.containsKey(inetAddress)) return false;
        return inetAddressMap.get(inetAddress).equals(uuid);
    }
}
