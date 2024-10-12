package ru.elementcraft.dailyfeatures.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import ru.elementcraft.dailyfeatures.DailyFeatures;
import ru.elementcraft.dailyfeatures.events.listeners.BlockBreakListener;
import ru.elementcraft.dailyfeatures.events.listeners.EntityDeathListener;

public class EventsManager {

    private final DailyFeatures dailyFeatures;

    public EventsManager(DailyFeatures dailyFeatures) {
        this.dailyFeatures = dailyFeatures;
    }

    /** Registers all events. */
    public void registerListeners() {
        // entity events
        Bukkit.getPluginManager().registerEvents(new EntityDeathListener(), dailyFeatures);

        // item events
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), dailyFeatures);
    }
}
