package space.devport.wertik.milestones.system.milestone.struct.condition;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import space.devport.utils.ConsoleOutput;
import space.devport.utils.configuration.Configuration;
import space.devport.wertik.milestones.Utils;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.impl.BaseCondition;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCondition {

    @Getter
    private final List<Biome> biomes = new ArrayList<>();

    @Getter
    private final List<String> regions = new ArrayList<>();

    @Getter
    private final List<String> worlds = new ArrayList<>();

    @Getter
    private final List<String> permissions = new ArrayList<>();

    @Getter
    private final List<ItemStack> tools = new ArrayList<>();

    @Getter
    private final List<ItemStack> possessions = new ArrayList<>();

    public AbstractCondition() {
    }

    public abstract boolean onCheck(Player player, ActionContext context);

    public abstract void onLoad(Configuration configuration, ConfigurationSection section);

    @NotNull
    public AbstractCondition load(Configuration configuration, String path) {

        ConfigurationSection section = configuration.getFileConfiguration().getConfigurationSection(path);

        if (section == null) {
            return new BaseCondition();
        }

        //TODO Load more basics

        this.permissions.addAll(configuration.getStringList(path + ".permissions", new ArrayList<>()));

        List<String> biomeNames = configuration.getStringList(path + ".biomes", new ArrayList<>());
        for (String biomeName : biomeNames) {
            Biome biome = Utils.parseEnum(biomeName, Biome.class);
            if (biome != null)
                this.biomes.add(biome);
        }

        this.regions.addAll(configuration.getStringList(path + ".regions", new ArrayList<>()));
        this.worlds.addAll(configuration.getStringList(path + ".worlds", new ArrayList<>()));

        try {
            onLoad(configuration, section);
        } catch (Exception e) {
            ConsoleOutput.getInstance().err("There was an error loading additional parameters to condition " + getClass().getSimpleName()
                    + " from " + configuration.getFile().getName() + "@" + path);
        }

        return this;
    }

    public boolean check(@NotNull Player player, ActionContext context) {

        // Permissions
        if (!permissions.isEmpty() && permissions.stream().anyMatch(perm -> !player.hasPermission(perm))) {
            ConsoleOutput.getInstance().debug("Check failed on permissions.");
            return false;
        }

        Location location = context.get(Location.class);
        if (location == null)
            location = player.getLocation();

        Block block = context.get(Block.class);
        if (block == null)
            block = location.getBlock();

        // Biomes
        if (!biomes.isEmpty() && !biomes.contains(block.getBiome())) {
            ConsoleOutput.getInstance().debug("Check failed on biomes.");
            return false;
        }

        // Worlds
        if (!worlds.isEmpty() && !worlds.contains(location.getWorld().getName())) {
            ConsoleOutput.getInstance().debug("Check failed on worlds.");
            return false;
        }

        //TODO Tools, regions, possessions

        return onCheck(player, context);
    }
}
