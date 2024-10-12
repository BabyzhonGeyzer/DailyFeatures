package ru.elementcraft.dailyfeatures.quests;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.elementcraft.dailyfeatures.configuration.units.Temporality;
import ru.elementcraft.dailyfeatures.player.PlayerQuests;
import ru.elementcraft.dailyfeatures.player.QuestsManager;
import ru.elementcraft.dailyfeatures.player.progression.Progression;
import ru.elementcraft.dailyfeatures.quests.QuestsLoader;
import ru.elementcraft.dailyfeatures.quests.types.AbstractQuest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QuestLoaderUtils {

    /**
     * Check if it is time to redraw quests for a player.
     * @param timestamp player timestamp.
     * @return true if it's time to redraw quests.
     */
    public static boolean checkTimestamp(long timestamp) {

        switch (Temporality.getTemporalityMode()) {
            case 1 -> {
                return System.currentTimeMillis() - timestamp >= 86400000L;
            }
            case 2 -> {
                return System.currentTimeMillis() - timestamp >= 604800000L;
            }
            case 3 -> {
                return System.currentTimeMillis() - timestamp >= 2678400000L;
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * Load quests for a player with no data.
     * @param playerName   player name.
     * @param activeQuests all active quests.
     */
    public static void loadNewPlayerQuests(String playerName, Map<String, PlayerQuests> activeQuests, int totalAchievedQuests) {

        activeQuests.remove(playerName);

        LinkedHashMap<AbstractQuest, Progression> quests = QuestsManager.selectRandomQuests();
        PlayerQuests playerQuests;

        playerQuests = new PlayerQuests(System.currentTimeMillis(), quests);

        playerQuests.setTotalAchievedQuests(totalAchievedQuests);

        final Player player = Bukkit.getPlayer(playerName);
        if (player == null) return;




        if (Bukkit.getPlayer(playerName) != null) activeQuests.put(playerName, playerQuests);

    }

    /**
     * Check if it's time to renew quests. If so, renew them.
     * @param player    player.
     * @param activeQuests all active quests.
     * @return true if it's time to renew quests.
     */
    public static boolean isTimeToRenew(Player player, Map<String, PlayerQuests> activeQuests) {
        final PlayerQuests playerQuests = activeQuests.get(player.getName());

        if (checkTimestamp(playerQuests.getTimestamp())) {
            loadNewPlayerQuests(player.getName(), activeQuests, playerQuests.getTotalAchievedQuests());
            return true;
        }

        return false;
    }

    /**
     * Find quest with index in arrays.
     * @param questIndex index of quest in array.
     * @return quest of index.
     */
    public static AbstractQuest findQuest(int questIndex) {
        AbstractQuest quest;
        try {
            quest = QuestsLoader.getQuestsGlobal().get(questIndex);
        } catch (IndexOutOfBoundsException e) {

            quest = QuestsLoader.getQuestsGlobal().get(0);
        }
        return quest;
    }
}
