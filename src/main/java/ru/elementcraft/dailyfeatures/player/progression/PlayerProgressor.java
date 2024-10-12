package ru.elementcraft.dailyfeatures.player.progression;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import ru.elementcraft.dailyfeatures.quests.QuestLoaderUtils;
import ru.elementcraft.dailyfeatures.api.events.QuestCompletedEvent;
import ru.elementcraft.dailyfeatures.api.events.QuestProgressEvent;
import ru.elementcraft.dailyfeatures.player.QuestsManager;
import ru.elementcraft.dailyfeatures.quests.types.AbstractQuest;

import java.util.HashMap;
import java.util.Map;

public class PlayerProgressor {

    public void setPlayerQuestProgression(Event event, Player player, int amount, String questType) {


        if (QuestsManager.getActiveQuests().containsKey(player.getName())) {
            checkForProgress(event, player, amount, questType);
        }
    }

    /**
     * Check for progress for a specific quest type
     *
     * @param event     the event that triggered the progression
     * @param player    the player to check for progress
     * @param amount    the amount of progression
     * @param questType the quest type to check for
     */
    private static void checkForProgress(Event event, Player player, int amount, String questType) {
        final HashMap<AbstractQuest, Progression> playerQuests = QuestsManager.getActiveQuests().get(player.getName()).getPlayerQuests();
        for (Map.Entry<AbstractQuest, Progression> entry : playerQuests.entrySet()) {
            final AbstractQuest quest = entry.getKey();
            if (quest.getQuestType().equals(questType)) {
                final Progression progression = entry.getValue();
                if (!progression.isAchieved() && quest.canProgress(event)) {
                    actionQuest(player, progression, quest, amount);
                }
            }
        }
    }

    /**
     * Raises the QuestProgressEvent event and determines whether to perform progress based on the event result.
     *
     * @param player      involved player
     * @param progression player's progression
     * @param quest       quest to be progressed
     * @param amount      amount of progression
     */
    public static void actionQuest(Player player, Progression progression, AbstractQuest quest, int amount) {


        final QuestProgressEvent event = new QuestProgressEvent(player, progression, quest, amount);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            runProgress(player, progression, quest, amount);
        }
    }


    /**
     * Increases quest progress.
     *
     * @param player      involved player
     * @param progression player's progression
     * @param quest       quest to be progressed
     * @param amount      amount of progression
     */
    private static void runProgress(Player player, Progression progression, AbstractQuest quest, int amount) {
        if (QuestLoaderUtils.isTimeToRenew(player, QuestsManager.getActiveQuests())) return;


        for (int i = 0; i < amount; i++) {
            progression.increaseProgression();
        }

        if (progression.getProgression() >= quest.getAmountRequired()) {
            final QuestCompletedEvent completedEvent = new QuestCompletedEvent(player, progression, quest);
            Bukkit.getPluginManager().callEvent(completedEvent);
        }

    }

}
