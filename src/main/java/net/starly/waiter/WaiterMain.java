package net.starly.waiter;

import net.starly.core.bstats.Metrics;
import net.starly.waiter.runnable.TimeCheckSchedule;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class WaiterMain extends JavaPlugin {

    private static JavaPlugin instance;

    public static JavaPlugin getInstance() {
        return instance;
    }

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
        new Metrics(this, 12345); // TODO: 수정
        TimeCheckSchedule.start();


        /* CONFIG
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        saveDefaultConfig();
        reloadConfig();

        /* COMMAND
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        // TODO: 수정

        /* LISTENER
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        // TODO: 수정
    }

    private boolean isPluginEnabled(String name) {
        Plugin plugin = getServer().getPluginManager().getPlugin(name);
        return plugin != null && plugin.isEnabled();
    }
}
