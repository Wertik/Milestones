package space.devport.wertik.milestones.system.milestone.struct.condition.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import space.devport.utils.configuration.Configuration;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.AbstractCondition;

import java.util.ArrayList;
import java.util.List;

public class MessageCondition extends AbstractCondition {

    private final List<String> allowedMessages = new ArrayList<>();

    @Override
    public boolean onCheck(Player player, ActionContext context) {
        String message = context.get(String.class);
        return allowedMessages.isEmpty() || message != null && allowedMessages.contains(message);
    }

    @Override
    public void onLoad(Configuration configuration, ConfigurationSection section) {
        this.allowedMessages.addAll(section.getStringList("messages"));
    }
}
