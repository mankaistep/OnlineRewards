package me.manaki.plugin.onlinerewards;

import me.manaki.plugin.onlinerewards.command.RPlayerCommand;
import me.manaki.plugin.onlinerewards.listener.RewardListener;
import me.manaki.plugin.onlinerewards.manager.Managers;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class OnlineRewards extends JavaPlugin {

    @Override
    public void onEnable() {
        this.reloadConfig();
        this.registerCommand();
        this.registerListener();
        this.registerTask();

        for (Player player : Bukkit.getOnlinePlayers()) {
            Managers.join(player);
        }
    }

    @Override
    public void onDisable() {
        Managers.getData().checkAndSave();
    }

    @Override
    public void reloadConfig() {
        this.saveDefaultConfig();
        var config = YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), "config.yml"));
        Managers.reload(config);
    }

    public void registerCommand() {
        this.getCommand("onlinereward").setExecutor(new RPlayerCommand());
    }

    public void registerListener() {
        Bukkit.getPluginManager().registerEvents(new RewardListener(), this);
    }

    public void registerTask() {
        Tasks.sync(() -> {
            Managers.getData().checkAndSave();
        }, 0, 200);
    }

    public static OnlineRewards get() {
        return JavaPlugin.getPlugin(OnlineRewards.class);
    }

}
