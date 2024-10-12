package ru.elementcraft.dailyfeatures.rewards;

import org.bukkit.configuration.ConfigurationSection;

public class RewardLoader {

    /**
     * Load a reward from a configuration section.
     * @param section configuration section.
     * @return reward.
     */
    public Reward getRewardFromSection(ConfigurationSection section) {

        RewardType rewardType;
        try {
            rewardType = RewardType.valueOf(section.getString(".reward_type"));
        } catch (Exception e) {
            rewardType = RewardType.NONE;
        }

        return switch (rewardType) {
            case NONE -> new Reward(RewardType.NONE, 0);
            case COMMAND -> new Reward(RewardType.COMMAND, section.getStringList(".commands"));

            default -> new Reward(rewardType, section.getDouble(".amount"));
        };
    }
}
