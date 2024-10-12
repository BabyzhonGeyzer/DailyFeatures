package ru.elementcraft.dailyfeatures;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.boomearo.menuinv.api.Menu;
import ru.elementcraft.dailyfeatures.api.DailyFeaturesAPI;
import ru.elementcraft.dailyfeatures.api.quests.QuestTypeRegistry;
import ru.elementcraft.dailyfeatures.events.EventsManager;
import ru.elementcraft.dailyfeatures.menu.MenuManager;
import ru.elementcraft.dailyfeatures.player.QuestsManager;
import ru.elementcraft.dailyfeatures.player.progression.listeners.AllQuestsCompletedListener;
import ru.elementcraft.dailyfeatures.player.progression.listeners.QuestCompletedListener;
import ru.elementcraft.dailyfeatures.player.progression.storage.H2Manager;
import ru.elementcraft.dailyfeatures.player.progression.storage.SQLManager;
import ru.elementcraft.dailyfeatures.quests.QuestsLoader;
import ru.elementcraft.dailyfeatures.quests.entity.KillQuest;
import ru.elementcraft.dailyfeatures.quests.item.BreakQuest;
import ru.elementcraft.dailyfeatures.tools.VaultHook;

import java.io.File;
import java.io.IOException;

public final class DailyFeatures extends JavaPlugin {
    public static DailyFeatures INSTANCE;
    @Getter
    private DailyFeaturesAPI API;


    private LiteCommands<CommandSender> liteCommands;


    @Getter
    private SQLManager sqlManager;
    @Getter
    private FileConfiguration questsConfiguration;


    @Getter
    private QuestsLoader questsLoader;
    @Getter
    private ReloadService reloadService;


    @Override
    public void onEnable() {
        INSTANCE = this;
        API = new DailyFeaturesAPI();


        // LiteCommands Init
        this.liteCommands = LiteBukkitFactory.builder("dailyfeatures",this)
                .commands(new DailyFeaturesRootCommand())
                .argumentSuggestion(String.class, ArgumentKey.of("mode"), SuggestionResult.of("quests", "total"))
                .build();


        // MenuInv Init
        Menu.initMenu(this);
        MenuManager.setupMenu(this);


        // QuestLoader Init
        questsLoader = new QuestsLoader();


        //Config Init
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            getLogger().info("Config not found, creating a new one...");
            saveDefaultConfig();
        }

        // Quests File Init
        File questFile = new File(getDataFolder(), "quests.yml");
        if (!questFile.exists()) {
            getLogger().info("quests.yml not found, creating a new one...");
            saveResource("quests.yml", false);
        }

        // Quests Config Init
        questsConfiguration = new YamlConfiguration();
        try {
            questsConfiguration.load(questFile);
        } catch (InvalidConfigurationException | IOException ignored) {}


        // Load SQL Support
        this.sqlManager = new H2Manager();


        // Load class instance
        this.reloadService = new ReloadService(this);


        // Vault Hook Init
        if (VaultHook.setupEconomy()) System.out.println("Vault successfully hooked.");


        // Register Quest Types
        final QuestTypeRegistry questTypeRegistry = API.getQuestTypeRegistry();


        // Entity Quests Type Init
        questTypeRegistry.registerQuestType("KILL", KillQuest.class);

        // Item Quests Type Init
        questTypeRegistry.registerQuestType("BREAK", BreakQuest.class);


        // Load All Elements
        reloadService.reload();


        // Load listeners
        new EventsManager(this).registerListeners();


        // Register plugin events
        getServer().getPluginManager().registerEvents(new QuestsManager(this), this);
        getServer().getPluginManager().registerEvents(new QuestCompletedListener(), this);
        getServer().getPluginManager().registerEvents(new AllQuestsCompletedListener(), this);

    }

    @Override
    public void onDisable() {
        // MenuInv Disable
        Menu.unloadMenu(this);
        reloadService.saveConnectedPlayerQuests(false);


        //LiteCommands Disable
        if (this.liteCommands != null)
            this.liteCommands.unregister();
    }
}
