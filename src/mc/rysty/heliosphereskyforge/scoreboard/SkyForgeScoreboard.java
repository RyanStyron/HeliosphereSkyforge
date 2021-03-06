package mc.rysty.heliosphereskyforge.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import mc.rysty.heliosphereskyforge.HelioSphereSkyForge;
import mc.rysty.heliosphereskyforge.utils.EconomyUtils;
import mc.rysty.heliosphereskyforge.utils.IslandsFileManager;
import mc.rysty.heliosphereskyforge.utils.JavaUtils;
import mc.rysty.heliosphereskyforge.utils.MessageUtils;

public class SkyForgeScoreboard implements Listener {

    private HelioSphereSkyForge plugin = HelioSphereSkyForge.getInstance();
    private IslandsFileManager islandsFileManager = HelioSphereSkyForge.islandsFileManager;
    private FileConfiguration islandsFile = islandsFileManager.getData();
    private PluginManager pluginManager = plugin.getServer().getPluginManager();

    public SkyForgeScoreboard(HelioSphereSkyForge plugin) {
        pluginManager.registerEvents(this, plugin);
        skyforgeScoreboardScheduler();
    }

    private HashMap<UUID, Scoreboard> scoreboardMap = new HashMap<UUID, Scoreboard>();
    private HashMap<String, Location> islandsMap = new HashMap<String, Location>();
    private ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    private String locationString;
    private HashMap<UUID, String> lastLocationMap = new HashMap<UUID, String>();
    private String balanceString;
    private HashMap<UUID, String> lastBalanceMap = new HashMap<UUID, String>();

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        World world = player.getWorld();

        lastLocationMap.put(playerId, null);
        lastBalanceMap.put(playerId, null);

        if (world.equals(Bukkit.getWorld("Skyforge"))) {
            lastLocationMap.put(playerId, null);
            lastBalanceMap.put(playerId, null);
        } else if (!world.equals(Bukkit.getWorld("Moshpit")))
            player.setScoreboard(scoreboardManager.getNewScoreboard());
    }

    private void skyforgeScoreboardScheduler() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getWorld() == Bukkit.getWorld("Skyforge")) {
                        updateSkyforgeScoreboardVariables(player);
                    }
                }
            }
        }, 0, 20);
    }

    private void updateSkyforgeScoreboard(Player player) {
        UUID playerId = player.getUniqueId();

        if (!scoreboardMap.containsKey(playerId)) {
            Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
            scoreboardMap.put(playerId, scoreboard);
        }
        Scoreboard scoreboard = scoreboardMap.get(playerId);
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);

        if (objective != null)
            objective.unregister();
        objective = scoreboard.registerNewObjective("Skyforge", "dummy", "Skyforge");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        animatedScoreboardTitle(objective);

        Score fillerLine = objective.getScore(MessageUtils.convertColorCodes("&3----------------------"));
        Score locationLine = objective.getScore(MessageUtils.convertColorCodes("&fLocation&7:"));
        Score locationScore = objective.getScore(MessageUtils.convertColorCodes("&b" + locationString));
        Score fillerLine2 = objective.getScore(MessageUtils.convertColorCodes("&f&f&3----------------------"));
        Score balance = objective.getScore(MessageUtils.convertColorCodes("&fCoins&7:&6 " + balanceString));
        Score fillerLine3 = objective.getScore(MessageUtils.convertColorCodes("&f&f&f&3----------------------"));

        fillerLine.setScore(5);
        locationLine.setScore(4);
        locationScore.setScore(3);
        fillerLine2.setScore(2);
        balance.setScore(1);
        fillerLine3.setScore(0);

        scoreboardMap.put(playerId, scoreboard);
        player.setScoreboard(scoreboard);

        lastLocationMap.put(playerId, locationString);
        lastBalanceMap.put(playerId, balanceString);
    }

    private void updateSkyforgeScoreboardVariables(Player player) {
        String playerName = player.getName();
        Location location = player.getLocation();
        World world = location.getWorld();

        if (world.equals(Bukkit.getWorld("Skyforge"))) {
            /* Location variable. */
            if (location.distanceSquared(world.getSpawnLocation()) <= 10000)
                locationString = "Spawn";
            else {
                for (String storedIsland : islandsFile.getConfigurationSection("islands").getKeys(false)) {
                    String storedIslandOwner = islandsFile.getString("islands." + storedIsland + ".owner");
                    double storedIslandXCoord = islandsFile.getDouble("islands." + storedIsland + ".location.x");
                    double storedIslandYCoord = islandsFile.getDouble("islands." + storedIsland + ".location.y");
                    double storedIslandZCoord = islandsFile.getDouble("islands." + storedIsland + ".location.z");
                    Location storedIslandLocation = new Location(world, storedIslandXCoord, storedIslandYCoord,
                            storedIslandZCoord);

                    islandsMap.put(storedIslandOwner, storedIslandLocation);
                }
                for (Location islandsMapValues : islandsMap.values())
                    if (location.distanceSquared(islandsMapValues) < 10000)
                        locationString = JavaUtils.getHashMapKey(islandsMap, islandsMapValues) + "'s &bIsland";
                if (locationString.contains(playerName))
                    locationString = "Your Island";
            }
            /* Balance variable. */
            if (pluginManager.isPluginEnabled("Vault") && EconomyUtils.getEconomy() != null)
                balanceString = "" + EconomyUtils.getPlayerBalance(player);
            else
                balanceString = "N/A";

            /* Update scoreboard. */
            if (skyforgeScoreboardValuesChanged(player))
                updateSkyforgeScoreboard(player);
        }
    }

    private boolean skyforgeScoreboardValuesChanged(Player player) {
        UUID playerId = player.getUniqueId();
        String lastPlayerLocation = lastLocationMap.get(playerId);
        String lastPlayerBalance = lastBalanceMap.get(playerId);

        if (lastPlayerLocation == null)
            lastPlayerLocation = "";
        if (lastPlayerBalance == null)
            lastPlayerBalance = "";

        if (!lastPlayerLocation.equals(locationString) || !lastPlayerBalance.equals(balanceString))
            return true;
        return false;
    }

    private void animatedScoreboardTitle(Objective objective) {
        List<String> titles = new ArrayList<>();

        titles.add(MessageUtils.convertColorCodes("&3&lSKYFORGE"));
        titles.add(MessageUtils.convertColorCodes("&b&lSKYFORGE"));
        titles.add(MessageUtils.convertColorCodes("&f&lSKYFORGE"));
        titles.add(MessageUtils.convertColorCodes("&b&lSKYFORGE"));
        titles.add(MessageUtils.convertColorCodes("&3&lSKYFORGE"));

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int integer = 0;

            @Override
            public void run() {
                if (integer >= titles.size())
                    integer = 0;
                else if (integer < titles.size())
                    objective.setDisplayName(titles.get(integer));
                integer++;
            }
        }, 0, 5);
    }
}