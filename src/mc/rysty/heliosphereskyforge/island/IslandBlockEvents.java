package mc.rysty.heliosphereskyforge.island;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import mc.rysty.heliosphereskyforge.HelioSphereSkyForge;
import mc.rysty.heliosphereskyforge.utils.IslandsFileManager;
import mc.rysty.heliosphereskyforge.utils.MessageUtils;

public class IslandBlockEvents implements Listener {

	private IslandsFileManager islandsFileManager = HelioSphereSkyForge.islandsFileManager;
	private FileConfiguration islandsFile = islandsFileManager.getData();

	public IslandBlockEvents(HelioSphereSkyForge plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		noBlockModify(event.getPlayer(), event);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		noBlockModify(event.getPlayer(), event);
	}

	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		noBlockModify(event.getPlayer(), event);
	}

	@EventHandler
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		noBlockModify(event.getPlayer(), event);
	}

	private void noBlockModify(Player player, Event event) {
		UUID playerId = player.getUniqueId();
		Location location = player.getLocation();
		World world = location.getWorld();

		if (world.equals(Bukkit.getWorld("Skyforge"))) {
			if (islandsFile.getString("islands." + playerId + ".status") != null) {
				double islandXCoord = islandsFile.getDouble("islands." + playerId + ".location.x");
				double islandYCoord = islandsFile.getDouble("islands." + playerId + ".location.y");
				double islandZCoord = islandsFile.getDouble("islands." + playerId + ".location.z");
				Location islandLocation = new Location(world, islandXCoord, islandYCoord, islandZCoord);

				if (location.distanceSquared(islandLocation) >= 10000) {
					if (location.distanceSquared(world.getSpawnLocation()) >= 2500) {
						if (!player.hasPermission("hs.skyforge.modifyIslandOther")) {
							MessageUtils.configStringMessage(player, "no_block_modify");
							cancelEvent(event);
						}
					}
				}
			} else if (location.distanceSquared(world.getSpawnLocation()) >= 2500) {
				if (!player.hasPermission("hs.skyforge.modifyIslandOther")) {
					MessageUtils.configStringMessage(player, "no_block_modify");
					cancelEvent(event);
				}
			}
		}
	}

	private void cancelEvent(Event event) {
		String eventName = event.getEventName();

		if (eventName.equalsIgnoreCase("BlockBreakEvent"))
			((BlockBreakEvent) event).setCancelled(true);
		else if (eventName.equalsIgnoreCase("BlockPlaceEvent"))
			((BlockPlaceEvent) event).setCancelled(true);
		else if (eventName.equalsIgnoreCase("PlayerBucketEmptyEvent"))
			((PlayerBucketEmptyEvent) event).setCancelled(true);
		else if (eventName.equalsIgnoreCase("PlayerBucketFillEvent"))
			((PlayerBucketFillEvent) event).setCancelled(true);
	}
}
