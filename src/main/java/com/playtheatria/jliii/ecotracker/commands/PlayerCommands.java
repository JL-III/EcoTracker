package com.playtheatria.jliii.ecotracker.commands;

import com.playtheatria.jliii.ecotracker.managers.EconomyTrackerManager;
import com.playtheatria.jliii.generalutils.GeneralUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Properties;

public class PlayerCommands implements CommandExecutor {

    private final Plugin plugin;
    private final EconomyTrackerManager economyTrackerManager;

    public PlayerCommands(Plugin plugin, EconomyTrackerManager economyTrackerManager) {
        this.plugin = plugin;
        this.economyTrackerManager = economyTrackerManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            return false;
        }

        if (!player.hasPermission("ecotracker.use")) {
            return true;
        }

        if (args[0].equalsIgnoreCase("version")) {
            player.sendMessage(plugin.getName() + plugin.getDescription().getVersion());
            player.sendMessage(GeneralUtils.getVersion());
        }

        if (args[0].equalsIgnoreCase("clear")) {
            economyTrackerManager.cleanUpProfilers();
            player.sendMessage(ChatColor.YELLOW + "Economy profilers cleaned up, check logs for info.");
            return true;
        }

        if (args[0].equalsIgnoreCase("start")) {
            if (args.length == 2) {
                try {
                    if (Integer.parseInt(args[1]) < 1) {
                        player.sendMessage("Invalid number, you must enter a number higher than 0.");
                        return true;
                    }
                    economyTrackerManager.runProfiler(player.getUniqueId(), Integer.parseInt(args[1]));
                } catch (NumberFormatException e) {
                    player.sendMessage("Invalid number.");
                }
                player.sendMessage(ChatColor.YELLOW + "Economy Profiler started.");
                return true;
            }
        }

        return false;
    }
}
