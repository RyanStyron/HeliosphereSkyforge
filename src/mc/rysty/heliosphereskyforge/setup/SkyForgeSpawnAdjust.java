package mc.rysty.heliosphereskyforge.setup;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import mc.rysty.heliosphereskyforge.HelioSphereSkyForge;
import mc.rysty.heliosphereskyforge.utils.Utils;

public class SkyForgeSpawnAdjust implements Listener {

	private HelioSphereSkyForge plugin = HelioSphereSkyForge.getInstance();
	private FileConfiguration config = plugin.getConfig();
	private boolean skyforge;

	@EventHandler
	public void onWorldChanged(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		skyforge = p.getWorld().getName().equalsIgnoreCase("SkyForge");

		if (skyforge) {
			Location skyforgeActualSpawn = new Location(Bukkit.getWorld("SkyForge"), 0.5, 65.0, 0.5);

			p.teleport(skyforgeActualSpawn);
		}
	}

	@EventHandler
	public void onSpawnCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();

		if (e.getMessage().equalsIgnoreCase("/spawn")) {
			skyforge = p.getWorld().getName().equalsIgnoreCase("SkyForge");

			if (skyforge) {
				e.setCancelled(true);
				Location skyforgeActualSpawn = new Location(Bukkit.getWorld("SkyForge"), 0.5, 65.0, 0.5);

				p.teleport(skyforgeActualSpawn);
				p.sendMessage(
						Utils.chat(config.getString("teleported_spawn_message").replaceAll("<world>", "SkyForge")));
			}
		}
	}
}
