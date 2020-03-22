package mc.rysty.heliosphereskyforge.island;

import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import mc.rysty.heliosphereskyforge.utils.SettingsManager;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		SettingsManager settings = SettingsManager.getInstance();
		FileConfiguration data = settings.getData();
		Player player = e.getPlayer();
		UUID pId = player.getUniqueId();
		String playerName = player.getName();

		if (data.getString("islands." + pId + ".owner") != playerName) {
			data.set("islands." + pId + ".owner", playerName);
			settings.saveData();
		}
	}
}
