package space.devport.wertik.milestones.system.action.struct.impl;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jetbrains.annotations.NotNull;
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
    public @NotNull List<String> getRegisteredActions() {
        return Arrays.asList("world_enter", "world_leave");
    }

    @Override
    public void registerConditions(@NotNull ConditionRegistry registry) {
        registry.setInstanceCreator("world_leave", WorldLeaveCondition::new);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {

        final Player player = event.getPlayer();
        final World from = event.getFrom();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            ActionContext context = new ActionContext();
            context.fromPlayer(player);
            handle("world_enter", player, context);

            ActionContext leaveContext = new ActionContext();
            leaveContext.fromPlayer(player)
                    .add(from);
            handle("world_leave", player, context);
        });
    }
}
