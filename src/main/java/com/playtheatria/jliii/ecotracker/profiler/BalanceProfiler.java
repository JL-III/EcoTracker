package com.playtheatria.jliii.ecotracker.profiler;

import com.playtheatria.jliii.ecotracker.tasks.EconomyTrackTask;
import com.playtheatria.jliii.ecotracker.utils.GeneralUtils;
import com.playtheatria.jliii.generalutils.utils.CustomLogger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class BalanceProfiler {

    private final Plugin plugin;

    private CustomLogger customLogger;

    private final Economy economy;

    private final UUID uuid;

    private double balance;

    public BalanceProfiler(Plugin plugin, CustomLogger customLogger, Economy economy, UUID uuid) {
        this.plugin = plugin;
        this.customLogger = customLogger;
        this.economy = economy;
        this.uuid = uuid;
        setBalance(economy.getBalance(Bukkit.getOfflinePlayer(uuid)));
    }

    public void runTask(int pollInterval) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }
        double balance = economy.getBalance(player);
        player.sendMessage(ChatColor.DARK_GREEN + "Starting balance: " + ChatColor.GREEN + GeneralUtils.formatDouble(balance));
        player.sendMessage(ChatColor.DARK_GREEN + "Starting profiler for " + ChatColor.YELLOW + pollInterval + ChatColor.DARK_GREEN + " minutes");
        setBalance(balance);
        new EconomyTrackTask(economy, this).runTaskLater(plugin,  20L * pollInterval * 60);
    }

    public void cancelTask() {
        try {
            Bukkit.getScheduler().getPendingTasks().forEach(x -> {
                if (x.getOwner().getName().equals(plugin.getName())) {
                    x.cancel();
                    customLogger.sendLog("Cancelled task " + x.getTaskId() + " for " + Bukkit.getOfflinePlayer(uuid).getName());
                }
            });
        } catch (IllegalStateException ex) {
            customLogger.sendLog("Tried to cancel a task that was already cancelled or wasn't scheduled!");
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
