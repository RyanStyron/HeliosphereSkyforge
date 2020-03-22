package mc.rysty.heliosphereskyforge.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereskyforge.HelioSphereSkyForge;
import mc.rysty.heliosphereskyforge.island.IslandSetup;
import mc.rysty.heliosphereskyforge.utils.SettingsManager;
import mc.rysty.heliosphereskyforge.utils.Utils;

public class IslandCommand implements CommandExecutor {

	private HelioSphereSkyForge plugin = HelioSphereSkyForge.getInstance();
	private FileConfiguration config = plugin.getConfig();
	private SettingsManager settings = SettingsManager.getInstance();
	private FileConfiguration data = settings.getData();

	public IslandCommand(HelioSphereSkyForge plugin) {
		plugin.getCommand("island").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("island")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				UUID pId = p.getUniqueId();
				String noPermMessage = Utils.chat(config.getString("no_perm_message"));
				String player_offline_message = Utils.chat(config.getString("player_offline_message"));
				String island_create_error = Utils.chat(config.getString("IslandCommand.island_create_error"));
				String island_create_message = Utils.chat(config.getString("IslandCommand.island_create_message"));
				String island_created_old_deleted = Utils
						.chat(config.getString("IslandCommand.island_created_old_deleted"));
				String delete_island_error = Utils.chat(config.getString("IslandCommand.delete_island_error"));
				String deleted_island_message = Utils.chat(config.getString("IslandCommand.deleted_island_message"));
				String deleted_island_already_deleted = Utils
						.chat(config.getString("IslandCommand.deleted_island_already_deleted"));
				String admin_warp_message = Utils.chat(config.getString("IslandCommand.admin_warp_message"));
				String admin_warp_command_error = Utils
						.chat(config.getString("IslandCommand.admin_warp_command_error"));
				String admin_warp_island_error = Utils.chat(config.getString("IslandCommand.admin_warp_island_error"));
				String island_home_message = Utils.chat(config.getString("IslandCommand.island_home_message"));
				String island_home_deleted_message = Utils
						.chat(config.getString("IslandCommand.island_home_deleted_message"));
				String island_home_null_message = Utils
						.chat(config.getString("IslandCommand.island_home_null_message"));
				String island_retrieve_message = Utils.chat(config.getString("IslandCommand.island_retrieve_message"));
				String island_retrieve_error = Utils.chat(config.getString("IslandCommand.island_retrieve_error"));
				String not_enough_args_message = Utils.chat(config.getString("IslandCommand.not_enough_args"));
				String improper_arg_error = Utils.chat(config.getString("IslandCommand.improper_arg_error"));
				World skyforge = Bukkit.getWorld("Skyforge");
				double pX;
				double pY;
				double pZ;

				if (p.getWorld().getName().equalsIgnoreCase("Skyforge")) {
					if (args.length >= 1) {
						String a0 = args[0];

						if (a0.equalsIgnoreCase("create")) {
							if (data.getString("islands." + pId) == null) {
								IslandSetup.createIsland(pId);
								p.sendMessage(island_create_message);
							} else if (data.getString("islands." + pId + ".status") == "deleted") {
								pX = data.getDouble("islands." + pId + ".location.x");
								pY = data.getDouble("islands." + pId + ".location.y");
								pZ = data.getDouble("islands." + pId + ".location.z");
								Location islandLocation = new Location(skyforge, pX, pY, pZ);

								IslandSetup.deleteIsland(p, islandLocation);
								data.set("islands." + pId, null);
								settings.saveData();
								IslandSetup.createIsland(pId);
								p.sendMessage(island_created_old_deleted);
							} else if (data.getString("islands." + pId + ".status") == "current") {
								p.sendMessage(island_create_error);
							}
						} else if (a0.equalsIgnoreCase("delete")) {
							if (data.getString("islands." + pId + ".status") != null) {
								if (data.getString("islands." + pId + ".status") == "current") {
									data.set("islands." + pId + ".status", "deleted");
									settings.saveData();
									p.teleport(skyforge.getSpawnLocation());
									p.sendMessage(deleted_island_message);
								} else if (data.getString("islands." + pId + ".status") == "deleted") {
									p.sendMessage(deleted_island_already_deleted);
								}
							} else {
								p.sendMessage(delete_island_error);
							}
						} else if (a0.equalsIgnoreCase("home") || a0.equalsIgnoreCase("h")) {
							if (data.getString("islands." + pId + ".status") == "current") {
								pX = data.getDouble("islands." + pId + ".location.x");
								pY = data.getDouble("islands." + pId + ".location.y");
								pZ = data.getDouble("islands." + pId + ".location.z");
								Location playerIslandSpawn = new Location(skyforge, pX, pY, pZ);

								p.teleport(playerIslandSpawn);
								p.teleport(p.getWorld().getHighestBlockAt(p.getLocation()).getLocation().add(0, 1, 0));
								p.sendMessage(island_home_message);
							} else if (data.getString("islands." + pId + ".status") == "deleted") {
								p.sendMessage(island_home_deleted_message);
							} else if (data.getString("islands." + pId + ".status") == null) {
								p.sendMessage(island_home_null_message);
							}
						} else if (a0.equalsIgnoreCase("warp")) {

						}
						if (a0.equalsIgnoreCase("retrieve")) {
							if (data.getString("islands." + pId + ".status") == "deleted") {
								data.set("islands." + pId + ".status", "current");
								settings.saveData();
								p.sendMessage(island_retrieve_message);
							} else {
								p.sendMessage(island_retrieve_error);
							}
						}

						// island adminwarp command
						if (a0.equalsIgnoreCase("adminwarp")) {
							if (p.hasPermission("hs.skyforge.adminwarp")) {
								if (args.length == 1) {
									p.sendMessage(admin_warp_command_error);
									return false;
								} else if (args.length > 1) {
									Player target = Bukkit.getPlayer(args[1]);

									if (target == null) {
										p.sendMessage(player_offline_message);
										return false;
									}
									UUID tId = target.getUniqueId();

									if (data.getString("islands." + tId) == null) {
										p.sendMessage(admin_warp_island_error);
										return false;
									} else if (data
											.getString("islands." + target.getUniqueId() + ".status") == "current") {
										pX = data.getDouble("islands." + tId + ".location.x");
										pY = data.getDouble("islands." + tId + ".location.y");
										pZ = data.getDouble("islands." + tId + ".location.z");
										Location islandLocation = new Location(skyforge, pX, pY, pZ);

										p.teleport(islandLocation);
										p.teleport(p.getWorld().getHighestBlockAt(p.getLocation()).getLocation().add(0,
												1, 0));
										p.sendMessage(
												admin_warp_message.replaceAll("<target>", target.getDisplayName()));
									} else if (data
											.getString("islands." + target.getUniqueId() + ".status") == "deleted") {
										p.sendMessage(admin_warp_command_error);
									}
								}
							} else {
								p.sendMessage(noPermMessage);
							}
						} else if (!(a0.equalsIgnoreCase("home") || a0.equalsIgnoreCase("h")
								|| a0.equalsIgnoreCase("create") || a0.equalsIgnoreCase("delete")
								|| a0.equalsIgnoreCase("adminwarp") || a0.equalsIgnoreCase("retrieve")
								|| a0.equalsIgnoreCase("warp"))) {
							p.sendMessage(improper_arg_error);
						}
					} else if (args.length < 1) {
						sender.sendMessage(not_enough_args_message);
					}
				}
			} else {
				sender.sendMessage(Utils.chat(config.getString("console_error_message")));
			}
		}
		return false;
	}
}
