package space.devport.wertik.milestones.system.action.struct.impl;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;
import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.system.action.struct.AbstractActionListener;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.ConditionRegistry;
import space.devport.wertik.milestones.system.milestone.struct.condition.impl.BlockCondition;

import java.util.Arrays;
import java.util.List;

public class BlockActionListener extends AbstractActionListener {

    public BlockActionListener(MilestonesPlugin plugin) {
        super(plugin);
    }

    @Override
    public List<String> getRegisteredActions() {
        return Arrays.asList("break", "place", "harvest");
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        final Block block = event.getBlock();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            ActionContext context = new ActionContext();
            context.fromBlock(block)
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

    @EventHandler
    public void onHarvest(PlayerHarvestBlockEvent event) {
        final Block block = event.getHarvestedBlock();
        final List<ItemStack> harvestedItems = event.getItemsHarvested();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            ActionContext context = new ActionContext();
            context.fromBlock(block)
                    .fromPlayer(event.getPlayer())
                    .add(harvestedItems);
            handle("harvest", event.getPlayer(), context);
        });
    }

    @Override
    public void registerConditions(ConditionRegistry registry) {
        registry.setInstanceCreator("break", BlockCondition::new);
        registry.setInstanceCreator("place", BlockCondition::new);
        registry.setInstanceCreator("harvest", BlockCondition::new);
    }
}