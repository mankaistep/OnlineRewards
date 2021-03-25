package me.manaki.plugin.onlinerewards.manager;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class Holder implements InventoryHolder {

    private String rewardID;

    public Holder(String rewardID) {
        this.rewardID = rewardID;
    }

    public String getRewardID() {
        return rewardID;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }

}
