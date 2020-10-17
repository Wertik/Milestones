package space.devport.wertik.milestones.system;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitTask;
import space.devport.utils.ConsoleOutput;
import space.devport.utils.configuration.Configuration;
import space.devport.wertik.milestones.MilestonesPlugin;

public class ReloadableTask implements Runnable {

    private final MilestonesPlugin plugin;

    @Getter
    private boolean enabled;
    @Getter
    private long interval;

    private TaskAction action;

    private BukkitTask task;

    public ReloadableTask(MilestonesPlugin plugin) {
        this.plugin = plugin;
    }

    public ReloadableTask load(Configuration configuration, String path) {

        ConfigurationSection section = configuration.getFileConfiguration().getConfigurationSection(path);

        if (section == null) {
            this.enabled = false;
            return this;
        }

        this.enabled = section.getBoolean("enabled", false);
        this.interval = section.getInt("interval", 300) * 20L;

        return this;
    }

    public ReloadableTask setup(TaskAction action) {
        this.action = action;
        return this;
    }

    public ReloadableTask reload(Configuration configuration, String path) {
        return stop().load(configuration, path).start();
    }

    public ReloadableTask stop() {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }
        return this;
    }

    public ReloadableTask start() {
        if (this.task != null)
            stop();

        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, interval, interval);
        ConsoleOutput.getInstance().debug("Started reloadable task with an interval of " + this.interval / 20 + "s...");
        return this;
    }

    @Override
    public void run() {
        if (action != null)
            action.run();
    }

    public interface TaskAction {
        void run();
    }
}
