package me.manaki.plugin.onlinerewards.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.manaki.plugin.onlinerewards.reward.Reward;
import me.manaki.plugin.onlinerewards.reward.RewardIcon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Managers {

    private static Data data;

    private static final Map<String, Reward> rewards = Maps.newHashMap();
    private static final Map<Long, String> onlinerewards = Maps.newHashMap();

    private static final Map<String, Long> joinTimes = Maps.newHashMap();

    public static Map<Long, String> getAllRewards() {
        return Maps.newHashMap(onlinerewards);
    }

    public static void reload(FileConfiguration config) {
        // Rewards
        rewards.clear();
        for (String id : config.getConfigurationSection("reward").getKeys(false)) {
            // Icon
            var path = "reward." + id + ".icon";
            Material m = Material.valueOf(config.getString(path + ".material").toUpperCase());
            int modelData = config.getInt(path + ".model-data");
            String name = config.getString(path + ".name").replace("&", "§");
            List<String> lore = config.getStringList(path + ".lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
            var icon = new RewardIcon(m, modelData, name, lore);

            // Command
            List<String> commands = config.getStringList("reward." + id + ".commands");

            Reward r = new Reward(id, icon, commands);
            rewards.put(id, r);
        }

        // Online Rewards
        onlinerewards.clear();
        for (String ts : config.getConfigurationSection("online-rewards").getKeys(false)) {
            onlinerewards.put(Long.parseLong(ts) * 1000, config.getString("online-rewards." + ts));
        }

        // Data
        data = Data.load();

        // Clear
        joinTimes.clear();
    }

    public static Reward get(String id) {
        return rewards.getOrDefault(id, null);
    }

    public static void join(Player player) {
        joinTimes.put(player.getName(), System.currentTimeMillis());
    }

    public static void quit(Player player) {
        joinTimes.remove(player.getName());
    }

    public static long getOnline(Player player) {
        return System.currentTimeMillis() - joinTimes.getOrDefault(player.getName(), System.currentTimeMillis());
    }

    public static List<String> getAvailables(Player player) {
        long onl = getOnline(player);
        List<String> l = Lists.newArrayList();
        List<String> received = data.getReceived(player);
        for (Long time : onlinerewards.keySet()) {
            if (onl >= time && !received.contains(onlinerewards.get(time))) l.add(onlinerewards.get(time));
        }
        return l;
    }

    public static Data getData() {
        return data;
    }

    public static void giveAndCheck(Player player, String id) {
        Reward r = get(id);
        for (String cmd : r.getCommands()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
        }
        getData().receive(player, id);
    }

    public static long getRemainSeconds(Player player, String id) {
        long time = -1;
        for (Long t : onlinerewards.keySet()) {
            if (onlinerewards.get(t).equalsIgnoreCase(id)) {
                time = t;
                break;
            }
        }
        return (time - getOnline(player)) / 1000;
    }

    public static String format(long seconds) {
        long h = seconds / 3600;
        long m = (seconds - h * 3600) / 60;
        long s = seconds - (h * 3600 + m * 60);

        return h + "h " + m + "m " + s + "s";
    }

}
