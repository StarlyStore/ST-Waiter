package net.starly.waiter.util;

import net.starly.waiter.WaiterMain;
import net.starly.waiter.manager.WaitingManager;
import org.bukkit.ChatColor;

import java.net.InetAddress;
import java.util.UUID;

public class MessageUtil {
    public static String format(String string, InetAddress inetAddress) {
        String result = "[ST-Waiter] " + ChatColor.translateAlternateColorCodes('&',string);
        result = result.replace("%remainPlayer%", WaitingManager.getInstance().get(inetAddress).toString());
        result = result.replace("%ip%",inetAddress.getHostName());
        result = result.replace("%priority%", WaitingManager.getInstance().get(inetAddress).toString());
        return result;
    }

    public static String format(String string, InetAddress inetAddress, boolean useRemainTime) {
        String result = format(string,inetAddress);
        if (useRemainTime) {
            result = result.replace("%remainTime%", WaitingManager.getInstance().getRemainTime().getSeconds() + "");
        }
        return result;
    }

    public static String formatExtra(String string, InetAddress inetAddress, UUID uuid) {
        String str = format(string,inetAddress);
        str = str.replace("%uuid%",uuid.toString());
        str = str.replace("%nickname%",WaiterMain.getInstance().getServer().getOfflinePlayer(uuid).getName());
        return str;
    }
}
