package ru.elementcraft.dailyfeatures.quests.types;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import ru.elementcraft.dailyfeatures.api.quests.IQuest;
import ru.elementcraft.dailyfeatures.player.progression.PlayerProgressor;
import ru.elementcraft.dailyfeatures.quests.types.shared.BasicQuest;
import ru.elementcraft.dailyfeatures.rewards.Reward;

import java.util.List;

@Getter
public abstract class AbstractQuest extends PlayerProgressor implements IQuest {
    final int questIndex;
    final String questName;
    final List<String> questDesc;
    final String questType;
    final ItemStack menuItem;
    final ItemStack achievedItem;
    final int amountRequired;
    final Reward reward;

    /**
     * Quest constructor.
     *
     * @param questName      name of the quest.
     * @param questDesc      description of the quest.
     * @param questType      type of the quest.
     * @param amountRequired required amount of the item.
     * @param reward         reward of the quest.
     */
    protected AbstractQuest(int questIndex, String questName, List<String> questDesc, String questType, ItemStack menuItem, ItemStack achievedItem, int amountRequired, Reward reward) {
        this.questIndex = questIndex;
        this.questName = questName;
        this.questDesc = questDesc;
        this.questType = questType;
        this.menuItem = menuItem;
        this.achievedItem = achievedItem;
        this.amountRequired = amountRequired;
        this.reward = reward;
    }
    protected AbstractQuest(BasicQuest basicQuest) {
        this.questIndex = basicQuest.getQuestIndex();
        this.questName = basicQuest.getQuestName();
        this.questDesc = basicQuest.getQuestDesc();
        this.questType = basicQuest.getQuestType();
        this.menuItem = basicQuest.getMenuItem();
        this.achievedItem = basicQuest.getAchievedItem();
        this.amountRequired = basicQuest.getAmountRequired();
        this.reward = basicQuest.getReward();
    }
}