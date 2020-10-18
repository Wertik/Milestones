package space.devport.wertik.milestones.system.milestone.struct.condition.impl;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import space.devport.utils.configuration.Configuration;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.AbstractCondition;

import java.util.HashSet;
import java.util.Set;

public class RegionLeaveCondition extends AbstractCondition {

    private final Set<String> allowedRegions = new HashSet<>();

    @Override
    public boolean onCheck(Player player, ActionContext context) {
        ApplicableRegionSet regionSet = context.get(ApplicableRegionSet.class);

        if (regionSet == null)
            return false;

        Set<ProtectedRegion> regions = regionSet.getRegions();

        return allowedRegions.isEmpty() || regions.stream().anyMatch(r -> allowedRegions.contains(r.getId()));
    }

    @Override
    public void onLoad(Configuration configuration, ConfigurationSection section) {
        this.allowedRegions.addAll(section.getStringList("from"));
    }
}
