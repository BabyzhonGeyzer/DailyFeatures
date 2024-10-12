package ru.elementcraft.dailyfeatures.configuration.units;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.elementcraft.dailyfeatures.rewards.Reward;
import ru.elementcraft.dailyfeatures.rewards.RewardLoader;
import ru.elementcraft.dailyfeatures.rewards.RewardManager;
import ru.elementcraft.dailyfeatures.rewards.RewardType;
import ru.elementcraft.dailyfeatures.tools.ColorConvert;

import java.util.ArrayList;
import java.util.List;

public class GlobalReward extends RewardLoader {


    private final FileConfiguration config;

    public GlobalReward(FileConfiguration configurationFiles) {
        this.config = configurationFiles;
    }


    private static Reward globalReward;
    private static boolean isEnabled;

    @Getter
    private static ItemStack menuItem;


    @Getter
    private static String countPrefix;



    public void initGlobalReward() {
        final ConfigurationSection globalRewardSection = config.getConfigurationSection("global_reward");
        if (globalRewardSection == null) {
            isEnabled = false;
            return;
        }

        if (!globalRewardSection.contains("enabled")) {
            isEnabled = false;
            return;
        }

        isEnabled = globalRewardSection.getBoolean("enabled");

        if (isEnabled) {
            final RewardType rewardType = RewardType.valueOf(globalRewardSection.getString(".reward_type"));

            if (rewardType == RewardType.COMMAND) {
                globalReward = new Reward(rewardType, globalRewardSection.getStringList(".commands"));
            } else {
                globalReward = new Reward(rewardType, globalRewardSection.getInt(".amount"));
            }

            // Menu Item Setup
            menuItem = new ItemStack(Material.valueOf(globalRewardSection.getString(".menu_item")));
            ItemMeta meta = menuItem.getItemMeta();
            meta.setDisplayName(ColorConvert.convertColorCode(globalRewardSection.getString(".name")));

            List<String> desc = globalRewardSection.getStringList(".description");
            for (String string : desc) desc.set(desc.indexOf(string), ColorConvert.convertColorCode(string));

            meta.setLore(desc);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            menuItem.setItemMeta(meta);


            countPrefix = ColorConvert.convertColorCode(globalRewardSection.getString(".count_prefix"));


            globalReward = new RewardLoader().getRewardFromSection(globalRewardSection);

        }
    }

    /**
     * Give reward when players have completed all their quests.
     * @param playerName player name.
     */
    public static void sendGlobalReward(String playerName) {

        if (isEnabled) {
            final Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                return;
            }
            RewardManager.sendQuestReward(Bukkit.getPlayer(playerName), globalReward);
        }
    }

}
