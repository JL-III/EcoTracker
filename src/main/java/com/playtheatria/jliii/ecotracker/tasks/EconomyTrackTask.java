package com.playtheatria.jliii.ecotracker.tasks;

import com.playtheatria.jliii.ecotracker.profiler.BalanceProfiler;
import com.playtheatria.jliii.ecotracker.utils.GeneralUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EconomyTrackTask extends BukkitRunnable {

    private final Economy economy;

    private final BalanceProfiler balanceProfiler;

    public EconomyTrackTask(Economy economy, BalanceProfiler balanceProfiler) {
        this.economy = economy;
        this.balanceProfiler = balanceProfiler;
    }

    @Override
    public void run() {
        Player player = Bukkit.getPlayer(balanceProfiler.getUuid());
        if (player == null) {
            return;
        }
        Double newBalance = economy.getBalance(player);
        if (newBalance.equals(balanceProfiler.getBalance())) {
            player.sendMessage(ChatColor.DARK_GREEN + "No changes detected in balance.");
            return;
        }
        String diff = GeneralUtils.formatDouble(newBalance - balanceProfiler.getBalance());
        player.sendMessage(ChatColor.DARK_GREEN + "Balance: " + ChatColor.GREEN + GeneralUtils.formatDouble(balanceProfiler.getBalance()) + " -> " + ChatColor.YELLOW + GeneralUtils.formatDouble(newBalance));
        player.sendMessage(ChatColor.DARK_GREEN + "Diff: " + ChatColor.GREEN + diff);
        balanceProfiler.setBalance(newBalance);
    }

}
