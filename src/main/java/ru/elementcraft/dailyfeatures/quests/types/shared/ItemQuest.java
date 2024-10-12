package ru.elementcraft.dailyfeatures.quests.types.shared;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import ru.elementcraft.dailyfeatures.quests.types.AbstractQuest;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class ItemQuest extends AbstractQuest {

    private static final String TYPE_PATH = ".required_item";

    /**
     *  Get the required items.
     */
    private final List<ItemStack> requiredItems;

    /**
     * Quest constructor.
     * @param base parent quest.
     */
    protected ItemQuest(BasicQuest base) {
        super(base);
        this.requiredItems = new ArrayList<>();
    }

    public boolean isRequiredItem(ItemStack provided) {
        if (requiredItems == null || requiredItems.isEmpty()) return true;

        for (ItemStack item : requiredItems) {

            if (item.getType() == provided.getType()) {
                return true;
            }


            if (item.isSimilar(provided)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean loadParameters(ConfigurationSection section, String index) {
        if (!section.contains(TYPE_PATH)) return true;

        final List<String> requiredItemStrings = new ArrayList<>();
        if (section.isList(TYPE_PATH)) requiredItemStrings.addAll(section.getStringList(TYPE_PATH));
        else requiredItemStrings.add(section.getString(TYPE_PATH));

        for (String type : requiredItemStrings) {

            final ItemStack requiredItem = new ItemStack(Material.valueOf(type));
            requiredItems.add(requiredItem);
        }

        return true;
    }


}
