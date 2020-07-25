package mc.rysty.heliosphereskyforge;

import org.bukkit.plugin.java.JavaPlugin;

import mc.rysty.heliosphereskyforge.commands.IslandCommand;
import mc.rysty.heliosphereskyforge.island.IslandBlockEvents;
import mc.rysty.heliosphereskyforge.modifications.CobbleGenerationModifier;
import mc.rysty.heliosphereskyforge.setup.SkyForgeCommandWhitelist;
import mc.rysty.heliosphereskyforge.setup.SkyForgeScoreboard;
import mc.rysty.heliosphereskyforge.setup.SkyForgeSpawn;
import mc.rysty.heliosphereskyforge.setup.UpdateIslandsYAML;
import mc.rysty.heliosphereskyforge.utils.IslandsFileManager;

public class HelioSphereSkyForge extends JavaPlugin {

	public static HelioSphereSkyForge plugin;

	public static HelioSphereSkyForge getInstance() {
		return plugin;
	}

	public static IslandsFileManager islandsFileManager = IslandsFileManager.getInstance();

	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		islandsFileManager.setup(this);

		/* Commands. */
		new IslandCommand(this);

		/* Listeners. */
		new SkyForgeCommandWhitelist(this);
		new SkyForgeScoreboard(this);
		new UpdateIslandsYAML(this);
		new SkyForgeSpawn(this);
		new IslandBlockEvents(this);
		new CobbleGenerationModifier(this);

		System.out.println("HS-SkyForge enabled");
	}

	public void onDisable() {
		System.out.println("HS-SkyForge disabled");
	}
}
