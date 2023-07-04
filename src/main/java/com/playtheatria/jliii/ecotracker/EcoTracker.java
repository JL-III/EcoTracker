package com.playtheatria.jliii.ecotracker;

import com.playtheatria.jliii.ecotracker.commands.PlayerCommands;
import com.playtheatria.jliii.ecotracker.managers.EconomyTrackerManager;
import com.playtheatria.jliii.generalutils.utils.CustomLogger;
import net.kyori.adventure.text.format.NamedTextColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class EcoTracker extends JavaPlugin {

    private final CustomLogger customLogger = new CustomLogger(getName(), NamedTextColor.DARK_GREEN, NamedTextColor.GREEN);
    private Economy economy = null;
    private EconomyTrackerManager economyTrackerManager;

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            customLogger.sendLog(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        economyTrackerManager = new EconomyTrackerManager(this, customLogger, economy);
        Objects.requireNonNull(Bukkit.getPluginCommand("ecotracker")).setExecutor(new PlayerCommands(this, economyTrackerManager));
    }

    @Override
    public void onDisable() {
        customLogger.sendLog(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
        economyTrackerManager.cleanUpProfilers();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

}
