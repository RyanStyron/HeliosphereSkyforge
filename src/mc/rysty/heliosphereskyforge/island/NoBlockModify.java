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
import mc.rysty.heliosphereskyforge.utils.SettingsManager;
import mc.rysty.heliosphereskyforge.utils.Utils;

public class NoBlockModify implements Listener {

	private HelioSphereSkyForge plugin = HelioSphereSkyForge.getInstance();
	private FileConfiguration config = plugin.getConfig();
	private SettingsManager settings = SettingsManager.getInstance();
	private FileConfiguration data = settings.getData();
	private String no_block_modify = Utils.chat(config.getString("no_block_modify"));
	private String modify_other_island = Utils.chat(config.getString("modify_other_island"));
	private String blockModify = "hs.skyforge.blockmodify";
	private World skyforgeWorld = Bukkit.getWorld("Skyforge");
	private double islandXCoord;
	private double islandYCoord;
	private double islandZCoord;

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();

		noModify(player, event);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();

		noModify(player, event);
	}

	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		Player player = event.getPlayer();

		noModify(player, event);
	}

	@EventHandler
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		Player player = event.getPlayer();

		noModify(player, event);
	}

	private void noModify(Player player, Event event) {
		UUID playerId = player.getUniqueId();
		islandXCoord = data.getDouble("islands." + playerId + ".location.x");
		islandYCoord = data.getDouble("islands." + playerId + ".location.y");
		islandZCoord = data.getDouble("islands." + playerId + ".location.z");
		Location islandLocation = new Location(skyforgeWorld, islandXCoord, islandYCoord, islandZCoord);
		World playerWorld = player.getWorld();
		boolean noBlockModifyPerm = !player.hasPermission(blockModify);
		Location playerLocation = player.getLocation();

		if (playerWorld == skyforgeWorld) {
			if (data.getString("islands." + playerId + ".status") != null) {
				if (playerLocation.distanceSquared(islandLocation) >= 10000
						&& playerLocation.distanceSquared(skyforgeWorld.getSpawnLocation()) >= 2500) {
					if (noBlockModifyPerm) {
						player.sendMessage(no_block_modify);
						((BlockBreakEvent) event).setCancelled(true);
					} else {
						player.sendMessage(modify_other_island);
					}
				}
			} else {
				if (playerLocation.distanceSquared(skyforgeWorld.getSpawnLocation()) >= 2500) {
					if (noBlockModifyPerm) {
						player.sendMessage(no_block_modify);

						String eventName = event.getEventName();

						if (eventName.equalsIgnoreCase("BlockBreakEvent"))
							((BlockBreakEvent) event).setCancelled(true);
						else if (eventName.equalsIgnoreCase("BlockPlaceEvent"))
							((BlockPlaceEvent) event).setCancelled(true);
						else if (eventName.equalsIgnoreCase("PlayerBucketEmptyEvent"))
							((PlayerBucketEmptyEvent) event).setCancelled(true);
						else if (eventName.equalsIgnoreCase("PlayerBucketFillEvent"))
							((PlayerBucketFillEvent) event).setCancelled(true);
					} else {
						player.sendMessage(modify_other_island);
					}
				}
			}
		}
	}
}
