package net.starly.waiter.command.tabcomplete;

import net.starly.waiter.WaiterMain;
import net.starly.waiter.manager.WaiterManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WaiterTabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            result.add("목록");
            result.add("삭제");
            result.add("리로드");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("삭제")) {
            for (UUID uuid : WaiterManager.getInstance().getInetAddressMap().values())
                result.add(WaiterMain.getInstance().getServer().getOfflinePlayer(uuid).getName());
        }

        return result;
    }
}
