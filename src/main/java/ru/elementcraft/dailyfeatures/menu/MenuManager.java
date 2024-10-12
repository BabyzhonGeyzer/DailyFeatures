package ru.elementcraft.dailyfeatures.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.boomearo.menuinv.api.*;
import ru.boomearo.menuinv.api.icon.IconBuilder;
import ru.boomearo.menuinv.api.session.InventorySessionImpl;
import ru.elementcraft.dailyfeatures.DailyFeatures;
import ru.elementcraft.dailyfeatures.configuration.units.GlobalReward;
import ru.elementcraft.dailyfeatures.player.PlayerQuests;
import ru.elementcraft.dailyfeatures.player.progression.Progression;
import ru.elementcraft.dailyfeatures.quests.types.AbstractQuest;
import ru.elementcraft.dailyfeatures.tools.ColorConvert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuManager {

    private static String PREFIX = "Quests";

    public static void setupMenu(Plugin plugin) {
        if (PREFIX == null) PREFIX = ColorConvert.convertColorCode(plugin.getConfig().getString("prefix"));
        Menu.registerPages(plugin)
                .createTemplatePage(MenuPage.MAIN)
                .setMenuType(MenuType.CHEST_9X5)
                .setInventoryTitle((inventoryPage) -> PREFIX)
                .setImmutableIcon(19, new IconBuilder()
                        .setIconUpdate((inventoryPage, player) -> getIcon(inventoryPage, 0)))


                .setImmutableIcon(22, new IconBuilder()
                        .setIconUpdate((inventoryPage, player) -> getIcon(inventoryPage, 1)))

                .setImmutableIcon(25, new IconBuilder()
                        .setIconUpdate((inventoryPage, player) -> {
                            PlayerQuests session = ((QuestInventorySession) inventoryPage.getSession()).getQuestData();
                            ItemStack stack = new ItemStack(GlobalReward.getMenuItem());
                            ItemMeta meta = stack.getItemMeta();
                            List<String> desc = meta.getLore();
                            desc.add("");
                            desc.add(GlobalReward.getCountPrefix() + session.getAchievedQuests() + " / " + 2);
                            meta.setLore(desc);
                            stack.setItemMeta(meta);
                            return stack;
                        }));




    }

    private static ItemStack getIcon(InventoryPage inventoryPage, int id) {
        if (inventoryPage.getSession() instanceof QuestInventorySession) {
            PlayerQuests session = ((QuestInventorySession) inventoryPage.getSession()).getQuestData();
            AbstractQuest quest = session.getPlayerQuests().keySet().stream().toList().get(id);
            Progression progression = session.getPlayerQuests().get(quest);
            ItemStack stack = new ItemStack(quest.getMenuItem());
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(quest.getQuestName());

            List<String> desc = new ArrayList<>(List.copyOf(quest.getQuestDesc()));
            desc.add("");
            desc.add(ColorConvert.convertColorCode(getProgress(progression, quest).toString()));
            meta.setLore(desc);


            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            stack.setItemMeta(meta);
            return stack;
        }
        return new ItemStack(Material.DIRT);
    }

    @NotNull
    private static StringBuilder getProgress(Progression progression, AbstractQuest quest) {
        StringBuilder builder = new StringBuilder();

        if (!progression.isAchieved()) {
            for (int i = 0; i < 10; i++) {
                if (i < 10F * ((float) progression.getProgression() / (float) quest.getAmountRequired())) {
                    builder.append("&2|");
                }
                else {
                    builder.append("&4|");
                }
            }

        }
        else {
            builder.append("&2||||||||||");
        }
        return builder;
    }

    @RequiredArgsConstructor
    @Getter
    public enum MenuPage implements PluginPage {

        MAIN(DailyFeatures.INSTANCE, "main");

        private final Plugin plugin;
        private final String page;

    }



    @Getter
    public static class QuestInventorySession extends InventorySessionImpl {
        private final PlayerQuests questData;
        public QuestInventorySession(PlayerQuests questData) {
            this.questData = questData;
        }
    }
}
