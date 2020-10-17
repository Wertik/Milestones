package space.devport.wertik.milestones.system.milestone.struct.condition;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import space.devport.utils.configuration.Configuration;
import space.devport.wertik.milestones.Utils;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.impl.BaseCondition;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCondition {

    @Getter
    private List<Biome> biomes;

    @Getter
    private List<String> regions;

    @Getter
    private List<String> worlds;

    @Getter
    private List<ItemStack> tools;

    @Getter
    private List<ItemStack> possessions;

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
            return new BaseCondition();
        }

        //TODO Load more basics

        this.permissions = configuration.getStringList(path + ".permissions");

        List<String> biomeNames = configuration.getStringList(path + ".biomes", new ArrayList<>());
        for (String biomeName : biomeNames) {
            Biome biome = Utils.parseEnum(biomeName, Biome.class);
            if (biome != null)
                this.biomes.add(biome);
        }

        this.regions = configuration.getStringList(path + ".regions");
        this.worlds = configuration.getStringList(path + ".worlds");

        return onLoad(configuration, section);
    }

    public boolean check(@NotNull Player player, ActionContext context) {

        // Permissions
        if (!Utils.isListNullOrEmpty(permissions) && permissions.stream().anyMatch(perm -> !player.hasPermission(perm)))
            return false;

        Location location = context.get(Location.class);
        if (location == null)
            location = player.getLocation();

        Block block = context.get(Block.class);
        if (block == null)
            block = location.getBlock();

        // Biomes
        if (!Utils.isListNullOrEmpty(biomes) && !biomes.contains(block.getBiome()))
            return false;

        // Worlds
        if (!Utils.isListNullOrEmpty(worlds) && !worlds.contains(location.getWorld().getName()))
            return false;

        // Tools
        //TODO
        if (!Utils.isListNullOrEmpty(tools)) {
            int i = 0;
            for (ItemStack item : tools) {

                if (item.getAmount() == -1)
                    if (item.getType().equals(player.getInventory().getItemInMainHand().getType()))
                        break;

                if (Utils.compareItemStacks(item, player.getInventory().getItemInMainHand()))
                    break;

                i++;
                if (i == tools.size())
                    return false;
            }
        }

        // Regions
        //TODO
        if (!Utils.isListNullOrEmpty(regions)) {
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
        //TODO
        if (!Utils.isListNullOrEmpty(possessions)) {
            List<ItemStack> contents = Utils.listToArray(player.getInventory().getContents());

            for (ItemStack item : possessions) {
                if (!contents.contains(item))
                    return false;
            }
        }

        return onCheck(player, context);
    }
}
