package mc.rysty.heliosphereskyforge.setup;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import mc.rysty.heliosphereskyforge.HelioSphereSkyForge;
import mc.rysty.heliosphereskyforge.utils.Utils;

public class SkyForgeCommandWhitelist implements Listener {

	private HelioSphereSkyForge plugin = HelioSphereSkyForge.getInstance();
	private FileConfiguration config = plugin.getConfig();

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		boolean skyforge = p.getWorld().getName().equalsIgnoreCase("SkyForge");

		if (!skyforge) {
			if (e.getMessage().startsWith("/island") || e.getMessage().startsWith("/is")) {
				e.setCancelled(true);
				p.sendMessage(Utils
						.chat(config.getString("world_command_error").replaceAll("<world>", p.getWorld().getName())));
			}
		}
	}
}
