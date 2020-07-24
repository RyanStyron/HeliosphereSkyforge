package mc.rysty.heliosphereskyforge.setup;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import mc.rysty.heliosphereskyforge.HelioSphereSkyForge;
import mc.rysty.heliosphereskyforge.utils.IslandsFileManager;

public class UpdateIslandsYAML implements Listener {

	private IslandsFileManager islandsFileManager = HelioSphereSkyForge.islandsFileManager;
	private FileConfiguration islandsFile = islandsFileManager.getData();

	public UpdateIslandsYAML(HelioSphereSkyForge plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		UUID playerId = player.getUniqueId();
		String playerName = player.getName();

		if (player.getWorld().equals(Bukkit.getWorld("Skyforge"))) {
			String islandOwnerString = islandsFile.getString("islands." + playerId + ".owner");

			if (islandOwnerString != null && islandOwnerString != playerName) {
				islandsFile.set("islands." + playerId + ".owner", playerName);
				islandsFileManager.saveData();
			}
		}
	}
}
