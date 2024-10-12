package ru.elementcraft.dailyfeatures.configuration.units;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Temporality {

    private final FileConfiguration config;

    public Temporality(FileConfiguration configurationFiles) {
        this.config = configurationFiles;
    }



    /** Get temporality mode. */
    @Getter
    private static int temporalityMode;
    private static String d;
    private static String h;
    private static String m;
    /** Get few seconds text. */
    @Getter
    private static String fewSeconds;

    public void loadTemporalitySettings() {
        temporalityMode = config.getInt("temporality_mode");

        final ConfigurationSection initials = config.getConfigurationSection("temporality_initials");

        d = initials.getString("days");
        h = initials.getString("hours");
        m = initials.getString("minutes");
        fewSeconds = initials.getString("few_seconds");
    }

    /** Get day initial. */
    public static String getDayInitial() {
        return d;
    }

    /** Get hour initial. */
    public static String getHourInitial() {
        return h;
    }

    /** Get minute initial. */
    public static String getMinuteInitial() {
        return m;
    }

}
