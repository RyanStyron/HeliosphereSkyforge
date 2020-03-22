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
import mc.rysty.heliosphereskyforge.utils.SettingsManager;

public class IslandSetup {

	public static void createIsland(UUID uuid) {
		SettingsManager settings = SettingsManager.getInstance();
		FileConfiguration data = settings.getData();
		Player player = Bukkit.getPlayer(uuid);
		String playerName = player.getName();
		UUID pId = player.getUniqueId();
		World skyforge = Bukkit.getWorld("SkyForge");

		Random random = new Random();
		int number = 12345678;
		int ranInt = random.nextInt(number);

		while (ranInt < 2500) {
			ranInt = random.nextInt(number);
		}
		int ranX = ranInt;
		int ranZ = ranInt;

		data.createSection("islands." + pId);
		data.set("islands." + pId + ".status", "current");
		data.set("islands." + pId + ".owner", playerName);
		data.set("islands." + pId + ".location.x", ranX);
		data.set("islands." + pId + ".location.y", 65);
		data.set("islands." + pId + ".location.z", ranZ);
		settings.saveData();

		double x = data.getDouble("islands." + pId + ".location.x");
		double y = data.getDouble("islands." + pId + ".location.y");
		double z = data.getDouble("islands." + pId + ".location.z");
		Location islandSpawn = new Location(skyforge, x, y, z);

		loadSchematic(islandSpawn);
	}

	public static void loadSchematic(Location location) {
		HelioSphereSkyForge plugin = HelioSphereSkyForge.getInstance();
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

	public static void deleteIsland(Player player, Location location) {
		HelioSphereSkyForge plugin = HelioSphereSkyForge.getInstance();
		File file = new File(plugin.getDataFolder() + File.separator + "SkyForgeIslandDeleteAir.schem");
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
