package ru.elementcraft.dailyfeatures.api.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.elementcraft.dailyfeatures.player.progression.Progression;
import ru.elementcraft.dailyfeatures.quests.types.AbstractQuest;

/**
 * Called when a quest progress.
 * This method advances the player's quest by the amount indicated.
 * It first checks that the player is not in a world prohibited by the configuration.
 */
public class QuestProgressEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;

    /** Get the player who progressed the quest. */
    @Getter
    private final Player player;


    /** Get the current progression of the quest. */
    @Getter
    private final Progression progression;
    /** Get the quest that was progressed. */
    @Getter
    private final AbstractQuest quest;
    /** Get the amount of progression. */
    @Getter
    private final int amount;

    /**
     * @param player player who progressed the quest
     * @param progression current progression of the quest
     * @param quest quest that was progressed
     * @param amount amount of progression
     */
    public QuestProgressEvent(Player player, Progression progression, AbstractQuest quest, int amount) {
        this.player = player;
        this.progression = progression;
        this.quest = quest;
        this.amount = amount;

        this.isCancelled = false;
    }


    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
