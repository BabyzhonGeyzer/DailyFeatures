package ru.elementcraft.dailyfeatures.api.quests;

import ru.elementcraft.dailyfeatures.quests.types.AbstractQuest;

import java.util.HashMap;

public class QuestTypeRegistry extends HashMap<String, Class<? extends AbstractQuest>> {
    public void registerQuestType(String type, Class<? extends AbstractQuest> questClass) {
        System.out.print("Registering quest type: " + type);
        this.put(type, questClass);
    }
}
