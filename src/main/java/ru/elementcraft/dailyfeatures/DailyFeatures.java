package ru.elementcraft.dailyfeatures;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class DailyFeatures extends JavaPlugin {
    private static final String FALLBACK_PREFIX = "dailyfeatures";
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        this.liteCommands = LiteBukkitFactory.builder(FALLBACK_PREFIX, this)
                .commands(new DailyFeaturesRootCommand())
                .build();
    }

    @Override
    public void onDisable() {
        if (this.liteCommands != null) this.liteCommands.unregister();
    }
}
