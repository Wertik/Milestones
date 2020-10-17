package space.devport.wertik.milestones.system.milestone.struct.condition;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import space.devport.utils.configuration.Configuration;
import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.Utils;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.impl.BaseCondition;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCondition {

    @Getter
    private List<ItemStack> inInventory;

    @Getter
    private List<String> biomes;

    @Getter
    private List<String> regions;

    @Getter
    private List<String> worldNames;

    @Getter
    private List<ItemStack> inHandItems;

    @Getter
    private List<String> permissions;

    public AbstractCondition() {
    }

    public abstract boolean onCheck(Player player, ActionContext context);

    @NotNull
    public abstract AbstractCondition onLoad(Configuration configuration, ConfigurationSection section);

    @NotNull
    public AbstractCondition load(Configuration configuration, String path) {

        ConfigurationSection section = configuration.getFileConfiguration().getConfigurationSection(path);

        if (section == null) {
            //TODO Think about this. Might want to return a always-false condition instead?
            return new BaseCondition();
        }

        //TODO Load more basics

        this.permissions = configuration.getStringList(path + ".permissions");
        this.biomes = configuration.getStringList(path + ".biomes");
        this.regions = configuration.getStringList(path + ".regions");
        this.worldNames = configuration.getStringList(path + ".worlds");

        return onLoad(configuration, section);
    }

    public boolean check(Player player, ActionContext context) {

        if (!biomes.contains(player.getLocation().getBlock().getBiome().toString()) || !worldNames.contains(player.getWorld().getName()))
            return false;

        // Tool (in hand items)
        if (!inHandItems.isEmpty()) {
            int i = 0;
            for (ItemStack item : inHandItems) {

                if (item.getAmount() == -1)
                    if (item.getType().equals(player.getInventory().getItemInMainHand().getType()))
                        break;

                if (Utils.compareItemStacks(item, player.getInventory().getItemInMainHand()))
                    break;

                i++;
                if (i == inHandItems.size())
                    return false;
            }
        }

        // Region
        if (!regions.isEmpty()) {
            List<String> regionSet = new ArrayList<>(); //MilestonesPlugin.getInstance().setupWorldGuard().getRegionManager(player.getWorld())
            //.getApplicableRegionsIDs(MilestonesPlugin.getInstance().setupWorldGuard().wrapPlayer(player).getPosition());

            int i = 0;
            for (String region : regionSet) {
                if (regions.contains(region))
                    break;

                i++;
                if (i == regionSet.size())
                    return false;
            }
        }

        // Items in inventory
        if (!inInventory.isEmpty()) {
            List<ItemStack> contents = Utils.listToArray(player.getInventory().getContents());

            for (ItemStack item : inInventory) {
                if (!contents.contains(item))
                    return false;
            }
        }

        // Permission
        if (MilestonesPlugin.getInstance().getServer().getPluginManager().getPlugin("Vault").isEnabled()) {
            for (String permission : permissions) {
                if (!player.hasPermission(permission))
                    return false;
            }
        }

        return onCheck(player, context);
    }
}
