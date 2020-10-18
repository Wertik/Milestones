package space.devport.wertik.milestones.system.action.struct.impl;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.system.action.struct.AbstractActionListener;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.ConditionRegistry;

import java.util.Arrays;
import java.util.List;

public class WorldActionListener extends AbstractActionListener {

    public WorldActionListener(MilestonesPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onWorldEnter(PlayerChangedWorldEvent event) {
        ActionContext context = new ActionContext();
        context.fromPlayer(event.getPlayer());
        handle("world_enter", event.getPlayer(), context);

        ActionContext leaveContext = new ActionContext();
        leaveContext.fromPlayer(event.getPlayer())
                .add(event.getFrom());
        handle("world_leave", event.getPlayer(), context);
    }

    @Override
    public void registerConditionLoaders(ConditionRegistry registry) {
        // Blank, base conditions are fine here
    }

    @Override
    public List<String> getRegisteredActions() {
        return Arrays.asList("world_enter", "world_leave");
    }
}
