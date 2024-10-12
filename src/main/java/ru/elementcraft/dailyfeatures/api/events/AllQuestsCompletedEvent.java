package ru.elementcraft.dailyfeatures.api.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player completes all his quests.
 * If defined, this method sends the global reward to the player.
 */
public class AllQuestsCompletedEvent extends Event implements Cancellable {

    /** Get the player who completed all his quests. */
    @Getter
    private final Player player;
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;

    /** @param player player who completed all his quests */
    public AllQuestsCompletedEvent(Player player) {
        this.player = player;
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
