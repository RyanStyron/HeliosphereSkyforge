package mc.rysty.heliosphereskyforge.setup;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import mc.rysty.heliosphereskyforge.HelioSphereSkyForge;

public class SkyForgeSpawn implements Listener {

	public SkyForgeSpawn(HelioSphereSkyForge plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		Location location = entity.getLocation();
		World world = location.getWorld();

		if (world.equals(Bukkit.getWorld("Skyforge")))
			if (location.distanceSquared(world.getSpawnLocation()) <= 3000)
				event.setCancelled(true);
	}

	@EventHandler
	public void onBlockInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Location location = player.getLocation();
		World world = location.getWorld();

		if (event.getClickedBlock() == null)
			return;
		Block block = event.getClickedBlock();

		if (block.getType() == null)
			return;

		if (world.equals(Bukkit.getWorld("Skyforge")))
			if (location.distanceSquared(world.getSpawnLocation()) <= 3000)
				if (player.getGameMode() != GameMode.CREATIVE)
					event.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		skyforgeSpawnBlockModifyCheck(event, event.getPlayer());
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		skyforgeSpawnBlockModifyCheck(event, event.getPlayer());
	}

	@EventHandler
	public void onBarrierBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();

		if (world.equals(Bukkit.getWorld("Skyforge"))) {
			if (event.getBlock().getType() == Material.BARRIER)
				if (!player.hasPermission("hs.barrierbreak"))
					event.setCancelled(true);
		}
	}

	private void skyforgeSpawnBlockModifyCheck(Event event, Player player) {
		String eventName = event.getEventName();
		Location location = player.getLocation();
		World world = location.getWorld();

		if (world.equals(Bukkit.getWorld("Skyforge")))
			if (location.distanceSquared(world.getSpawnLocation()) <= 3000)
				if (!player.getGameMode().equals(GameMode.CREATIVE) || !player.hasPermission("hs.skyforge.spawnbuild"))
					if (eventName.equalsIgnoreCase("BlockBreakEvent"))
						((BlockBreakEvent) event).setCancelled(true);
					else if (eventName.equalsIgnoreCase("BlockPlaceEvent"))
						((BlockPlaceEvent) event).setCancelled(true);
	}
}