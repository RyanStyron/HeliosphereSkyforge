package mc.rysty.heliosphereskyforge.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class IslandsFileManager {

	public static IslandsFileManager instance = new IslandsFileManager();
	private static Logger logger = Bukkit.getServer().getLogger();

	public static IslandsFileManager getInstance() {
		return instance;
	}

	FileConfiguration data;
	File dataFile;

	public void setup(Plugin plugin) {
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();

		dataFile = new File(plugin.getDataFolder(), "islands.yml");

		if (!dataFile.exists()) {
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				logger.severe(ChatColor.RED + "Could not create islands.yml!");
			}
		}

		data = YamlConfiguration.loadConfiguration(dataFile);
	}

	public FileConfiguration getData() {
		return data;
	}

	public void saveData() {
		try {
			data.save(dataFile);
		} catch (IOException e) {
			logger.severe(ChatColor.RED + "Could not save islands.yml!");
		}
	}

	public void reloadData() {
		data = YamlConfiguration.loadConfiguration(dataFile);
	}
}