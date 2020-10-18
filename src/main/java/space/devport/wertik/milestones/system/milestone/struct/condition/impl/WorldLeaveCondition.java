package space.devport.wertik.milestones.system.milestone.struct.condition.impl;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import space.devport.utils.configuration.Configuration;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.AbstractCondition;

import java.util.HashSet;
import java.util.Set;

public class WorldLeaveCondition extends AbstractCondition {

    private final Set<String> allowedWorlds = new HashSet<>();

    @Override
    public boolean onCheck(Player player, ActionContext context) {
        World from = context.get(World.class);
        return allowedWorlds.isEmpty() || from != null && allowedWorlds.contains(from.getName());
    }

    @Override
    public void onLoad(Configuration configuration, ConfigurationSection section) {
        this.allowedWorlds.addAll(section.getStringList("from"));
    }
}
