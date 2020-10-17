package space.devport.wertik.milestones.system.milestone.struct.reward;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.configuration.Configuration;
import space.devport.utils.struct.Rewards;

public class RepeatingReward extends Rewards {

    @Getter
    private final int count;

    public RepeatingReward(int count) {
        this.count = count;
    }

    @Nullable
    public static RepeatingReward load(Configuration configuration, String path) {
        //TODO
        return null;
    }
}