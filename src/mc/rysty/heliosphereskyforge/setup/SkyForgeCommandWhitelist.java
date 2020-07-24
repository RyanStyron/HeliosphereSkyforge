package mc.rysty.heliosphereskyforge.setup;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import mc.rysty.heliosphereskyforge.HelioSphereSkyForge;
import mc.rysty.heliosphereskyforge.utils.MessageUtils;

public class SkyForgeCommandWhitelist implements Listener {

	public SkyForgeCommandWhitelist(HelioSphereSkyForge plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		String message = event.getMessage();
		Player player = event.getPlayer();
		World world = player.getWorld();

		if (!world.equals(Bukkit.getWorld("Skyforge"))) {
			if (message.startsWith("/island") || message.startsWith("/is")) {
				event.setCancelled(true);
				MessageUtils.configStringMessage(player, "world_command_error", "<world>", world.getName());
			}
		}
	}
}
