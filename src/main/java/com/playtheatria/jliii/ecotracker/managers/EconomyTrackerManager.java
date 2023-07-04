package com.playtheatria.jliii.ecotracker.managers;

import com.playtheatria.jliii.ecotracker.profiler.BalanceProfiler;
import com.playtheatria.jliii.generalutils.utils.CustomLogger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EconomyTrackerManager {

    private final Plugin plugin;

    private final Economy economy;

    private CustomLogger customLogger;

    private final List<BalanceProfiler> balanceProfilers;

    public EconomyTrackerManager(Plugin plugin, CustomLogger customLogger, Economy economy) {
        this.plugin = plugin;
        this.economy = economy;
        this.customLogger = customLogger;
        this.balanceProfilers = new ArrayList<>();
    }

    public void createBalanceProfiler(UUID uuid) {
        customLogger.sendLog("Creating balance profiler for " + Bukkit.getOfflinePlayer(uuid).getName());
        balanceProfilers.add(new BalanceProfiler(plugin, customLogger, economy, uuid));
    }

    public void createIfNotExists(UUID uuid) {
        if (balanceProfilers.stream().noneMatch(balanceProfiler -> balanceProfiler.getUuid().equals(uuid))) {
            createBalanceProfiler(uuid);
        }
    }

    public void runProfiler(UUID uuid, int pollInterval) {
        createIfNotExists(uuid);
        balanceProfilers.stream().filter(balanceProfiler -> balanceProfiler.getUuid().equals(uuid)).forEach(balanceProfiler -> balanceProfiler.runTask(pollInterval));
    }

    public void cleanUpProfilers() {
        balanceProfilers.forEach(BalanceProfiler::cancelTask);
    }
}
