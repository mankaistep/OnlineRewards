package me.manaki.plugin.onlinerewards.command;

import me.manaki.plugin.onlinerewards.gui.RewardGUI;
import me.manaki.plugin.onlinerewards.manager.Managers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RPlayerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player player = (Player) sender;

        if (args.length == 0) {
            RewardGUI.open(player);
            return false;
        }

        String id = args[0];
        if (!Managers.getAvailables(player).contains(id)) {
            player.sendMessage("§cBạn không thể nhận phần quà này!");
            return false;
        }

        Managers.giveAndCheck(player, id);

        return false;
    }

}
