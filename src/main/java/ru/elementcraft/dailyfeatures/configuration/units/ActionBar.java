package ru.elementcraft.dailyfeatures.configuration.units;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.elementcraft.dailyfeatures.tools.ColorConvert;

public class ActionBar {

    private final FileConfiguration config;
    public ActionBar(FileConfiguration configurationFiles) {
        this.config = configurationFiles;
    }


    private static boolean isEnabled;
    private static String text;
    public void loadActionbar() {
        ConfigurationSection section = config.getConfigurationSection("actionbar");
        isEnabled = section.getBoolean("enabled");

        if (isEnabled) {
            text = ColorConvert.convertColorCode(ChatColor.translateAlternateColorCodes('&', section.getString("text")));
        }
    }

    /**
     * Send actionbar.
     *
     * @param player    to send.
     * @param questName name of the achieved quest.
     */
    public static void sendActionbar(Player player, String questName) {
        if (isEnabled) {

            final String toSend = ColorConvert.convertColorCode(text
                    .replace("%player%", player.getDisplayName())
                    .replace("%questName%", questName)
            );

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(toSend));
        }
    }
}
