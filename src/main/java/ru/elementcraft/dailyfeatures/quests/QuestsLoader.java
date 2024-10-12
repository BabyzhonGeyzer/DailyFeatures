package ru.elementcraft.dailyfeatures.quests;


import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import ru.elementcraft.dailyfeatures.DailyFeatures;
import ru.elementcraft.dailyfeatures.api.quests.QuestTypeRegistry;
import ru.elementcraft.dailyfeatures.quests.types.AbstractQuest;
import ru.elementcraft.dailyfeatures.quests.types.shared.BasicQuest;
import ru.elementcraft.dailyfeatures.rewards.Reward;
import ru.elementcraft.dailyfeatures.rewards.RewardLoader;
import ru.elementcraft.dailyfeatures.rewards.RewardType;
import ru.elementcraft.dailyfeatures.tools.ColorConvert;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class QuestsLoader {

    @Getter
    private static final List<AbstractQuest> questsGlobal = new ArrayList<>();
    private final RewardLoader rewardLoader = new RewardLoader();
    private final QuestTypeRegistry questTypeRegistry = DailyFeatures.INSTANCE.getAPI().getQuestTypeRegistry();

    /**
     * Load the reward of a quest.
     *
     * @param questSection the current quest section.
     * @return the reward of the quest.
     */
    private Reward createReward(ConfigurationSection questSection) {
        if (!questSection.isConfigurationSection(".reward")) return new Reward(RewardType.NONE, 0);
        final ConfigurationSection rewardSection = questSection.getConfigurationSection(".reward");

        return rewardLoader.getRewardFromSection(rewardSection);
    }

    /**
     * Create a quest with all basic information.
     *
     * @param questSection the current quest section.
     * @param questIndex   the quest index in the file.
     * @return the global quest.
     */
    private BasicQuest createBasicQuest(ConfigurationSection questSection, int questIndex) {

        /* quest name */
        String questName = ColorConvert.convertColorCode(questSection.getString(".name"));

        /* quest description */
        List<String> questDesc = questSection.getStringList(".description");
        for (String string : questDesc) questDesc.set(questDesc.indexOf(string), ColorConvert.convertColorCode(string));

        /* quest type */
        final String questType = questSection.getString(".quest_type");
        if (!questTypeRegistry.containsKey(questType)) {
            return null;
        }

        /* required amount */
        int requiredAmount = !questSection.contains(".required_amount") ? 1 : questSection.getInt(".required_amount");
        if (requiredAmount < 1) requiredAmount = 1;

        /* menu icon */
        String presumedItem = questSection.getString(".menu_item");
        if (presumedItem == null) {
            return null;
        }
        final ItemStack menuItem = new ItemStack(Material.valueOf(presumedItem));


        /* menu icon achieved */
        final ItemStack achievedItem;
        if (questSection.isString("achieved_menu_item")) {
            final String presumedAchievedItem = questSection.getString("achieved_menu_item");
            achievedItem = new ItemStack(Material.valueOf(presumedAchievedItem));
        } else {
            achievedItem = menuItem;
        }

        /* reward */
        final Reward reward = createReward(questSection);

        return new BasicQuest(questIndex, questName, questDesc, questType, menuItem,
                achievedItem, requiredAmount, reward);
    }

    /** Load quests from a file. */
    public void loadQuests(FileConfiguration file) {
        final ConfigurationSection allQuestsSection = file.getConfigurationSection("quests");
        if (allQuestsSection == null) {
            return;
        }

        int questIndex = 0;

        for (String fileQuest : allQuestsSection.getKeys(false)) {
            final ConfigurationSection questSection = allQuestsSection.getConfigurationSection(fileQuest);
            if (questSection == null) continue;

            final BasicQuest base = createBasicQuest(questSection, questIndex);
            if (base == null) continue;

            final String questType = base.getQuestType();
            if (registerQuest(questType, base, questSection, fileQuest)) {
                questIndex++;
            }
        }
        if (questsGlobal.size() < 2) {
            System.out.println("Impossible to enable the plugin.");
            System.out.println("You need to have at least " + 2 + " quests in your quests.yml file.");
            Bukkit.getPluginManager().disablePlugin(DailyFeatures.INSTANCE);
        }
    }

    /**
     * Register a quest.
     * @param questType    type of the quest.
     * @param base         parent quest.
     * @param questSection current quest section.
     * @param questIndex   quest index in the file.
     */
    private boolean registerQuest(String questType, BasicQuest base, ConfigurationSection questSection, String questIndex) {
        final Class<? extends AbstractQuest> questClass = questTypeRegistry.get(questType);

        AbstractQuest questInstance = null;
        try {
            questInstance = questClass.getDeclaredConstructor(BasicQuest.class).newInstance(base);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException ignored) {
        }

        if (questInstance != null && questInstance.loadParameters(questSection, questIndex)) {
            QuestsLoader.questsGlobal.add(questInstance);
            return true;
        }
        return false;
    }
}
