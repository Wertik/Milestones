package space.devport.wertik.milestones.system.milestone.struct.reward;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.ConsoleOutput;
import space.devport.utils.ParseUtil;
import space.devport.utils.configuration.Configuration;
import space.devport.utils.struct.Rewards;

import java.util.function.BiPredicate;

public class CountingRewards extends Rewards {

    @Getter
    private final long count;

    private final BiPredicate<Long, Long> condition;

    public CountingRewards(Rewards rewards, int count, BiPredicate<Long, Long> condition) {
        super(rewards);
        this.count = count;
        this.condition = condition;
    }

    public void give(Player player, long count) {
        if (condition.test(count, this.count))
            super.give(player, true);
    }

    @Nullable
    public static CountingRewards load(Configuration configuration, String path, BiPredicate<Long, Long> condition, boolean silent) {
        ConfigurationSection section = configuration.getFileConfiguration().getConfigurationSection(path);

        if (section == null) {
            if (!silent)
                ConsoleOutput.getInstance().warn("Could not load counting rewards at " + configuration.getFile().getName() + "@" + path + ", section is invalid.");
            return null;
        }

        int count = ParseUtil.parseInteger(section.getName(), -1, true);

        if (count <= 0) {
            if (!silent)
                ConsoleOutput.getInstance().warn("Could not load counting rewards at " + configuration.getFile().getName() + "@" + path + ", count is invalid.");
            return null;
        }

        Rewards rewards = configuration.getRewards(section.getCurrentPath());
        ConsoleOutput.getInstance().debug("Loaded counting rewards at " + configuration.getFile().getName() + "@" + path);
        return new CountingRewards(rewards, count, condition);
    }
}