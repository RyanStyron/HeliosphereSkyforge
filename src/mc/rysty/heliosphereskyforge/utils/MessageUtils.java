package mc.rysty.heliosphereskyforge.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereskyforge.HelioSphereSkyForge;

public class MessageUtils {

	private static FileConfiguration config = HelioSphereSkyForge.getInstance().getConfig();
	private static CommandSender console = Bukkit.getConsoleSender();

	public static String convertColorCodes(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public static void message(CommandSender sender, String message) {
		sender.sendMessage(convertColorCodes(message));
	}

	public static void broadcastMessage(String message, String permission) {
		for (Player onlinePlayer : Bukkit.getOnlinePlayers())
			if (permission == null)
				message(onlinePlayer, message);
			else if (onlinePlayer.hasPermission(permission))
				message(onlinePlayer, message);
		message(console, message);
	}

	public static void configStringMessage(CommandSender sender, String configString) {
		message(sender, config.getString(configString));
	}

	public static void configStringMessage(CommandSender sender, String configString, String regex,
			String replacement) {
		message(sender, config.getString(configString).replaceAll(regex, replacement));
	}

	public static void configStringMessage(CommandSender sender, String configString, String regex, String replacement,
			String regex2, String replacement2) {
		message(sender, config.getString(configString).replaceAll(regex, replacement).replaceAll(regex2, replacement2));
	}

	public static void noPermissionError(CommandSender sender) {
		configStringMessage(sender, "no_perm_message");
	}

	public static void argumentError(CommandSender sender, String usage) {
		configStringMessage(sender, "argument-error", "<usage>", usage);
	}

	public static void consoleError() {
		configStringMessage(console, "console_error_message");
	}

	public static void validPlayerError(CommandSender sender) {
		configStringMessage(sender, "player_offline_message");
	}
}