package ru.elementcraft.dailyfeatures;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.elementcraft.dailyfeatures.configuration.units.ActionBar;
import ru.elementcraft.dailyfeatures.configuration.units.Temporality;
import ru.elementcraft.dailyfeatures.configuration.units.Title;
import ru.elementcraft.dailyfeatures.player.QuestsManager;
import ru.elementcraft.dailyfeatures.player.progression.storage.SQLManager;
import ru.elementcraft.dailyfeatures.quests.QuestsLoader;
import ru.elementcraft.dailyfeatures.configuration.units.GlobalReward;

import java.util.HashSet;
import java.util.Set;

public class ReloadService {

    private final DailyFeatures dailyFeatures;
    private final QuestsLoader questsLoader;
    private final SQLManager sqlManager;

    /**
     * Constructor.
     *
     * @param dailyFeatures main class instance.
     */
    public ReloadService(DailyFeatures dailyFeatures) {
        this.dailyFeatures = dailyFeatures;
        this.sqlManager = dailyFeatures.getSqlManager();
        this.questsLoader = dailyFeatures.getQuestsLoader();
    }

    /**
     * Load all quests from connected players, to avoid errors on reload.
     */
    public void loadConnectedPlayerQuests() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!QuestsManager.getActiveQuests().containsKey(player.getName())) {
                loadQuestsForPlayer(player);
            }
        }
    }

    /**
     * Load quests for a specific player.
     *
     * @param player player to load quests for.
     */
    private void loadQuestsForPlayer(Player player) {

        sqlManager.getLoadProgressionSQL().loadProgression(player.getName(), QuestsManager.getActiveQuests());
    }

    /**
     * Save all quests from connected players, to avoid errors on reload.
     */
    public void saveConnectedPlayerQuests(boolean isAsync) {

        final Set<String> playersToRemove = new HashSet<>();

        for (String player : QuestsManager.getActiveQuests().keySet()) {
            sqlManager.getSaveProgressionSQL().saveProgression(player, QuestsManager.getActiveQuests().get(player), isAsync);
            playersToRemove.add(player);
        }

        for (String player : playersToRemove) {
            QuestsManager.getActiveQuests().remove(player);
        }
    }

    /**
     * Execute all required actions when the command /qadmin reload is performed.
     */
    public void reload() {

        // Reload Config
        dailyFeatures.reloadConfig();

        // Load specific settings
        new Temporality(dailyFeatures.getConfig()).loadTemporalitySettings();
        new ActionBar(dailyFeatures.getConfig()).loadActionbar();
        new Title(dailyFeatures.getConfig()).loadTitle();
        new GlobalReward(dailyFeatures.getConfig()).initGlobalReward();

        // Load quests & interface
        questsLoader.loadQuests(dailyFeatures.getQuestsConfiguration());

        saveConnectedPlayerQuests(true);
        new BukkitRunnable() {
            @Override
            public void run() {
                loadConnectedPlayerQuests();
            }
        }.runTaskLaterAsynchronously(dailyFeatures, 20L);
    }
}
