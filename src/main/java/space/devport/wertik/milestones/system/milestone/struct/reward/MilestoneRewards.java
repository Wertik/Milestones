package space.devport.wertik.milestones.system.milestone.struct.reward;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import space.devport.utils.configuration.Configuration;
import space.devport.utils.struct.Rewards;

import java.util.HashSet;
import java.util.Set;

public class MilestoneRewards extends Rewards {

    @Getter
    private final Set<RepeatingReward> repeatingRewards = new HashSet<>();

    public MilestoneRewards(Rewards rewards) {
        super(rewards);
    }

    public MilestoneRewards(MilestoneRewards milestoneRewards) {
        super(milestoneRewards);
        this.repeatingRewards.clear();
        this.repeatingRewards.addAll(milestoneRewards.getRepeatingRewards());
    }

    @NotNull
    public static MilestoneRewards load(Configuration configuration, String path) {
        MilestoneRewards milestoneRewards = new MilestoneRewards(configuration.getRewards(path));

        //TODO load rest

        return milestoneRewards;
    }
}