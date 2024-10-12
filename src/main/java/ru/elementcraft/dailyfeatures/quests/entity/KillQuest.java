package ru.elementcraft.dailyfeatures.quests.entity;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import ru.elementcraft.dailyfeatures.quests.types.shared.BasicQuest;
import ru.elementcraft.dailyfeatures.quests.types.shared.EntityQuest;

public class KillQuest extends EntityQuest {

    public KillQuest(BasicQuest base) {
        super(base);
    }

    @Override
    public String getType() {
        return "KILL";
    }

    @Override
    public boolean canProgress(Event provided) {
        if (provided instanceof EntityDeathEvent event) {
            return super.isRequiredEntity(event.getEntity().getType());
        }

        return false;
    }
}
