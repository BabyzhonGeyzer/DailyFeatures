package ru.elementcraft.dailyfeatures.tools;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {
    private static Economy econ = null;

    /**
     * Setup economy.
     * @return true if economy is properly setup - false if not.
     */
    public static boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        econ = rsp.getProvider();
        return econ != null;
    }

    /**
     * Get Vault economy.
     */
    public static Economy getEconomy() {
        return econ;
    }

}
