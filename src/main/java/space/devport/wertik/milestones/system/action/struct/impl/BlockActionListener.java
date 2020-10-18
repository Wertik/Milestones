package space.devport.wertik.milestones.system.action.struct.impl;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.jetbrains.annotations.NotNull;
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
    public @NotNull List<String> getRegisteredActions() {
        return Arrays.asList("break", "place", "harvest");
    }

    @Override
    public void registerConditions(@NotNull ConditionRegistry registry) {
        registry.setInstanceCreator("break", BlockCondition::new);
        registry.setInstanceCreator("place", BlockCondition::new);
        registry.setInstanceCreator("harvest", BlockCondition::new);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent event) {

        final Player player = event.getPlayer();
        final Block block = event.getBlock();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            ActionContext context = new ActionContext();
            context.fromBlock(block)
                    .fromPlayer(player);
            handle("break", player, context);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlace(BlockPlaceEvent event) {

        final Player player = event.getPlayer();
        final Block block = event.getBlockPlaced();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            ActionContext context = new ActionContext();
            context.fromBlock(block)
                    .fromPlayer(player);
            handle("place", player, context);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHarvest(PlayerHarvestBlockEvent event) {

        final Player player = event.getPlayer();
        final Block block = event.getHarvestedBlock();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            ActionContext context = new ActionContext();
            context.fromBlock(block)
                    .fromPlayer(player);
            handle("harvest", player, context);
        });
    }
}