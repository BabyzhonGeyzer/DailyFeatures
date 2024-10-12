package ru.elementcraft.dailyfeatures.player.progression.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.elementcraft.dailyfeatures.api.events.AllQuestsCompletedEvent;
import ru.elementcraft.dailyfeatures.configuration.units.GlobalReward;

public class AllQuestsCompletedListener implements Listener {

    @EventHandler
    public void onAllQuestsCompletedEvent(AllQuestsCompletedEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        GlobalReward.sendGlobalReward(player.getName());
    }
}
