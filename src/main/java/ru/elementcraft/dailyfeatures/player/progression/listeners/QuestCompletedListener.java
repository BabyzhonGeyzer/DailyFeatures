package ru.elementcraft.dailyfeatures.player.progression.listeners;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.elementcraft.dailyfeatures.api.events.QuestCompletedEvent;
import ru.elementcraft.dailyfeatures.player.QuestsManager;
import ru.elementcraft.dailyfeatures.rewards.RewardManager;

public class QuestCompletedListener implements Listener {

    @EventHandler
    public void onQuestCompletedEvent(QuestCompletedEvent event) {
        final var player = event.getPlayer();
        final var progression = event.getProgression();
        final var quest = event.getAbstractQuest();


        progression.setAchieved(true);
        RewardManager.sendAllRewardItems(quest.getQuestName(), player, quest.getReward());
        QuestsManager.getActiveQuests().get(player.getName()).increaseAchievedQuests(player);
    }
}
