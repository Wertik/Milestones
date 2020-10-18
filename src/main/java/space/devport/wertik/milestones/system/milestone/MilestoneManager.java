package space.devport.wertik.milestones.system.milestone;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.ConsoleOutput;
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

    public void loadAdditional() {
        FileConfiguration config = configuration.getFileConfiguration();

        int count = 0;
        for (String name : config.getKeys(false)) {

            if (isValid(name)) continue;

            Milestone milestone = Milestone.load(configuration, name);
            if (milestone == null)
                continue;
            this.loadedMilestones.put(name, milestone);
            count++;
        }
        if (count > 0)
            ConsoleOutput.getInstance().info("Loaded " + this.loadedMilestones.size() + " additional milestone(s)...");
    }

    public void load() {

        this.loadedMilestones.clear();
        configuration.load();

        FileConfiguration config = configuration.getFileConfiguration();

        for (String name : config.getKeys(false)) {
            Milestone milestone = Milestone.load(configuration, name);
            if (milestone == null)
                continue;
            this.loadedMilestones.put(name, milestone);
        }
        ConsoleOutput.getInstance().info("Loaded " + this.loadedMilestones.size() + " milestone(s)...");
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
