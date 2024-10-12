package ru.elementcraft.dailyfeatures.configuration.units;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.elementcraft.dailyfeatures.tools.ColorConvert;

public class Title {


    private final FileConfiguration config;

    public Title(FileConfiguration configurationFiles) {
        this.config = configurationFiles;
    }



    private static boolean isEnabled;
    private static int fadeIn;
    private static int fadeOut;
    private static int stay;
    private static String title;
    private static String subtitle;

    public void loadTitle() {
        ConfigurationSection section = config.getConfigurationSection("title");
        isEnabled = section.getBoolean("enabled");

        if (isEnabled) {
            fadeIn = section.getInt("fadeIn");
            stay = section.getInt("stay");
            fadeOut = section.getInt("fadeOut");
            title = ColorConvert.convertColorCode(ChatColor.translateAlternateColorCodes('&', section.getString( "text")));
            subtitle = ColorConvert.convertColorCode(ChatColor.translateAlternateColorCodes('&', section.getString("subtitle")));

        }
    }

    /**
     * Send title to player.
     * @param player player to send the title.
     * @param questName name of the achieved quest.
     */
    public static void sendTitle(Player player, String questName) {
        if (isEnabled) {
            player.sendTitle(
                    title.replace("%player%", player.getDisplayName()).replace("%questName%", questName),
                    subtitle.replace("%player%", player.getDisplayName()).replace("%questName%", questName),
                    fadeIn, stay, fadeOut);
        }
    }
}
