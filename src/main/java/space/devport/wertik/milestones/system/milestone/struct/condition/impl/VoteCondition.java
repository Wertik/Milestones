package space.devport.wertik.milestones.system.milestone.struct.condition.impl;

import com.vexsoftware.votifier.model.Vote;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import space.devport.utils.configuration.Configuration;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.AbstractCondition;

import java.util.HashSet;
import java.util.Set;

public class VoteCondition extends AbstractCondition {

    private final Set<String> allowedServices = new HashSet<>();

    @Override
    public boolean onCheck(Player player, ActionContext context) {
        Vote vote = context.get(Vote.class);
        return allowedServices.isEmpty() || vote != null && allowedServices.contains(vote.getServiceName());
    }

    @Override
    public void onLoad(Configuration configuration, ConfigurationSection section) {
        this.allowedServices.addAll(section.getStringList("services"));
    }
}
