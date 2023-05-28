package net.starly.waiter.manager;

import lombok.Getter;
import lombok.Setter;

import net.starly.core.data.Time;
import net.starly.waiter.WaiterMain;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class WaiterManager {
    private static WaiterManager instance;

    public static WaiterManager getInstance() {
        if (instance == null) instance = new WaiterManager();
        return instance;
    }

    private final FileConfiguration configuration = WaiterMain.getInstance().getConfig();

    @Getter
    private final List<InetAddress> waitingList = new ArrayList<>();

    @Getter
    private final HashMap<InetAddress, UUID> inetAddressMap = new HashMap<>();

    @Getter
    private final HashMap<UUID, String> nicknameMap = new HashMap<>();

    @Getter
    private final Time remainTime = new Time(configuration.getInt("enterTime"));

    @Getter
    @Setter
    private boolean canJoin = false;

    public Integer get(InetAddress inetAddress) {
        System.out.println(waitingList);
        return waitingList.indexOf(inetAddress);
    }

    public void add(InetAddress inetAddress, Player player) {
        if (waitingList.contains(inetAddress)) return;
        waitingList.add(inetAddress);
        inetAddressMap.put(inetAddress, player.getUniqueId());
        nicknameMap.put(player.getUniqueId(), player.getName());
    }

    public boolean has(InetAddress inetAddress) {
        return waitingList.contains(inetAddress);
    }

    public void next() {
        InetAddress address = waitingList.get(0);
        nicknameMap.remove(inetAddressMap.get(address));
        inetAddressMap.remove(address);
        waitingList.remove(address);
        remainTime.setSeconds(configuration.getInt("enterTime"));
        canJoin = false;
    }

    public InetAddress getInetAddress(UUID uuid) {
        for (InetAddress address : inetAddressMap.keySet()) {
            if (inetAddressMap.get(address).equals(uuid)) {
                return address;
            }
        }
        return null;
    }

    public String getNickname(UUID uuid) {
        return nicknameMap.get(uuid);
    }

    public int getLength() {
        return waitingList.size();
    }

    public boolean validate(InetAddress inetAddress, UUID uuid) {
        if (!inetAddressMap.containsKey(inetAddress)) return false;
        return inetAddressMap.get(inetAddress).equals(uuid);
    }
}
