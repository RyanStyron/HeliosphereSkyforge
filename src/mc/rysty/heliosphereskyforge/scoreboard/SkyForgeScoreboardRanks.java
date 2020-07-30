package mc.rysty.heliosphereskyforge.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import mc.rysty.heliosphereskyforge.utils.JavaUtils;

@SuppressWarnings("deprecation")
public class SkyForgeScoreboardRanks {

    private static Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    public static String getSkyforgeDungeonRank(Player player) {
        return JavaUtils.getRomanNumeral(scoreboard.getObjective("DungeonRank").getScore(player).getScore());
    }

    public static String getSkyforgeFarmingRank(Player player) {
        return JavaUtils.getRomanNumeral(scoreboard.getObjective("FarmingRank").getScore(player).getScore());
    }

    public static String getSkyforgeFishingRank(Player player) {
        return JavaUtils.getRomanNumeral(scoreboard.getObjective("FishingRank").getScore(player).getScore());
    }

    public static String getSkyforgeHuntingRank(Player player) {
        return JavaUtils.getRomanNumeral(scoreboard.getObjective("HuntingRank").getScore(player).getScore());
    }

    public static String getSkyforgeMiningRank(Player player) {
        return JavaUtils.getRomanNumeral(scoreboard.getObjective("MiningRank").getScore(player).getScore());
    }
}