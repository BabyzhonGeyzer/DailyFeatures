package ru.elementcraft.dailyfeatures.api;

import lombok.Getter;
import ru.elementcraft.dailyfeatures.api.quests.QuestTypeRegistry;
import ru.elementcraft.dailyfeatures.quests.types.AbstractQuest;

import java.util.HashMap;
import java.util.Map;

public class DailyFeaturesAPI {
    @Getter
    private static final Map<String, Class<? extends AbstractQuest>> externalTypes = new HashMap<>();
    @Getter
    private final QuestTypeRegistry questTypeRegistry = new QuestTypeRegistry();
}
