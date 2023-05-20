package util;

import net.starly.waiter.manager.WaitingManager;
import org.bukkit.ChatColor;

import java.net.InetAddress;

public class MessageUtil {
    public static String format(String string, InetAddress inetAddress) {
        String result = ChatColor.translateAlternateColorCodes('&',string);
        result = result.replaceAll("%remainPlayer%", WaitingManager.getInstance().get(inetAddress) + "");
        return result;
    }

    public static String format(String string, InetAddress inetAddress, boolean useRemainTime) {
        String result = ChatColor.translateAlternateColorCodes('&',string);
        result = result.replaceAll("%remainPlayer%", WaitingManager.getInstance().get(inetAddress) + "");
        if (useRemainTime) {
            result = result.replaceAll("%remainTime%", WaitingManager.getInstance().getRemainTime().getSeconds() + "");
        }
        return result;
    }
}
