package mc.rysty.heliosphereskyforge.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

@SuppressWarnings("deprecation")
public class SkyForgeScoreboardRanks {

    private static Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    public static int getSkyforgeDungeonRank(Player player) {
        return scoreboard.getObjective("DungeonRank").getScore(player).getScore();
    }

    public static int getSkyforgeFarmingRank(Player player) {
        return scoreboard.getObjective("FarmingRank").getScore(player).getScore();
    }

    public static int getSkyforgeFishingRank(Player player) {
        return scoreboard.getObjective("FishingRank").getScore(player).getScore();
    }

    public static int getSkyforgeHuntingRank(Player player) {
        return scoreboard.getObjective("HuntingRank").getScore(player).getScore();
    }

    public static int getSkyforgeMiningRank(Player player) {
        return scoreboard.getObjective("MiningRank").getScore(player).getScore();
    }
}