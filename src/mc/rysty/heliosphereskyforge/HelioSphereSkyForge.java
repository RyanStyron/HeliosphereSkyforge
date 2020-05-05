package mc.rysty.heliosphereskyforge;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import mc.rysty.heliosphereskyforge.commands.IslandCommand;
import mc.rysty.heliosphereskyforge.island.NoBlockModify;
import mc.rysty.heliosphereskyforge.island.PlayerJoin;
import mc.rysty.heliosphereskyforge.modifications.CobbleGenerationModifier;
import mc.rysty.heliosphereskyforge.setup.SkyForgeCommandWhitelist;
import mc.rysty.heliosphereskyforge.setup.SkyForgeSpawnNoBuild;
import mc.rysty.heliosphereskyforge.utils.SettingsManager;

public class HelioSphereSkyForge extends JavaPlugin {

	public static HelioSphereSkyForge plugin;

	public static HelioSphereSkyForge getInstance() {
		return plugin;
	}

	PluginManager pm = Bukkit.getPluginManager();

	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		SettingsManager.getInstance().setup(this);

		new IslandCommand(this);
		//pm.registerEvents(new SkyForgeSpawnAdjust(), this);
		pm.registerEvents(new SkyForgeCommandWhitelist(), this);
		pm.registerEvents(new SkyForgeSpawnNoBuild(), this);
		pm.registerEvents(new PlayerJoin(), this);
		pm.registerEvents(new NoBlockModify(), this);
		pm.registerEvents(new CobbleGenerationModifier(), this);

		System.out.println("HS-SkyForge enabled");
	}

	public void onDisable() {
		SettingsManager.getInstance().saveData();
		System.out.println("HS-SkyForge disabled");
	}
}
