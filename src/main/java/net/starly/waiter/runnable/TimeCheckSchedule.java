package net.starly.waiter.runnable;

import net.starly.waiter.WaiterMain;
import net.starly.waiter.manager.WaitingManager;
import net.starly.waiter.util.WaitingUtil;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TimeCheckSchedule extends BukkitRunnable {

    private static TimeCheckSchedule instance;

    private final JavaPlugin plugin = WaiterMain.getInstance();

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
        WaitingManager waitingManager = WaitingManager.getInstance();
        if (!WaitingUtil.isFull()) return;

        if (waitingManager.getLength() <= 0) return;

        if (!waitingManager.isCanJoin()) waitingManager.setCanJoin(true);

        if (waitingManager.getRemainTime().getSeconds() == 0) waitingManager.next();
        else waitingManager.getRemainTime().subtract(1);
    }
}
