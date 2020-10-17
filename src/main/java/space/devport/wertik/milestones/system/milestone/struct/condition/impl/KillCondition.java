package space.devport.wertik.milestones.system.milestone.struct.condition.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.devport.utils.configuration.Configuration;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.AbstractCondition;

public class KillCondition extends AbstractCondition {

    @Override
    public boolean onCheck(Player player, ActionContext context) {
        return false;
    }

    @Override
    public @NotNull AbstractCondition onLoad(Configuration configuration, ConfigurationSection section) {
        return this;
    }
}
