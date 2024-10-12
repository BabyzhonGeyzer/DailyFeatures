package ru.elementcraft.dailyfeatures.quests.types.shared;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import ru.elementcraft.dailyfeatures.quests.types.AbstractQuest;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityQuest extends AbstractQuest {

    private static final String TYPE_PATH = ".required_entity";
    protected final List<EntityType> requiredEntities;

    protected EntityQuest(BasicQuest base) {
        super(base);
        this.requiredEntities = new ArrayList<>();
    }

    @Override
    public boolean loadParameters(ConfigurationSection section, String index) {
        if (!section.contains(TYPE_PATH)) return true;

        if (section.isString(TYPE_PATH)) {
            final EntityType entityType = getEntityType(section.getString(TYPE_PATH));
            if (entityType != null) requiredEntities.add(entityType);
            else return false;
        } else {
            for (String presumedEntity : section.getStringList(TYPE_PATH)) {
                final EntityType entityType = getEntityType(presumedEntity);
                if (entityType != null) requiredEntities.add(entityType);
                else return false;
            }
        }

        return true;
    }

    /**
     * Get the entity type.
     *
     * @param value the value
     * @return the entity type, or null if the entity type is invalid
     */
    private EntityType getEntityType(String value) {
        try {
            return EntityType.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Check if the entity is required by the quest.
     *
     * @param entityType the entity type
     * @return true if the entity is required, false otherwise
     */
    public boolean isRequiredEntity(EntityType entityType) {
        return requiredEntities == null || requiredEntities.isEmpty() || requiredEntities.contains(entityType);
    }
}
