package me.manaki.plugin.onlinerewards.listener;

import me.manaki.plugin.onlinerewards.gui.RewardGUI;
import me.manaki.plugin.onlinerewards.manager.Managers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RewardListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Managers.join(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Managers.quit(player);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        RewardGUI.onClick(e);
    }

    @EventHandler
    public void onClick(InventoryDragEvent e) {
        RewardGUI.onDrag(e);
    }

}
