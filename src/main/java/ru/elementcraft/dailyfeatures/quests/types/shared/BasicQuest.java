package ru.elementcraft.dailyfeatures.quests.types.shared;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import ru.elementcraft.dailyfeatures.quests.types.AbstractQuest;
import ru.elementcraft.dailyfeatures.rewards.Reward;

import java.util.List;

public class BasicQuest extends AbstractQuest {

    public BasicQuest(int questIndex, String questName, List<String> questDesc, String questType, ItemStack menuItem, ItemStack achievedItem, int amountRequired, Reward reward) {
        super(questIndex, questName, questDesc, questType, menuItem, achievedItem, amountRequired, reward);
    }

    @Override
    public String getType() {
        return "BASIC";
    }


    @Override
    public boolean canProgress(@Nullable Event provided) {
        return false;
    }

    @Override
    public boolean loadParameters(ConfigurationSection section, String index) {
        return true;
    }
}
