package mc.rysty.heliosphereskyforge.island;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;

import mc.rysty.heliosphereskyforge.HelioSphereSkyForge;
import mc.rysty.heliosphereskyforge.utils.IslandsFileManager;

public class IslandSetup {

	private static HelioSphereSkyForge plugin = HelioSphereSkyForge.getInstance();
	private static IslandsFileManager islandsFileManager = HelioSphereSkyForge.islandsFileManager;
	private static FileConfiguration islandsFile = islandsFileManager.getData();

	public static void createIsland(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		String playerName = player.getName();
		UUID playerId = player.getUniqueId();
		World skyforge = Bukkit.getWorld("Skyforge");

		Random random = new Random();
		int randomInteger = random.nextInt(12345678);

		while (randomInteger < 30000)
			randomInteger = random.nextInt(12345678);

		islandsFile.createSection("islands." + playerId);
		islandsFile.set("islands." + playerId + ".status", "current");
		islandsFile.set("islands." + playerId + ".owner", playerName);
		islandsFile.set("islands." + playerId + ".location.x", randomInteger);
		islandsFile.set("islands." + playerId + ".location.y", 65);
		islandsFile.set("islands." + playerId + ".location.z", randomInteger);
		islandsFileManager.saveData();

		double x = islandsFile.getDouble("islands." + playerId + ".location.x");
		double y = islandsFile.getDouble("islands." + playerId + ".location.y");
		double z = islandsFile.getDouble("islands." + playerId + ".location.z");
		Location islandSpawn = new Location(skyforge, x, y, z);

		loadNewIslandSchematic(islandSpawn);
	}

	public static void loadNewIslandSchematic(Location location) {
		File file = new File(plugin.getDataFolder() + File.separator + "SkyForgeIsland.schem");
		ClipboardFormat format = ClipboardFormats.findByFile(file);

		try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
			Clipboard clipboard = reader.read();
			com.sk89q.worldedit.world.World world = new BukkitWorld(location.getWorld());

			try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
				Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
						.to(BlockVector3.at(location.getX(), location.getY(), location.getZ())).ignoreAirBlocks(false)
						.build();
				Operations.complete(operation);
			} catch (WorldEditException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
