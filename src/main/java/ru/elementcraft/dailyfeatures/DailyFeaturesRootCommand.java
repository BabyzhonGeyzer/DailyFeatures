package ru.elementcraft.dailyfeatures;


import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.boomearo.menuinv.api.Menu;
import ru.elementcraft.dailyfeatures.menu.MenuManager;
import ru.elementcraft.dailyfeatures.menu.MenuManager.MenuPage;
import ru.elementcraft.dailyfeatures.player.PlayerQuests;
import ru.elementcraft.dailyfeatures.player.QuestsManager;
import ru.elementcraft.dailyfeatures.quests.QuestLoaderUtils;

import java.util.HashMap;

@RootCommand
public class DailyFeaturesRootCommand {



    @Execute(name = "daily")
    @Permission("elementtestplugin.daily")
    void executeDaily(@Context CommandSender sender) {
        sender.sendMessage("execute Daily");



        if (sender instanceof Player) {
//            for (AbstractQuest q : QuestsManager.getActiveQuests().get("MordaSobaki").getPlayerQuests().keySet()) {
//
//                Progression pr = QuestsManager.getActiveQuests().get("MordaSobaki").getPlayerQuests().get(q);
//                sender.sendMessage(q.getQuestName() + pr.getProgression());
//            }
            Menu.open(MenuPage.MAIN, (Player) sender, new MenuManager.QuestInventorySession(QuestsManager.getActiveQuests().get(sender.getName())));
        }
        else
            sender.sendMessage("U're the console");

    }


    @Execute(name = "ElementTestPlugin reload")
    @Permission("elementtestplugin.reload")
//    @Async
    void executeReload(@Context CommandSender sender) {
        DailyFeatures.INSTANCE.getReloadService().reload();
        sender.sendMessage(ChatColor.GREEN + "Plugin successfully reloaded!");
    }

    @Execute(name = "ElementTestPlugin reset")
    @Permission("elementtestplugin.reset")
//    @Async
    void executeReset(@Arg Player target, @Arg("mode") String mode) {

        if (mode.equalsIgnoreCase("quests")) {
            final String playerName = target.getName();
            final HashMap<String, PlayerQuests> activeQuests = QuestsManager.getActiveQuests();
            int totalAchievedQuests = activeQuests.get(playerName).getTotalAchievedQuests();
            QuestLoaderUtils.loadNewPlayerQuests(playerName, QuestsManager.getActiveQuests(), totalAchievedQuests);

        }
        else if (mode.equalsIgnoreCase("total")) {
            QuestsManager.getActiveQuests().get(target.getName()).setTotalAchievedQuests(0);
        }
    }


}
