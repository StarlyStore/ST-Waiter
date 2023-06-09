package net.starly.waiter;

import lombok.Getter;
import net.starly.core.bstats.Metrics;
import net.starly.waiter.command.WaiterCommand;
import net.starly.waiter.command.tabcomplete.WaiterTabComplete;
import net.starly.waiter.listener.InventoryClickListener;
import net.starly.waiter.listener.InventoryCloseListener;
import net.starly.waiter.listener.PlayerJoinListener;
import net.starly.waiter.listener.ServerListPingListener;
import net.starly.waiter.runnable.TimeCheckSchedule;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class WaiterMain extends JavaPlugin {

    @Getter
    private static JavaPlugin instance;

    public void onLoad() {
        instance = this;
    }


    @Override
    public void onEnable() {
        /* DEPENDENCY
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        if (!isPluginEnabled("ST-Core")) {
            getServer().getLogger().warning("[" + getName() + "] ST-Core 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            getServer().getLogger().warning("[" + getName() + "] 다운로드 링크 : §fhttp://starly.kr/");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        /* SETUP
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        new Metrics(this, 18591);
        TimeCheckSchedule.start();


        /* CONFIG
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        saveDefaultConfig();
        /* COMMAND
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        getCommand("대기열").setExecutor(new WaiterCommand());
        getCommand("대기열").setTabCompleter(new WaiterTabComplete());

        /* LISTENER
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new ServerListPingListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);
    }

    private boolean isPluginEnabled(String name) {
        Plugin plugin = getServer().getPluginManager().getPlugin(name);
        return plugin != null && plugin.isEnabled();
    }

    @Override
    public void onDisable() {
        TimeCheckSchedule.stop();
        HandlerList.unregisterAll(this); // 이벤트 중복 방지
    }
}
