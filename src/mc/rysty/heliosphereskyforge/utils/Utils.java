package mc.rysty.heliosphereskyforge.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Utils {

	public static String chat(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public static void messge(CommandSender sender, String message) {
		sender.sendMessage(chat(message));
	}
}
