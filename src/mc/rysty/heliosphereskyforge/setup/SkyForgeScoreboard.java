package mc.rysty.heliosphereskyforge.setup;

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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import mc.rysty.heliosphereskyforge.HelioSphereSkyForge;
import mc.rysty.heliosphereskyforge.utils.IslandsFileManager;
import mc.rysty.heliosphereskyforge.utils.MessageUtils;

public class SkyForgeScoreboard implements Listener {

    private HelioSphereSkyForge plugin = HelioSphereSkyForge.getInstance();
    private IslandsFileManager islandsFileManager = HelioSphereSkyForge.islandsFileManager;
    private FileConfiguration islandsFile = islandsFileManager.getData();

    public SkyForgeScoreboard(HelioSphereSkyForge plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private HashMap<UUID, Scoreboard> scoreboardMap = new HashMap<UUID, Scoreboard>();
    private HashMap<String, Location> islandsMap = new HashMap<String, Location>();
    private ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    private String locationString;
    private String lastLocationString = "";

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        World world = location.getWorld();

        if (world.equals(Bukkit.getWorld("Skyforge"))) {
            locationString = "Spawn";
            updateSkyforgeScoreboard(player);
        } else
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        updateSkyforgeScoreboardVariables(event.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Location toLocation = event.getTo();
        Location fromLocation = event.getFrom();

        if (toLocation.getWorld() == fromLocation.getWorld())
            updateSkyforgeScoreboardVariables(event.getPlayer());
    }

    @SuppressWarnings("deprecation")
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
        objective = scoreboard.registerNewObjective("Skyforge", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        animatedScoreboardTitle(objective);

        Score fillerLine = objective.getScore(MessageUtils.convertColorCodes("&3----------------------"));
        Score locationLine = objective.getScore(MessageUtils.convertColorCodes("&fLocation&7:"));
        Score locationScore = objective.getScore(MessageUtils.convertColorCodes("&b" + locationString));
        Score fillerLine2 = objective.getScore(MessageUtils.convertColorCodes("&f&3----------------------"));
        Score dungeonRank = objective.getScore(MessageUtils.convertColorCodes("&fDungeon Rank&7:&b " + ""));
        Score farmingRank = objective.getScore(MessageUtils.convertColorCodes("&fFarming Rank&7:&b " + ""));
        Score fishingRank = objective.getScore(MessageUtils.convertColorCodes("&fFishing Rank&7:&b " + ""));
        Score huntingRank = objective.getScore(MessageUtils.convertColorCodes("&fHunting Rank&7:&b " + ""));
        Score miningRank = objective.getScore(MessageUtils.convertColorCodes("&fMining Rank&7:&b " + ""));
        Score fillerLine3 = objective.getScore(MessageUtils.convertColorCodes("&f&f&3----------------------"));
        Score balance = objective.getScore(MessageUtils.convertColorCodes("&fBalance&7:&b " + ""));
        Score fillerLine4 = objective.getScore(MessageUtils.convertColorCodes("&f&f&f&3----------------------"));

        fillerLine.setScore(16);
        locationLine.setScore(15);
        locationScore.setScore(14);
        fillerLine2.setScore(13);
        dungeonRank.setScore(12);
        farmingRank.setScore(11);
        fishingRank.setScore(10);
        huntingRank.setScore(9);
        miningRank.setScore(8);
        fillerLine3.setScore(7);
        balance.setScore(6);
        fillerLine4.setScore(5);

        scoreboardMap.put(playerId, scoreboard);
        player.setScoreboard(scoreboard);

        lastLocationString = locationString;
    }

    private void updateSkyforgeScoreboardVariables(Player player) {
        String playerName = player.getName();
        Location location = player.getLocation();
        World world = location.getWorld();

        if (world.equals(Bukkit.getWorld("Skyforge"))) {
            if (location.distanceSquared(world.getSpawnLocation()) <= 3000)
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
                        locationString = getKey(islandsMap, islandsMapValues) + "'s &bIsland";

                if (locationString.contains(playerName))
                    locationString = "Your Island";

                if (!locationString.endsWith("Island") && lastLocationString.equalsIgnoreCase("Spawn"))
                    locationString = "Void";
            }
            if (!locationString.equalsIgnoreCase(lastLocationString))
                updateSkyforgeScoreboard(player);
        }
    }

    private void animatedScoreboardTitle(Objective objective) {
        List<String> titles = new ArrayList<>();

        titles.add(MessageUtils.convertColorCodes("&b&lSKYFORGE"));
        titles.add(MessageUtils.convertColorCodes("&6&lSKYFORGE"));
        titles.add(MessageUtils.convertColorCodes("&1&lSKYFORGE"));
        titles.add(MessageUtils.convertColorCodes("&7&lSKYFORGE"));
        titles.add(MessageUtils.convertColorCodes("&4&lSKYFORGE"));
        titles.add(MessageUtils.convertColorCodes("&5&lSKYFORGE"));
        titles.add(MessageUtils.convertColorCodes("&c&lSKYFORGE"));
        titles.add(MessageUtils.convertColorCodes("&b&lSKYFORGE"));

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
        }, 0, 30);
    }

    private <K, V> K getKey(HashMap<K, V> map, V value) {
        for (HashMap.Entry<K, V> entry : map.entrySet())
            if (value.equals(entry.getValue()))
                return entry.getKey();
        return null;
    }
}