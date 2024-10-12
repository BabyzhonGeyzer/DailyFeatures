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
 * Called when a quest is completed.
 * This method sends the player the rewards of the quest, and increases their total number of completed quests.
 */
public class QuestCompletedEvent extends Event implements Cancellable {

    /** Get the player who completed the quest. */
    @Getter
    private final Player player;
    /** Get the current progression of the quest. */
    @Getter
    private final Progression progression;
    /**  Get the quest that was completed. */
    @Getter
    private final AbstractQuest abstractQuest;

    private static final HandlerList HANDLERS = new HandlerList();


    private boolean isCancelled;

    /**
     * @param player player who completed the quest
     * @param progression current progression of the quest
     * @param abstractQuest quest that was completed
     */
    public QuestCompletedEvent(Player player, Progression progression, AbstractQuest abstractQuest) {
        this.player = player;
        this.progression = progression;
        this.abstractQuest = abstractQuest;
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
