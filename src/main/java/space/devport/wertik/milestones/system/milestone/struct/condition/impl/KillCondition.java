package space.devport.wertik.milestones.system.milestone.struct.condition.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.devport.utils.configuration.Configuration;
import space.devport.wertik.milestones.Utils;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.AbstractCondition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KillCondition extends AbstractCondition {

    private final Set<EntityType> allowedTypes = new HashSet<>();

    @Override
    public boolean onCheck(Player player, ActionContext context) {
        EntityType type = context.get(EntityType.class);
        return allowedTypes.isEmpty() || type != null && allowedTypes.contains(type);
    }

    @Override
    public @NotNull AbstractCondition onLoad(Configuration configuration, ConfigurationSection section) {
        List<String> typeNames = section.getStringList("entity-types");
        for (String typeName : typeNames) {
            EntityType type = Utils.parseEnum(typeName, EntityType.class);
            if (type != null)
                this.allowedTypes.add(type);
        }
        return this;
    }
}
