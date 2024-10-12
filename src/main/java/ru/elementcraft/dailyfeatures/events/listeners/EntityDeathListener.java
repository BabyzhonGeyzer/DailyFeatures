package ru.elementcraft.dailyfeatures.events.listeners;


import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import ru.elementcraft.dailyfeatures.player.progression.PlayerProgressor;

public class EntityDeathListener extends PlayerProgressor implements Listener {

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        final LivingEntity entity = event.getEntity();


        if (entity.getKiller() == null) return;
        setPlayerQuestProgression(event, entity.getKiller(), 1, "KILL");
    }
}
