package ru.elementcraft.dailyfeatures.player.progression.storage;

import org.h2.Driver;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.H2Dialect;

public class H2Manager extends SQLManager {


    public H2Manager() {

        super.loadProgressionSQL = new LoadProgressionSQL(this);
        super.saveProgressionSQL = new SaveProgressionSQL(this);

        setupDatabase();
    }

    /**
     * Init database.
     */
    public void setupDatabase() {
        initH2();
        testConnection();
        setupTables();
    }

    private void initH2() {
        Configuration config = new Configuration();
        config.setProperty("hibernate.connection.driver_class", Driver.class);
        config.setProperty("hibernate.connection.url", "jdbc:h2:./plugins/DailyFeatures/database");
        config.setProperty("hibernate.connection.username", "odq");
        config.setProperty("hibernate.c3p0.max_size", 100);
        config.setProperty("hibernate.c3p0.timeout", 60000);
        config.setProperty("hibernate.dialect", H2Dialect.class);

        super.sessionFactory = config.buildSessionFactory();
    }
}