package space.devport.wertik.milestones.system.milestone.struct.condition;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final Set<Material> tools = new HashSet<>();

    @Getter
    private final Set<Material> possessions = new HashSet<>();

    public AbstractCondition() {
    }

    public abstract boolean onCheck(Player player, ActionContext context);

    public abstract void onLoad(Configuration configuration, ConfigurationSection section);

    //TODO Implement Vault economy condition

    @NotNull
    public AbstractCondition load(Configuration configuration, String path) {

        ConfigurationSection section = configuration.getFileConfiguration().getConfigurationSection(path);

        if (section == null) {
            return new BaseCondition();
        }

        this.permissions.addAll(configuration.getStringList(path + ".permissions", new ArrayList<>()));

        List<String> biomeNames = configuration.getStringList(path + ".biomes", new ArrayList<>());
        for (String biomeName : biomeNames) {
            Biome biome = Utils.parseEnum(biomeName, Biome.class);
            if (biome != null)
                this.biomes.add(biome);
        }

        this.regions.addAll(configuration.getStringList(path + ".regions", new ArrayList<>()));
        this.worlds.addAll(configuration.getStringList(path + ".worlds", new ArrayList<>()));

        List<String> possessionNames = section.getStringList("possessions");
        for (String possessionName : possessionNames) {
            Material material = Material.matchMaterial(possessionName);
            if (material != null)
                this.possessions.add(material);
        }

        List<String> toolNames = section.getStringList("tools");
        for (String toolName : toolNames) {
            Material material = Material.matchMaterial(toolName);
            if (material != null)
                this.tools.add(material);
        }

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
        if (!worlds.isEmpty() && location.getWorld() != null && !worlds.contains(location.getWorld().getName())) {
            ConsoleOutput.getInstance().debug("Check failed on worlds.");
            return false;
        }

        // Regions
        if (!regions.isEmpty()) {

            if (location.getWorld() == null)
                return false;

            com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(location.getWorld());

            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regionManager = container.get(world);

            if (regionManager == null)
                return false;

            com.sk89q.worldedit.util.Location worldGuardLocation = BukkitAdapter.adapt(location);

            ApplicableRegionSet regionSet = regionManager.getApplicableRegions(worldGuardLocation.toVector().toBlockPoint());

            if (regionSet.getRegions().stream()
                    .map(ProtectedRegion::getId)
                    .noneMatch(this.regions::contains))
                return false;
        }

        // Tools
        if (!tools.isEmpty()) {
            Material tool = player.getInventory().getItemInMainHand().getType();

            if (!tools.contains(tool))
                return false;
        }

        // Possessions
        if (!possessions.isEmpty()) {
            List<Material> playerPossessions = Arrays.stream(player.getInventory().getContents())
                    .map(ItemStack::getType)
                    .collect(Collectors.toList());

            if (possessions.stream().anyMatch(p -> !playerPossessions.contains(p)))
                return false;
        }

        return onCheck(player, context);
    }
}
