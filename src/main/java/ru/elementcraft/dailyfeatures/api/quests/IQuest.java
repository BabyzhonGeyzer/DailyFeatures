package ru.elementcraft.dailyfeatures.api.quests;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public interface IQuest {
    String getType();
    boolean canProgress(@Nullable Event provided);
    boolean loadParameters(ConfigurationSection section, String index);
}
