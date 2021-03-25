package me.manaki.plugin.onlinerewards.reward;

import java.util.List;

public class Reward {

    private String id;
    private RewardIcon icon;
    private List<String> commands;

    public Reward(String id, RewardIcon icon, List<String> commands) {
        this.id = id;
        this.icon = icon;
        this.commands = commands;
    }

    public String getID() {
        return id;
    }

    public RewardIcon getIcon() {
        return icon;
    }

    public List<String> getCommands() {
        return commands;
    }
}
