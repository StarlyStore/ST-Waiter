package net.starly.waiter.util;

import net.starly.waiter.WaiterMain;
import org.bukkit.plugin.java.JavaPlugin;

public class WaitingUtil {
    private static JavaPlugin plugin = WaiterMain.getInstance();

    public static boolean isFull() {
        return plugin.getServer().getOnlinePlayers().size() >= plugin.getConfig().getInt("maxPlayer");
    }
}
