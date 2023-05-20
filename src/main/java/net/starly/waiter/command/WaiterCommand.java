package net.starly.waiter.command;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.starly.waiter.WaiterMain;
import net.starly.waiter.converter.UUIDConverter;
import net.starly.waiter.manager.InventoryManager;
import net.starly.waiter.manager.WaitingManager;
import net.starly.waiter.page.PaginationManager;
import net.starly.waiter.util.ListTranslateUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class WaiterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        JavaPlugin plugin = WaiterMain.getInstance();
        FileConfiguration config = plugin.getConfig();
        WaitingManager waitingManager = WaitingManager.getInstance();
        if (args.length == 0) {
            sender.sendMessage(plugin.getName() + " version " + plugin.getDescription().getVersion());
            return true;
        }

        switch (args[0]) {

            case "목록": {
                if (sender instanceof Player) {

                    if (waitingManager.getLength() <= 0) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("errorMessage.emptyWaitLine")));
                        return false;
                    }

                    Player player = (Player) sender;
                    List<ItemStack> itemStackList = ListTranslateUtil.translate(waitingManager.getInetAddressMap());
                    PaginationManager manager = new PaginationManager(itemStackList);
                    InventoryManager.getInstance().openInventory(manager, player);
                } else if (sender instanceof ConsoleCommandSender) {

                    if (waitingManager.getLength() <= 0) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("errorMessage.emptyWaitLine")));
                        return false;
                    }

                    int page = args.length > 1 ? Integer.parseInt(args[0]) - 1 : 0;
                    int max = (page * 10) + 10;

                    if ((page * 10) > waitingManager.getLength()) page = 0;

                    plugin.getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', config.getString("message.consoleListTitle")));

                    for (int i = (page * 10) - 1; i < max; i++) {
                        if (i > waitingManager.getLength() - 1) break;
                        InetAddress address = waitingManager.getWaitingList().get(i);
                        UUID uuid = waitingManager.getInetAddressMap().get(address);
                        String message = util.MessageUtil.formatExtra(config.getString("message.consoleListFormat"), address, uuid);
                        plugin.getLogger().log(Level.INFO, message);
                    }
                } else sender.sendMessage(config.getString("errorMessage.wrongPlatform"));
                break;
            }

            case "삭제": {
                if (args.length < 2) {
                    sender.sendMessage("§c사용법 ) /대기열 삭제 [<닉네임>]");
                    break;
                }

                String uuidString = UUIDConverter.getMinecraftUUID(args[1]);

                if (uuidString.isEmpty() || !waitingManager.getInetAddressMap().containsValue(UUID.fromString(uuidString))) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("errorMessage.noExistPlayer")));
                    break;
                }

                UUID uuid = UUID.fromString(uuidString);
                InetAddress address = waitingManager.getInetAddress(uuid);
                String message = util.MessageUtil.formatExtra(config.getString("message.successRemovePlayerFromWaitList"), address, uuid);
                if (waitingManager.get(address) <= 0) waitingManager.next();
                else {
                    waitingManager.getWaitingList().remove(address);
                    waitingManager.getInetAddressMap().remove(address);
                }
                sender.sendMessage(message);
                break;
            }

            default: {
                for (String str : config.getStringList("message.helpMessage"))
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',str));
                break;
            }
        }

        return false;
    }

}
