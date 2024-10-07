package ru.elementcraft.dailyfeatures;


import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RootCommand
public class DailyFeaturesRootCommand {

    @Execute(name = "daily")
    void executeDaily(@Context CommandSender sender) {
        sender.sendMessage("execute Daily");
    }


    @Execute(name = "ElementTestPlugin reload")
    void executeReload(@Context CommandSender sender) {
        sender.sendMessage("execute Reload");
    }

    @Execute(name = "ElementTestPlugin reset")
    void executeReset(@Context CommandSender sender, @Arg Player p) {
        sender.sendMessage("execute Reset " + p.toString());
    }

}
