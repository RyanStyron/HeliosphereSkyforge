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
import mc.rysty.heliosphereskyforge.utils.IslandsFileManager;
import mc.rysty.heliosphereskyforge.utils.MessageUtils;

public class IslandCommand implements CommandExecutor {

	private IslandsFileManager islandsFileManager = HelioSphereSkyForge.islandsFileManager;
	private FileConfiguration islandsFile = islandsFileManager.getData();

	public IslandCommand(HelioSphereSkyForge plugin) {
		plugin.getCommand("island").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("island")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				UUID playerId = player.getUniqueId();
				Location location = player.getLocation();
				World world = location.getWorld();
				double xCoord;
				double yCoord;
				double zCoord;

				if (world.equals(Bukkit.getWorld("Skyforge"))) {
					if (args.length >= 1) {
						String islandStatus = islandsFile.getString("islands." + playerId + ".status");

						if (args[0].equalsIgnoreCase("create")) {
							if (islandStatus == null) {
								IslandSetup.createIsland(playerId);
								MessageUtils.configStringMessage(sender, "IslandCommand.island_create_message");
							} else if (islandStatus.equalsIgnoreCase("deleted")) {
								islandsFile.set("islands." + playerId, null);
								islandsFileManager.saveData();
								IslandSetup.createIsland(playerId);
								MessageUtils.configStringMessage(sender, "IslandCommand.island_created_old_deleted");
							} else
								MessageUtils.configStringMessage(sender, "IslandCommand.island_create_error");
						} else if (args[0].equalsIgnoreCase("delete")) {
							if (islandStatus != null) {
								if (islandStatus.equalsIgnoreCase("current")) {
									islandsFile.set("islands." + playerId + ".status", "deleted");
									islandsFileManager.saveData();
									player.teleport(world.getSpawnLocation());
									MessageUtils.configStringMessage(sender, "IslandCommand.deleted_island_message");
								} else
									MessageUtils.configStringMessage(sender,
											"IslandCommand.deleted_island_already_deleted");
							} else
								MessageUtils.configStringMessage(sender, "IslandCommand.delete_island_error");
						} else if (args[0].equalsIgnoreCase("home") || args[0].equalsIgnoreCase("h")) {
							if (islandStatus.equalsIgnoreCase("current")) {
								xCoord = islandsFile.getDouble("islands." + playerId + ".location.x");
								yCoord = islandsFile.getDouble("islands." + playerId + ".location.y");
								zCoord = islandsFile.getDouble("islands." + playerId + ".location.z");
								Location playerIslandSpawn = new Location(world, xCoord, yCoord, zCoord);

								player.teleport(playerIslandSpawn);
								player.teleport(world.getHighestBlockAt(playerIslandSpawn).getLocation().add(0, 1, 0));
								MessageUtils.configStringMessage(sender, "IslandCommand.island_home_message");
							} else if (islandStatus.equalsIgnoreCase("deleted")) {
								MessageUtils.configStringMessage(sender, "IslandCommand.island_home_deleted_message");
							} else
								MessageUtils.configStringMessage(sender, "IslandCommand.island_home_null_message");
						} else if (args[0].equalsIgnoreCase("warp")) {
						} else if (args[0].equalsIgnoreCase("retrieve")) {
							if (islandStatus.equalsIgnoreCase("deleted")) {
								islandsFile.set("islands." + playerId + ".status", "current");
								islandsFileManager.saveData();
								MessageUtils.configStringMessage(sender, "IslandCommand.island_retrieve_message");
							} else
								MessageUtils.configStringMessage(sender, "IslandCommand.island_retrieve_error");
						} else if (args[0].equalsIgnoreCase("adminwarp")) {
							if (player.hasPermission("hs.skyforge.adminwarp")) {
								if (args.length == 1)
									MessageUtils.configStringMessage(sender, "IslandCommand.admin_warp_command_error");
								else if (args.length > 1) {
									Player target = Bukkit.getPlayer(args[1]);

									if (target != null) {
										UUID targetId = target.getUniqueId();

										if (islandsFile.getString("islands." + targetId) == null) {
											MessageUtils.configStringMessage(sender,
													"IslandCommand.admin_warp_command_error");
											return false;
										} else if (islandStatus.equalsIgnoreCase("current")) {
											xCoord = islandsFile.getDouble("islands." + targetId + ".location.x");
											yCoord = islandsFile.getDouble("islands." + targetId + ".location.y");
											zCoord = islandsFile.getDouble("islands." + targetId + ".location.z");
											Location islandLocation = new Location(world, xCoord, yCoord, zCoord);

											player.teleport(islandLocation);
											player.teleport(
													world.getHighestBlockAt(islandLocation).getLocation().add(0, 1, 0));
											MessageUtils.configStringMessage(sender, "IslandCommand.admin_warp_message",
													"<target>", target.getDisplayName());
										} else if (islandStatus.equalsIgnoreCase("deleted"))
											MessageUtils.configStringMessage(sender,
													"IslandCommand.admin_warp_command_error");
									} else
										MessageUtils.validPlayerError(sender);
								}
							} else
								MessageUtils.noPermissionError(sender);
						} else
							MessageUtils.configStringMessage(sender, "IslandCommand.improper_arg_error");
					} else if (args.length < 1)
						MessageUtils.configStringMessage(sender, "IslandCommand.improper_arg_error");
				}
			} else
				MessageUtils.consoleError();
		}
		return false;
	}
}
