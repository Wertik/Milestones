package space.devport.wertik.milestones.system.milestone.struct.reward;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.devport.utils.ConsoleOutput;
import space.devport.utils.configuration.Configuration;
import space.devport.utils.struct.Rewards;
import space.devport.wertik.milestones.system.user.struct.User;

import java.util.HashSet;
import java.util.Set;

public class MilestoneRewards extends Rewards {

    @Getter
    @Setter
    private Set<CountingRewards> cumulative = new HashSet<>();
    @Getter
    @Setter
    private Set<CountingRewards> repeat = new HashSet<>();

    @Getter
    @Setter
    private Rewards first = new Rewards();

    public MilestoneRewards() {
        super();
    }

    public MilestoneRewards(Rewards rewards) {
        super(rewards);
    }

    public MilestoneRewards(MilestoneRewards rewards) {
        super(rewards);
        this.cumulative = new HashSet<>(rewards.getCumulative());
        this.repeat = new HashSet<>(rewards.getRepeat());
        this.first = new Rewards(rewards.getFirst());
    }

    public void give(User user, String milestone) {
        Player player = user.getPlayer();

        if (player == null) {
            ConsoleOutput.getInstance().warn("Player " + user.getOfflinePlayer().getName() + " is not online, cannot reward him.");
            return;
        }

        this.give(player);

        if (user.getScore(milestone) == 0) {
            first.give(player, true);
            ConsoleOutput.getInstance().debug("Reward for first goes to " + player.getName());
        }

        long score = user.getScore(milestone);

        cumulative.forEach(r -> r.give(player, score));
        repeat.forEach(r -> r.give(player, score));
    }

    @NotNull
    public static MilestoneRewards load(Configuration configuration, String path, boolean silent) {
        ConfigurationSection section = configuration.getFileConfiguration().getConfigurationSection(path);

        if (section == null) {
            if (!silent)
                ConsoleOutput.getInstance().warn("Could not load milestone rewards, section " + configuration.getFile().getName() + "@" + path + " is invalid.");
            return new MilestoneRewards();
        }

        Rewards rewards = configuration.getRewards(section.getCurrentPath());

        MilestoneRewards milestoneRewards = new MilestoneRewards(rewards);

        ConfigurationSection cumulative = section.getConfigurationSection("cumulative");

        if (cumulative != null) {
            for (String count : cumulative.getKeys(false)) {
                CountingRewards countingRewards = CountingRewards.load(configuration, cumulative.getCurrentPath() + "." + count, Long::equals, silent);
                if (countingRewards == null)
                    continue;
                milestoneRewards.getCumulative().add(countingRewards);
            }
            ConsoleOutput.getInstance().debug("Loaded " + milestoneRewards.getCumulative().size() + " repeating rewards...");
        }

        ConfigurationSection repeat = section.getConfigurationSection("repeat");

        if (repeat != null) {
            for (String count : repeat.getKeys(false)) {
                CountingRewards countingRewards = CountingRewards.load(configuration, repeat.getCurrentPath() + "." + count, (c, c1) -> c % c1 == 0, silent);
                if (countingRewards == null)
                    continue;
                milestoneRewards.getRepeat().add(countingRewards);
            }
            ConsoleOutput.getInstance().debug("Loaded " + milestoneRewards.getRepeat().size() + " repeating rewards...");
        }

        milestoneRewards.setFirst(configuration.getRewards(path + ".first"));
        ConsoleOutput.getInstance().debug("With first find rewards " + milestoneRewards.getFirst().toString());

        ConsoleOutput.getInstance().debug("Loaded milestone rewards at " + configuration.getFile().getName() + "@" + path);
        return milestoneRewards;
    }
}