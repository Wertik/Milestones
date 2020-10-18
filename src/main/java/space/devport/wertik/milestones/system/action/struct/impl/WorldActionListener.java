package space.devport.wertik.milestones.system.action.struct.impl;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.system.action.struct.AbstractActionListener;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.ConditionRegistry;
import space.devport.wertik.milestones.system.milestone.struct.condition.impl.WorldLeaveCondition;

import java.util.Arrays;
import java.util.List;

public class WorldActionListener extends AbstractActionListener {

    public WorldActionListener(MilestonesPlugin plugin) {
        super(plugin);
    }

    @Override
    public List<String> getRegisteredActions() {
        return Arrays.asList("world_enter", "world_leave");
    }

    @Override
    public void registerConditions(ConditionRegistry registry) {
        registry.setInstanceCreator("world_leave", WorldLeaveCondition::new);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        ActionContext context = new ActionContext();
        context.fromPlayer(event.getPlayer());
        handle("world_enter", event.getPlayer(), context);

        ActionContext leaveContext = new ActionContext();
        leaveContext.fromPlayer(event.getPlayer())
                .add(event.getFrom());
        handle("world_leave", event.getPlayer(), context);
    }
}
