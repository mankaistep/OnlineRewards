package me.manaki.plugin.onlinerewards.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.manaki.plugin.onlinerewards.manager.Managers;
import me.manaki.plugin.onlinerewards.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RewardGUI {

    public static void open(Player player) {
        Map<Long, String> rewards = Managers.getAllRewards();
        List<Long> ordered = Lists.newArrayList(rewards.keySet()).stream().sorted().collect(Collectors.toList());

        Map<Integer, String> slots = Maps.newLinkedHashMap();
        int c = 0;
        for (Long time : ordered) {
            slots.put(c, rewards.get(time));
            c++;
        }
        int size = slots.size() % 9 == 0 ? slots.size() : (slots.size() / 9 + 1) * 9;

        Inventory inv = Bukkit.createInventory(new Holder(slots), size, "§0§lQUÀ ONLINE");
        player.openInventory(inv);

        Tasks.async(() -> {
            for (Integer slot : slots.keySet()) {
                String r = slots.get(slot);
                boolean can = Managers.getAvailables(player).contains(r);
                String remain = null;
                if (!can) {
                    remain = Managers.format(Managers.getRemainSeconds(player, r));
                }
                boolean received = Managers.getData().getReceived(player).contains(r);
                inv.setItem(slot, Managers.get(r).getIcon().build(can, received, remain));
            }
        });
    }

    public static void onClick(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof Holder)) return;
        e.setCancelled(true);
        if (e.getClickedInventory() != e.getWhoClicked().getOpenInventory().getTopInventory()) return;

        var h = (Holder) e.getInventory().getHolder();
        int slot = e.getSlot();
        var player = (Player) e.getWhoClicked();

        if (h.getRewards().containsKey(slot)) {
            String id = h.getRewards().get(slot);
            if (!Managers.getAvailables(player).contains(id)) {
                player.sendMessage("§cBạn chưa thể nhận phần quà này");
                return;
            }
            Managers.giveAndCheck(player, id);
            open(player);
        }
    }

    public static void onDrag(InventoryDragEvent e) {
        if (!(e.getInventory().getHolder() instanceof Holder)) return;
        e.setCancelled(true);
    }

}

class Holder implements InventoryHolder {

    private Map<Integer, String> rewards;

    public Holder(Map<Integer, String> rewards) {
        this.rewards = rewards;
    }

    public Map<Integer, String> getRewards() {
        return rewards;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
