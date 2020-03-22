package mc.rysty.heliosphereskyforge.modifications;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class CobbleGenerationModifier implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onFromTo(BlockFromToEvent event) {
		if (event.getBlock().getWorld().getName().equalsIgnoreCase("SkyForge")) {
			if (event.getBlock() != null) {
				Block block = event.getBlock();

				if (block.getType() != null) {
					Material material = block.getType();

					if (material == Material.WATER || material == Material.LEGACY_STATIONARY_WATER
							|| material == Material.LAVA || material == Material.LEGACY_STATIONARY_LAVA) {
						Block toBlock = event.getToBlock();

						if (toBlock.getType() == Material.AIR) {
							if (generatesCobble(material, toBlock)) {
								event.setCancelled(true);
								Random random = new Random();
								int percent = random.nextInt(100);

								if (percent >= 30 && percent < 101) {
									toBlock.setType(Material.COBBLESTONE);
								}
								if (percent >= 10 && percent < 30) {
									toBlock.setType(Material.COAL_ORE);
								}
								if (percent >= 0 && percent < 10) {
									toBlock.setType(Material.IRON_ORE);
								}
							}
						}
					}
				}
			}
		}
	}

	private final BlockFace[] faces = new BlockFace[] { BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH,
			BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

	@SuppressWarnings("deprecation")
	private boolean generatesCobble(Material material, Block block) {
		Material mirrorID1 = (material == Material.WATER || material == Material.LEGACY_STATIONARY_WATER ? Material.LAVA
				: Material.WATER);
		Material mirrorID2 = (material == Material.WATER || material == Material.LEGACY_STATIONARY_WATER
				? Material.LEGACY_STATIONARY_LAVA
				: Material.LEGACY_STATIONARY_WATER);

		for (BlockFace face : faces) {
			Block relative = block.getRelative(face, 1);

			if (relative.getType() == mirrorID1 || relative.getType() == mirrorID2) {
				return true;
			}
		}
		return false;
	}
}
