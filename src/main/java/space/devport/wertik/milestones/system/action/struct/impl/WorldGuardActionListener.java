package space.devport.wertik.milestones.system.action.struct.impl;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;
import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.system.action.struct.AbstractActionListener;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.ConditionRegistry;
import space.devport.wertik.milestones.system.milestone.struct.condition.impl.RegionLeaveCondition;

import java.util.Arrays;
import java.util.List;

public class WorldGuardActionListener extends AbstractActionListener {

    public WorldGuardActionListener(MilestonesPlugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull List<String> getRegisteredActions() {
        return Arrays.asList("region_enter", "region_leave");
    }

    @Override
    public void registerConditions(@NotNull ConditionRegistry registry) {
        registry.setInstanceCreator("handle_leave", RegionLeaveCondition::new);
    }

    @Override
    public boolean canRegister() {
        return plugin.getPluginManager().isPluginEnabled("WorldGuard");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent event) {

        final Player player = event.getPlayer();

        final Location to = event.getTo();
        final Location from = event.getFrom();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (to == null)
                return;

            if (to.getX() == from.getX() &&
                    to.getY() == from.getY() &&
                    to.getZ() == from.getZ())
                return;

            World bukkitWorld = to.getWorld();

            if (bukkitWorld == null)
                return;

            com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(bukkitWorld);

            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regionManager = container.get(world);

            if (regionManager == null)
                return;

            com.sk89q.worldedit.util.Location locationTo = BukkitAdapter.adapt(to);
            com.sk89q.worldedit.util.Location locationFrom = BukkitAdapter.adapt(from);

            ApplicableRegionSet regionsTo = regionManager.getApplicableRegions(locationTo.toVector().toBlockPoint());
            ApplicableRegionSet regionsFrom = regionManager.getApplicableRegions(locationFrom.toVector().toBlockPoint());

            // Only listen to changes
            if (!regionsTo.equals(regionsFrom)) {

                // Handle leave
                ActionContext leaveContext = new ActionContext();
                leaveContext.fromPlayer(player)
                        .add(regionsFrom);
                handle("region_leave", player, leaveContext);

                // Handle enter
                ActionContext context = new ActionContext();
                context.fromPlayer(player)
                        .add(to);
                handle("region_enter", player, context);
            }
        });
    }
}
