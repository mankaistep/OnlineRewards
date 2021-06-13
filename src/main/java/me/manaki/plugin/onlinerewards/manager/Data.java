package me.manaki.plugin.onlinerewards.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.manaki.plugin.onlinerewards.OnlineRewards;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class Data {

    private long last;
    private Map<String, List<String>> received;

    private Data(long last, Map<String, List<String>> received) {
        this.last = last;
        this.received = received;
    }

    public List<String> getReceived(Player player) {
        return this.received.getOrDefault(player.getName(), Lists.newArrayList());
    }

    public void setLast(long last) {
        this.last = last;
    }

    public void receive(Player player, String reward) {
        List<String> l = this.received.getOrDefault(player.getName(), Lists.newArrayList());
        l.add(reward);
        this.received.put(player.getName(), l);
    }

    public void setReceived(Map<String, List<String>> received) {
        this.received = received;
    }

    public void checkAndSave() {
        var calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.last);
        int lastDay = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTimeInMillis(System.currentTimeMillis());
        int currentDay = calendar.get(Calendar.DAY_OF_YEAR);
        if (lastDay != currentDay) {
            this.received.clear();
            this.last = System.currentTimeMillis();
            Managers.newDay();
        }

        save();
    }

    public void save() {
        File file = new File(OnlineRewards.get().getDataFolder(), "data.yml");
        var config = YamlConfiguration.loadConfiguration(file);
        for (String s : config.getConfigurationSection("").getKeys(false)) {
            config.set(s, null);
        }
        config.set("last", this.last);
        for (String name : received.keySet()) {
            config.set("received." + name, received.get(name));
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Data load() {
        File file = new File(OnlineRewards.get().getDataFolder(), "data.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        var config = YamlConfiguration.loadConfiguration(file);

        long last = config.getLong("last");
        Map<String, List<String>> received = Maps.newHashMap();
        if (config.contains("received")) {
            for (String name : config.getConfigurationSection("received").getKeys(false)) {
                received.put(name, config.getStringList("received." + name));
            }
        }

        return new Data(last, received);
    }

}
