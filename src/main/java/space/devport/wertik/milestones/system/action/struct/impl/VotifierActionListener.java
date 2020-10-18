package space.devport.wertik.milestones.system.action.struct.impl;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.system.action.struct.AbstractActionListener;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.ConditionRegistry;
import space.devport.wertik.milestones.system.milestone.struct.condition.impl.VoteCondition;

import java.util.Collections;
import java.util.List;

public class VotifierActionListener extends AbstractActionListener {

    public VotifierActionListener(MilestonesPlugin plugin) {
        super(plugin);
    }

    @Override
    public List<String> getRegisteredActions() {
        return Collections.singletonList("vote");
    }

    @Override
    public void registerConditions(ConditionRegistry registry) {
        registry.setInstanceCreator("vote", VoteCondition::new);
    }

    @Override
    public boolean canRegister() {
        return plugin.getPluginManager().isPluginEnabled("Votifier");
    }

    @EventHandler
    public void onVote(VotifierEvent event) {
        Vote vote = event.getVote();

        Player player = Bukkit.getPlayer(vote.getUsername());

        // Only on online votes
        if (player == null)
            return;

        ActionContext context = new ActionContext();
        context.add(vote)
                .fromPlayer(player);
        handle("vote", player, context);
    }
}
