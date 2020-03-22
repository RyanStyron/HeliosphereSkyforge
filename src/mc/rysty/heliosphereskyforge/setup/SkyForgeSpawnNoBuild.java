package mc.rysty.heliosphereskyforge.setup;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import mc.rysty.heliosphereskyforge.HelioSphereSkyForge;
import mc.rysty.heliosphereskyforge.utils.Utils;

public class SkyForgeSpawnNoBuild implements Listener {

	private HelioSphereSkyForge plugin = HelioSphereSkyForge.getInstance();
	private FileConfiguration config = plugin.getConfig();
	private String noPermMessage = Utils.chat(config.getString("no_perm_message"));
	private String noBlockModifyMessage = Utils.chat(config.getString("Spawn.no_block_modify"));
	private boolean skyforge;

	@EventHandler
	public void onBlockInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		if (e.getClickedBlock() == null) {
			return;
		}
		Block block = e.getClickedBlock();

		if (block.getType() == null) {
			return;
		}
		skyforge = p.getWorld().getName().equalsIgnoreCase("SkyForge");

		if (skyforge) {
			if (p.getLocation().distanceSquared(Bukkit.getWorld("SkyForge").getSpawnLocation()) <= 2500) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		boolean spawnBuild = p.hasPermission("hs.spawnbuild");
		boolean creative = p.getGameMode().equals(GameMode.CREATIVE);
		skyforge = p.getWorld().getName().equalsIgnoreCase("SkyForge");

		if (skyforge) {
			if (p.getLocation().distanceSquared(Bukkit.getWorld("SkyForge").getSpawnLocation()) <= 2500) {
				if (!creative || !spawnBuild) {
					e.setCancelled(true);
					if (!spawnBuild) {
						return;
					} else {
						p.sendMessage(noBlockModifyMessage);
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		boolean spawnBuild = p.hasPermission("hs.spawnbuild");
		boolean creative = p.getGameMode().equals(GameMode.CREATIVE);
		skyforge = p.getWorld().getName().equalsIgnoreCase("SkyForge");

		if (skyforge) {
			if (p.getLocation().distanceSquared(Bukkit.getWorld("SkyForge").getSpawnLocation()) <= 2500) {
				if (!creative || !spawnBuild) {
					e.setCancelled(true);
					if (!spawnBuild) {
						return;
					} else {
						p.sendMessage(noBlockModifyMessage);
					}
				}
			}
		}
	}

	@EventHandler
	public void onBarrierBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		skyforge = p.getWorld().getName().equalsIgnoreCase("SkyForge");

		if (skyforge) {
			if (e.getBlock().getType() == Material.BARRIER) {
				if (!p.hasPermission("hs.barrierbreak")) {
					e.setCancelled(true);
					p.sendMessage(noPermMessage);
				}
			}
		}
	}
}