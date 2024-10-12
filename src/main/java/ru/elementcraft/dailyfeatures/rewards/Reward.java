package ru.elementcraft.dailyfeatures.rewards;

import lombok.Getter;

import java.util.List;

public class Reward {


    @Getter
    private final RewardType rewardType;
    @Getter
    private final List<String> commands;
    @Getter
    private final String currencyLabel;
    @Getter
    private final String currencyDisplayName;
    @Getter
    private double amount;

    /**
     * Constructor for a reward with a command.
     * @param commands the reward-command.
     */
    public Reward(RewardType rewardType, List<String> commands) {
        this.rewardType = rewardType;
        this.commands = commands;

        this.currencyLabel = null;
        this.currencyDisplayName = null;
    }

    /**
     * Constructor for other reward.
     * @param amount the reward amount.
     */
    public Reward(RewardType rewardType, double amount) {
        this.rewardType = rewardType;
        this.amount = amount;

        this.commands = null;
        this.currencyLabel = null;
        this.currencyDisplayName = null;
    }

    /**
     * Constructor for a reward that is using CoinsEngine.
     * @param currencyLabel the reward-currency, by its name in the configuration.
     * @param currencyDisplayName the name of the currency that will be displayed to the player.
     * @param amount the reward amount.
     */
    public Reward(RewardType rewardType, String currencyLabel, String currencyDisplayName, int amount) {
        this.rewardType = rewardType;
        this.currencyLabel = currencyLabel;
        this.currencyDisplayName = currencyDisplayName;
        this.amount = amount;

        this.commands = null;
    }
}
