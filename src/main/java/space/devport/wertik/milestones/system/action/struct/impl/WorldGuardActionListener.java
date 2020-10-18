package space.devport.wertik.milestones.system.action.struct.impl;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
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
    public List<String> getRegisteredActions() {
        return Arrays.asList("region_enter", "region_leave");
    }

    @Override
    public void registerConditions(ConditionRegistry registry) {
        registry.setInstanceCreator("handle_leave", RegionLeaveCondition::new);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        if (event.getTo() == null)
            return;

        if (event.getTo().getX() == event.getFrom().getX() &&
                event.getTo().getY() == event.getFrom().getY() &&
                event.getTo().getZ() == event.getFrom().getZ())
            return;

        World bukkitWorld = event.getTo().getWorld();

        if (bukkitWorld == null)
            return;

        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(bukkitWorld);

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(world);

        if (regions == null)
            return;

        Location locationTo = BukkitAdapter.adapt(event.getTo());
        Location locationFrom = BukkitAdapter.adapt(event.getFrom());

        ApplicableRegionSet setTo = regions.getApplicableRegions(locationTo.toVector().toBlockPoint());
        ApplicableRegionSet setFrom = regions.getApplicableRegions(locationFrom.toVector().toBlockPoint());

        if (!setTo.equals(setFrom)) {

            // Handle leave
            ActionContext leaveContext = new ActionContext();
            leaveContext.fromPlayer(event.getPlayer())
                    .add(setFrom);
            handle("region_leave", event.getPlayer(), leaveContext);

            // Handle enter
            ActionContext context = new ActionContext();
            context.fromPlayer(event.getPlayer())
                    .add(event.getTo());
            handle("region_enter", event.getPlayer(), context);
        }
    }
}
