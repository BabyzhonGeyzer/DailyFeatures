package ru.elementcraft.dailyfeatures.rewards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.elementcraft.dailyfeatures.configuration.units.ActionBar;
import ru.elementcraft.dailyfeatures.configuration.units.Title;
import ru.elementcraft.dailyfeatures.tools.ColorConvert;
import ru.elementcraft.dailyfeatures.tools.VaultHook;

public class RewardManager {

    public static void sendAllRewardItems(String questName, Player player, Reward reward) {


        Title.sendTitle(player, questName);
        ActionBar.sendActionbar(player, questName);

        sendQuestReward(player, reward);
    }

    /**
     * Give quest-reward to player.
     * @param player to give the reward.
     * @param reward     quest reward.
     */
    public static void sendQuestReward(Player player, Reward reward) {
        if (reward.getRewardType() == RewardType.NONE) return;


        switch (reward.getRewardType()) {

            case COMMAND -> {
                for (String cmd : reward.getCommands()) {
                    cmd = ColorConvert.convertColorCode(cmd);
                    String finalCmd = cmd;
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), finalCmd.replace("%player%", player.getName()));
                }
            }
            case EXP_LEVELS -> {
                player.giveExpLevels((int) reward.getAmount());
            }

            case EXP_POINTS -> {
                player.giveExp((int) reward.getAmount());
            }

            case MONEY -> {
                if (VaultHook.getEconomy() != null) {
                    VaultHook.getEconomy().depositPlayer(player, reward.getAmount());

                }
            }
        }
    }
}
