package me.manaki.plugin.onlinerewards.reward;

import com.google.common.collect.Lists;
import me.manaki.plugin.onlinerewards.manager.Managers;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RewardIcon {

    private Material material;
    private int modelData;
    private String name;
    private List<String> lore;

    public RewardIcon(Material material, int modelData, String name, List<String> lore) {
        this.material = material;
        this.modelData = modelData;
        this.name = name;
        this.lore = lore;
    }

    public Material getMaterial() {
        return material;
    }

    public int getModelData() {
        return modelData;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public ItemStack build(boolean can, boolean received, String remain) {
        var is = new ItemStack(material);
        var meta = is.getItemMeta();
        if (modelData > 0) meta.setCustomModelData(this.modelData);

        List<String> l = Lists.newArrayList(this.lore);
        l.add("");
        if (can) l.add("§aClick để nhận");
        else if (received){
            l.add("§2Đã nhận");
        }
        else {
            l.add("§cKhông thể nhận");
            l.add("§cĐợi thêm: §o" + remain);
        }

        meta.setLore(l);
        meta.setDisplayName(name);
        is.setItemMeta(meta);
        return is;
    }

}
