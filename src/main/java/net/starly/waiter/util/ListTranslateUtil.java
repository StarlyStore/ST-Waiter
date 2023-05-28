package net.starly.waiter.util;

import net.starly.core.jb.util.PlayerSkullManager;

import net.starly.waiter.WaiterMain;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ListTranslateUtil {

    public static List<ItemStack> translate(HashMap<InetAddress, UUID> map) {
        List<ItemStack> result = new ArrayList<>();
        JavaPlugin plugin = WaiterMain.getInstance();
        map.forEach((inetAddress, uuid) -> {
                ItemStack itemStack = PlayerSkullManager.getPlayerSkull(uuid);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(MessageUtil.formatExtraWithoutPrefix(plugin.getConfig().getString("gui.itemDisplayName"),inetAddress,uuid));
                itemMeta.setLore(translatePlaceHolder(plugin.getConfig().getStringList("gui.itemLore"),uuid,inetAddress));
                itemStack.setItemMeta(itemMeta);
                result.add(itemStack);
            }
        );
        return result;
    }

    public static List<String> translatePlaceHolder(List<String> list, UUID uuid, InetAddress inetAddress) {
        List<String> result = new ArrayList<>();
        for (String str : list) {
            str = MessageUtil.formatExtraWithoutPrefix(str,inetAddress,uuid);
            result.add(str);
        }
        return result;
    }
}
