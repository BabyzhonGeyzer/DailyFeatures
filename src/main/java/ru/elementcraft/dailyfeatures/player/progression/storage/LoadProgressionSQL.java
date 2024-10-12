package ru.elementcraft.dailyfeatures.player.progression.storage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.hibernate.Session;
import ru.elementcraft.dailyfeatures.DailyFeatures;
import ru.elementcraft.dailyfeatures.quests.QuestLoaderUtils;
import ru.elementcraft.dailyfeatures.player.PlayerQuests;
import ru.elementcraft.dailyfeatures.player.progression.Progression;
import ru.elementcraft.dailyfeatures.quests.types.AbstractQuest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LoadProgressionSQL {

    private final SQLManager sqlManager;


    /**
     * @param sqlManager instance of MySQLManager.
     */
    public LoadProgressionSQL(SQLManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    /**
     * Load player quests progression.
     * @param playerName name of the player.
     */
    public void loadProgression(String playerName, HashMap<String, PlayerQuests> activeQuests) {


        LinkedHashMap<AbstractQuest, Progression> quests = new LinkedHashMap<>();


        new BukkitRunnable() {
            @Override
            public void run() {

                AtomicBoolean hasStoredData = new AtomicBoolean(false);
                AtomicLong timestamp = new AtomicLong();
                AtomicInteger achievedQuests = new AtomicInteger();
                AtomicInteger totalAchievedQuests = new AtomicInteger();

                final Session session = sqlManager.getConnection();

                session.doWork(connection -> {
                    final String timestampQuery = "SELECT PLAYERTIMESTAMP,ACHIEVEDQUESTS,TOTALACHIEVEDQUESTS FROM PLAYER WHERE PLAYERNAME = ?";

                    final PreparedStatement preparedStatement = connection.prepareStatement(timestampQuery);
                    preparedStatement.setString(1, playerName);

                    final ResultSet resultSet = preparedStatement.executeQuery();


                    if (resultSet.next()) {
                        hasStoredData.set(true);
                        timestamp.set(resultSet.getLong("PLAYERTIMESTAMP"));
                        achievedQuests.set(resultSet.getInt("ACHIEVEDQUESTS"));
                        totalAchievedQuests.set(resultSet.getInt("TOTALACHIEVEDQUESTS"));
                    }

                    resultSet.close();
                    preparedStatement.close();
                });

                session.close();

                if (hasStoredData.get()) {
                    if (QuestLoaderUtils.checkTimestamp(timestamp.get())) {
                        QuestLoaderUtils.loadNewPlayerQuests(playerName, activeQuests, totalAchievedQuests.get());
                    } else {
                        loadPlayerQuests(playerName, quests);

                        PlayerQuests playerQuests = new PlayerQuests(timestamp.get(), quests);
                        playerQuests.setAchievedQuests(achievedQuests.get());
                        playerQuests.setTotalAchievedQuests(totalAchievedQuests.get());

                        final Player target = Bukkit.getPlayer(playerName);
                        if (target == null) return;

                        activeQuests.put(playerName, playerQuests);

                    }
                } else {
                    QuestLoaderUtils.loadNewPlayerQuests(playerName, activeQuests, 0);
                }


            }
        }.runTaskLaterAsynchronously(DailyFeatures.INSTANCE, 10L);
    }

    /**
     * Load player quests.
     * @param playerName player.
     * @param quests     list of player quests.
     */
    private void loadPlayerQuests(String playerName, LinkedHashMap<AbstractQuest, Progression> quests) {
        final Session session = sqlManager.getConnection();
        session.doWork(connection -> {
            final String getQuestProgressionQuery = "SELECT * FROM PROGRESSION WHERE PLAYERNAME = ?";

            final PreparedStatement preparedStatement = connection.prepareStatement(getQuestProgressionQuery);
            preparedStatement.setString(1, playerName);

            final ResultSet resultSet = preparedStatement.executeQuery();

            int id = 1;

            resultSet.next();
            do {
                int questIndex = resultSet.getInt("QUESTINDEX");
                int advancement = resultSet.getInt("ADVANCEMENT");
                boolean isAchieved = resultSet.getBoolean("ISACHIEVED");

                Progression progression = new Progression(advancement, isAchieved);
                AbstractQuest quest = QuestLoaderUtils.findQuest(questIndex);

                quests.put(quest, progression);

                id++;
            } while (resultSet.next() && id <= 2);

            resultSet.close();
            preparedStatement.close();
        });
        session.close();
    }
}