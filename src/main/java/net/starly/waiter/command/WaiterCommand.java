package net.starly.waiter.command;

import net.starly.waiter.WaiterMain;
import net.starly.waiter.converter.UUIDConverter;
import net.starly.waiter.manager.InventoryManager;
import net.starly.waiter.manager.WaiterManager;
import net.starly.waiter.page.PaginationManager;
import net.starly.waiter.util.ListTranslateUtil;
import net.starly.waiter.util.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetAddress;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class WaiterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        JavaPlugin plugin = WaiterMain.getInstance();
        FileConfiguration config = plugin.getConfig();
        WaiterManager waiterManager = WaiterManager.getInstance();
        String prefix = WaiterMain.getInstance().getConfig().getString("message.prefix");
        if (args.length == 0) {
            sender.sendMessage(plugin.getName() + " version " + plugin.getDescription().getVersion());
            return true;
        }

        switch (args[0]) {

            case "목록": {

                if (!sender.hasPermission("starly.waiter.list")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + config.getString("errorMessage.permissionDenied")));
                    break;
                }

                if (sender instanceof Player) {

                    if (waiterManager.getLength() <= 0) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + " " + config.getString("errorMessage.emptyWaitLine")));
                        return false;
                    }

                    Player player = (Player) sender;
                    List<ItemStack> itemStackList = ListTranslateUtil.translate(waiterManager.getInetAddressMap());
                    PaginationManager manager = new PaginationManager(itemStackList);
                    InventoryManager.getInstance().openInventory(manager, player);
                } else if (sender instanceof ConsoleCommandSender) {

                    if (waiterManager.getLength() <= 0) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix + " " + config.getString("errorMessage.emptyWaitLine")));
                        return false;
                    }

                    int page = args.length > 1 ? Integer.parseInt(args[1]) - 1 : 0;
                    int max = (page * 10) + 10;

                    if ((page * 10) > waiterManager.getLength()) page = 0;

                    plugin.getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', prefix + " " + config.getString("message.consoleListTitle")));

                    for (int i = page * 10; i < max; i++) {
                        if (i > waiterManager.getLength() - 1) break;
                        InetAddress address = waiterManager.getWaitingList().get(i);
                        UUID uuid = waiterManager.getInetAddressMap().get(address);
                        String message = MessageUtil.formatExtra(config.getString("message.consoleListFormat"), address, uuid);
                        plugin.getLogger().log(Level.INFO, message);
                    }
                    sender.sendMessage("/대기열 목록 [<페이지>]로 더 많은 페이지를 확인해보세요!");
                } else sender.sendMessage(config.getString("errorMessage.wrongPlatform"));
                break;
            }

            case "삭제": {

                if (!sender.hasPermission("starly.waiter.delete")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + config.getString("errorMessage.permissionDenied")));
                    break;
                }

                if (args.length < 2) {
                    sender.sendMessage("§c사용법 ) /대기열 삭제 [<닉네임>]");
                    break;
                }

                String uuidString = UUIDConverter.getMinecraftUUID(args[1]);

                if (uuidString.isEmpty() || !waiterManager.getInetAddressMap().containsValue(UUID.fromString(uuidString))) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("errorMessage.noExistPlayer")));
                    break;
                }

                UUID uuid = UUID.fromString(uuidString);
                InetAddress address = waiterManager.getInetAddress(uuid);
                String message = MessageUtil.formatExtra(config.getString("message.successRemovePlayerFromWaitList"), address, uuid);
                if (waiterManager.get(address) <= 0) waiterManager.next();
                else {
                    waiterManager.getWaitingList().remove(address);
                    waiterManager.getInetAddressMap().remove(address);
                    waiterManager.getNicknameMap().remove(uuid);
                }
                sender.sendMessage(message);
                break;
            }

            case "리로드": {
                if (!sender.hasPermission("starly.waiter.reload")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + config.getString("errorMessage.permissionDenied")));
                    break;
                }

                plugin.reloadConfig();

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + config.getString("message.reloadComplete")));
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
