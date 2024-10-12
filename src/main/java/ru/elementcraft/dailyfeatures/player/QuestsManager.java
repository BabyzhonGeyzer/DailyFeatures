package ru.elementcraft.dailyfeatures.player;

import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.elementcraft.dailyfeatures.DailyFeatures;
import ru.elementcraft.dailyfeatures.player.progression.Progression;
import ru.elementcraft.dailyfeatures.player.progression.storage.SQLManager;
import ru.elementcraft.dailyfeatures.quests.QuestsLoader;
import ru.elementcraft.dailyfeatures.quests.types.AbstractQuest;

import java.util.*;

public class QuestsManager implements Listener {

    private final SQLManager sqlManager;

    /**
     * Class instance constructor.
     * @param dailyFeatures main class instance.
     */
    public QuestsManager(DailyFeatures dailyFeatures) {
        this.sqlManager = dailyFeatures.getSqlManager();
    }

    @Getter
    private static final HashMap<String, PlayerQuests> activeQuests = new HashMap<>();


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        final String playerName = event.getPlayer().getName();


        if (!activeQuests.containsKey(playerName)) {

            sqlManager.getLoadProgressionSQL().loadProgression(playerName, activeQuests);
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();

        final PlayerQuests playerQuests = activeQuests.get(playerName);

        sqlManager.getSaveProgressionSQL().saveProgression(playerName, playerQuests, true);

        activeQuests.remove(playerName);

    }



    /** Select random quests. */
    public static LinkedHashMap<AbstractQuest, Progression> selectRandomQuests() {

        LinkedHashMap<AbstractQuest, Progression> quests = new LinkedHashMap<>();

        List<AbstractQuest> globalQuests = QuestsLoader.getQuestsGlobal();

        for (int i = 0; i <  2; i++) {
            final AbstractQuest quest = getRandomQuestForPlayer(quests.keySet(), globalQuests);
            final Progression progression = new Progression(0, false);
            quests.put(quest, progression);
        }

        return quests;
    }

    /**
     * Get a random quest that is not already in the player's quests.
     * @param currentQuests the player's current quests
     * @param availableQuests the available quests
     * @return a quest
     */
    public static AbstractQuest getRandomQuestForPlayer(Set<AbstractQuest> currentQuests, List<AbstractQuest> availableQuests) {
        AbstractQuest quest;
        do {
            quest = availableQuests.get(new Random().nextInt(availableQuests.size()));
        } while (currentQuests.contains(quest));
        return quest;
    }


}
