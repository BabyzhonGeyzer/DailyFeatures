package ru.elementcraft.dailyfeatures.player.progression.storage;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.*;

public abstract class SQLManager {

    protected SessionFactory sessionFactory;

    /**
     *  Get load progression SQL instance.
     */
    @Getter
    protected LoadProgressionSQL loadProgressionSQL;
    /**
     *  Get save progression SQL instance.
     */
    @Getter
    protected SaveProgressionSQL saveProgressionSQL;

    public void setupTables() {
        final Session session = getConnection();
        session.doWork(connection -> {

            if (!tableExists(connection, "PLAYER")) {

                String str = "create table PLAYER\n" +
                        "  (\n" +
                        "     PLAYERNAME char(32)  not null  ,\n" +
                        "     PLAYERTIMESTAMP bigint not null,  \n" +
                        "     ACHIEVEDQUESTS tinyint not null, \n" +
                        "     TOTALACHIEVEDQUESTS int not null, \n" +
                        "     constraint PK_PLAYER primary key (PLAYERNAME)\n" +
                        "  );";

                PreparedStatement preparedStatement = connection.prepareStatement(str);
                preparedStatement.execute();

                preparedStatement.close();
            }
            if (!tableExists(connection, "PROGRESSION")) {

                String str = "create table PROGRESSION\n" +
                        "  (\n" +
                        "     PRIMARYKEY int auto_increment  ,\n" +
                        "     PLAYERNAME char(32)  not null  ,\n" +
                        "     PLAYERQUESTID smallint  not null  ,\n" +
                        "     QUESTINDEX int  not null  ,\n" +
                        "     ADVANCEMENT int  not null  ,\n" +
                        "     ISACHIEVED bit  not null  ,\n" +
                        "     primary key (PRIMARYKEY) ,\n" +
                        "     constraint UNIQUE_PLAYERNAME_PLAYERQUESTID unique (PLAYERNAME, PLAYERQUESTID)" +
                        "  ); ";

                PreparedStatement preparedStatement = connection.prepareStatement(str);
                preparedStatement.execute();

                preparedStatement.close();
            }

        });
        session.close();
    }

    /**
     * Check if a table exists in database.
     * @param connection connection to check.
     * @param tableName  name of the table to check.
     * @return true if table exists.
     * @throws SQLException SQL errors.
     */
    private static boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName, new String[]{"TABLE"});

        return resultSet.next();
    }


    /**
     * Get database connection.
     */
    public Session getConnection() {
        if (this.sessionFactory != null && !this.sessionFactory.isClosed()) {
            return this.sessionFactory.openSession();
        }
        return null;
    }

    /**
     * Test database connection.
     */
    protected void testConnection() {
        Session con = getConnection();
        if (con.isConnected()) {
            con.close();
        }
    }

}
