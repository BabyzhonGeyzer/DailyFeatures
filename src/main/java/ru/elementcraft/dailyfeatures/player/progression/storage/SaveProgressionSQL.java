package ru.elementcraft.dailyfeatures.player.progression.storage;

import org.bukkit.scheduler.BukkitRunnable;
import org.hibernate.Session;
import ru.elementcraft.dailyfeatures.DailyFeatures;
import ru.elementcraft.dailyfeatures.player.PlayerQuests;
import ru.elementcraft.dailyfeatures.player.progression.Progression;
import ru.elementcraft.dailyfeatures.quests.types.AbstractQuest;

import java.sql.PreparedStatement;
import java.util.LinkedHashMap;

public class SaveProgressionSQL {

    private final SQLManager sqlManager;

    /**
     * @param sqlManager instance of MySQLManager.
     */
    public SaveProgressionSQL(SQLManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    /* requests */

    private final String H2_PLAYER_QUERY =
            "MERGE INTO PLAYER (PLAYERNAME, PLAYERTIMESTAMP, ACHIEVEDQUESTS, TOTALACHIEVEDQUESTS) " +
                    "KEY (PLAYERNAME) VALUES (?, ?, ?, ?)";

    private final String H2_PROGRESS_UPDATE =
            "MERGE INTO PROGRESSION (PLAYERNAME, PLAYERQUESTID, QUESTINDEX, ADVANCEMENT, ISACHIEVED) " +
                    "KEY (PLAYERNAME, PLAYERQUESTID) VALUES (?, ?, ?, ?, ?)";

    /**
     * Save player quests progression.
     * @param playerName   name of the player.
     * @param playerQuests player quests.
     */
    public void saveProgression(String playerName, PlayerQuests playerQuests, boolean isAsync) {
        if (playerQuests == null) return;
        System.out.println("Save progression");


        long timestamp = playerQuests.getTimestamp();
        int achievedQuests = playerQuests.getAchievedQuests();
        int totalAchievedQuests = playerQuests.getTotalAchievedQuests();

        final LinkedHashMap<AbstractQuest, Progression> quests = playerQuests.getPlayerQuests();

        if (isAsync) {

            new BukkitRunnable() {
                @Override
                public void run() {
                    saveDatas(playerName, timestamp, achievedQuests, totalAchievedQuests, quests);
                }
            }.runTaskAsynchronously(DailyFeatures.INSTANCE);
        } else {
            saveDatas(playerName, timestamp, achievedQuests, totalAchievedQuests, quests);
        }
    }

    /**
     * Save player quests progression.
     * @param playerName          name of the player.
     * @param timestamp           timestamp.
     * @param achievedQuests      achieved quests.
     * @param totalAchievedQuests total achieved quests.
     * @param quests              quests.
     */
    private void saveDatas(String playerName, long timestamp, int achievedQuests, int totalAchievedQuests, LinkedHashMap<AbstractQuest, Progression> quests) {
        final Session session = sqlManager.getConnection();
        session.doWork(connection -> {
            PreparedStatement playerStatement;
            playerStatement = connection.prepareStatement(H2_PLAYER_QUERY);

            playerStatement.setString(1, playerName);
            playerStatement.setLong(2, timestamp);
            playerStatement.setInt(3, achievedQuests);
            playerStatement.setInt(4, totalAchievedQuests);

            playerStatement.executeUpdate();


            int index = 0;
            for (AbstractQuest quest : quests.keySet()) {
                PreparedStatement progressionStatement;
                progressionStatement = connection.prepareStatement(H2_PROGRESS_UPDATE);

                progressionStatement.setString(1, playerName);
                progressionStatement.setInt(2, index);
                progressionStatement.setInt(3, quest.getQuestIndex());
                progressionStatement.setInt(4, quests.get(quest).getProgression());
                progressionStatement.setBoolean(5, quests.get(quest).isAchieved());

                progressionStatement.executeUpdate();


                index++;
            }

        });
        session.close();
    }
}
