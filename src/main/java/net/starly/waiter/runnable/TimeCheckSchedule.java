package net.starly.waiter.runnable;

import net.starly.waiter.WaiterMain;
import net.starly.waiter.manager.WaiterManager;
import org.bukkit.scheduler.BukkitRunnable;

public class TimeCheckSchedule extends BukkitRunnable {

    private static TimeCheckSchedule instance;
    public static void start() {
        instance = new TimeCheckSchedule();
        instance.runTaskTimer(WaiterMain.getInstance(), 0L, 20L);
    }

    public static void stop() {
        if (instance != null) instance.cancel();
        instance = null;
    }

    @Override
    public void run() {

        if (this.isCancelled()) return;

        WaiterManager waiterManager = WaiterManager.getInstance();
        if (WaiterMain.getInstance().getServer().getOnlinePlayers().size() >= WaiterMain.getInstance().getConfig().getInt("maxPlayer")) return;

        if (waiterManager.getLength() <= 0) return;

        if (!waiterManager.isCanJoin()) waiterManager.setCanJoin(true);

        if (waiterManager.getRemainTime().getSeconds() == 0) waiterManager.next();
        else waiterManager.getRemainTime().subtract(1);
    }
}
