package mc.rysty.heliosphereskyforge.setup;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import mc.rysty.heliosphereskyforge.HelioSphereSkyForge;
import mc.rysty.heliosphereskyforge.utils.MessageUtils;

public class SkyForgeScoreboard implements Listener {

    public SkyForgeScoreboard(HelioSphereSkyForge plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private HashMap<UUID, Scoreboard> scoreboardMap = new HashMap<UUID, Scoreboard>();
    private ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    private Score currentLocation;
    private Score blankSpace;
    private String island;

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        World world = location.getWorld();

        if (world.equals(Bukkit.getWorld("Skyforge"))) {
            island = "Spawn";
            updateScoreboard(player);
        } else
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        World world = location.getWorld();

        if (world.equals(Bukkit.getWorld("Skyforge"))) {
            if (location.distanceSquared(world.getSpawnLocation()) < 25) {
                island = "Spawn";
            } else
                island = "Other";
            updateScoreboard(player);
        }
    }

    private void updateScoreboard(Player player) {
        UUID playerId = player.getUniqueId();

        if (!scoreboardMap.containsKey(playerId)) {
            Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
            scoreboardMap.put(playerId, scoreboard);
        }
        Scoreboard scoreboard = scoreboardMap.get(playerId);
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);

        if (objective != null)
            objective.unregister();

        objective = scoreboard.registerNewObjective("Skyforge", "dummy",
                MessageUtils.convertColorCodes("&b&lSKYFORGE"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        currentLocation = objective.getScore(MessageUtils.convertColorCodes("&7Location&f:&b " + island));
        blankSpace = objective.getScore("");

        currentLocation.setScore(16);
        blankSpace.setScore(15);

        scoreboardMap.put(playerId, scoreboard);
        player.setScoreboard(scoreboard);
    }
}