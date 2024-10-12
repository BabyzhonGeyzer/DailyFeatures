package ru.elementcraft.dailyfeatures.quests.item;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import ru.elementcraft.dailyfeatures.quests.types.shared.BasicQuest;
import ru.elementcraft.dailyfeatures.quests.types.shared.ItemQuest;

public class BreakQuest extends ItemQuest {

    public BreakQuest(BasicQuest base) {
        super(base);
    }

    @Override
    public String getType() {
        return "BREAK";
    }

    @Override
    public boolean canProgress(Event provided) {
        if (provided instanceof BlockBreakEvent event) {
            final Block block = event.getBlock();
            Material material = block.getType();

            if (!material.isItem()) {
                return false;
            }

            return super.isRequiredItem(new ItemStack(material));
        }

        return false;
    }
}
