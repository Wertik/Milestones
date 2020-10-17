package space.devport.wertik.milestones.system.action.struct.impl;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.system.action.struct.AbstractActionListener;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.ConditionRegistry;
import space.devport.wertik.milestones.system.milestone.struct.condition.impl.BlockCondition;

public class BlockActionListener extends AbstractActionListener {

    public BlockActionListener(MilestonesPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            ActionContext context = new ActionContext();
            context.fromBlock(event.getBlock())
                    .fromPlayer(event.getPlayer());
            handle("break", event.getPlayer(), context);
        });
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            ActionContext context = new ActionContext();
            context.fromBlock(event.getBlock())
                    .fromPlayer(event.getPlayer());
            handle("place", event.getPlayer(), context);
        });
    }

    @Override
    public void registerConditionLoaders(ConditionRegistry registry) {
        registry.setInstanceCreator("break", BlockCondition::new);
    }
}