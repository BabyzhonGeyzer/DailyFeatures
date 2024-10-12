package ru.elementcraft.dailyfeatures.events.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import ru.elementcraft.dailyfeatures.player.progression.PlayerProgressor;

public class BlockBreakListener extends PlayerProgressor implements Listener {

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {

        if (event.isCancelled()) return;
        setPlayerQuestProgression(event, event.getPlayer(), 1, "BREAK");

    }
}



