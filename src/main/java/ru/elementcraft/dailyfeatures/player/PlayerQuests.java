package ru.elementcraft.dailyfeatures.player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import ru.elementcraft.dailyfeatures.DailyFeatures;
import ru.elementcraft.dailyfeatures.api.events.AllQuestsCompletedEvent;
import ru.elementcraft.dailyfeatures.player.progression.Progression;
import ru.elementcraft.dailyfeatures.quests.types.AbstractQuest;

import java.util.LinkedHashMap;

/**
 * Represents the quests of a player and its data.
 */
@Getter
public class PlayerQuests {

    /* Timestamp of last quests renew */
    @Setter
    private Long timestamp;
    @Setter
    private int achievedQuests;
    @Setter
    private int totalAchievedQuests;
    private final LinkedHashMap<AbstractQuest, Progression> playerQuests;

    public PlayerQuests(Long timestamp, LinkedHashMap<AbstractQuest, Progression> playerQuests) {
        this.timestamp = timestamp;
        this.playerQuests = playerQuests;
        this.achievedQuests = 0;
        this.totalAchievedQuests = 0;
    }
    /**
     * Increase number of achieved quests.
     *
     * @param player player who achieved a quest.
     */
    public void increaseAchievedQuests(Player player) {


        this.achievedQuests++;
        this.totalAchievedQuests++;


        /* check if the player have completed all quests */
        if (this.achievedQuests == 2) {

            final AllQuestsCompletedEvent event = new AllQuestsCompletedEvent(player);
            DailyFeatures.INSTANCE.getServer().getPluginManager().callEvent(event);
        }
    }

}
