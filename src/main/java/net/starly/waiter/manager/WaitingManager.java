package net.starly.waiter.manager;

import lombok.Getter;
import lombok.Setter;

import net.starly.core.data.Time;
import net.starly.waiter.WaiterMain;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class WaitingManager {
    private static WaitingManager instance;

    public static WaitingManager getInstance() {
        if (instance == null) instance = new WaitingManager();
        return instance;
    }

    private final FileConfiguration configuration = WaiterMain.getInstance().getConfig();
    private final List<InetAddress> list = new ArrayList<>();

    @Getter
    private final Time remainTime = new Time(configuration.getInt("enterTime"));

    @Getter
    @Setter
    private boolean canJoin = false;

    public Integer get(InetAddress inetAddress) {
        return list.indexOf(inetAddress);
    }

    public void add(InetAddress inetAddress) {
        list.add(inetAddress);
    }

    public boolean has(InetAddress inetAddress) {
        return list.contains(inetAddress);
    }

    public void next() {
        list.remove(0);
        remainTime.setSeconds(configuration.getInt("enterTime"));
        canJoin = false;
    }

    public int getLength() {
        return list.size();
    }

    public boolean isFull() {
        JavaPlugin plugin = WaiterMain.getInstance();
        return plugin.getServer().getOnlinePlayers().size() >= plugin.getConfig().getInt("maxPlayer");
    }
}
