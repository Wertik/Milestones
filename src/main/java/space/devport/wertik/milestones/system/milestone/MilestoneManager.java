package space.devport.wertik.milestones.system.milestone;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.configuration.Configuration;
import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.system.milestone.struct.Milestone;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MilestoneManager {

    private final MilestonesPlugin plugin;

    private final Map<String, Milestone> loadedMilestones = new HashMap<>();

    private final Configuration configuration;

    public MilestoneManager(MilestonesPlugin plugin) {
        this.plugin = plugin;
        this.configuration = new Configuration(plugin, "milestones.yml");
    }

    @Nullable
    public Milestone getMilestone(String name) {
        return name == null ? null : this.loadedMilestones.get(name);
    }

    public void load() {
        configuration.load();

        FileConfiguration config = configuration.getFileConfiguration();

        for (String name : config.getKeys(false)) {

        }
    }

    public Map<String, Milestone> getLoadedMilestones() {
        return Collections.unmodifiableMap(loadedMilestones);
    }

    public Set<String> getMilestones() {
        return this.loadedMilestones.keySet();
    }

    public boolean isValid(String name) {
        return this.loadedMilestones.containsKey(name);
    }
}
