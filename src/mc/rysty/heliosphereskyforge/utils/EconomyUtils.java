package mc.rysty.heliosphereskyforge.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

import mc.rysty.heliosphereskyforge.HelioSphereSkyForge;
import net.milkbowl.vault.economy.Economy;

public class EconomyUtils {

    private static HelioSphereSkyForge plugin = HelioSphereSkyForge.getInstance();
    private static ServicesManager servicesManager = plugin.getServer().getServicesManager();

    public static Economy economy;

    public static boolean setupEconomy() {
        RegisteredServiceProvider<Economy> registeredServiceProvider = servicesManager.getRegistration(Economy.class);

        if (registeredServiceProvider != null)
            economy = registeredServiceProvider.getProvider();
        return (economy != null);
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static double getPlayerBalance(Player player) {
        return getEconomy().getBalance(player);
    }
}