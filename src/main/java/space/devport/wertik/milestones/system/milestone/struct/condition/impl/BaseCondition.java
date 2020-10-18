package space.devport.wertik.milestones.system.milestone.struct.condition.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import space.devport.utils.configuration.Configuration;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.AbstractCondition;

/**
 * No extra requirements.
 */
public class BaseCondition extends AbstractCondition {

    @Override
    public boolean onCheck(Player player, ActionContext context) {
        return true;
    }

    @Override
    public void onLoad(Configuration configuration, ConfigurationSection section) {
    }
}
