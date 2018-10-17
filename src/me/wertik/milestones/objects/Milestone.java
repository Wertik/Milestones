package me.wertik.milestones.objects;

import java.util.List;

public class Milestone {

    private Condition condition;
    private boolean onlyOnce;
    private boolean global;
    private String name;
    private String displayName;
    private Reward reward;
    private List<StagedReward> stagedRewards;

    public Milestone(String name, String displayName, Condition condition, Reward reward, List<StagedReward> stagedRewards, boolean onlyOnce, boolean global) {
        this.name = name;
        this.displayName = displayName;
        this.condition = condition;
        this.onlyOnce = onlyOnce;
        this.global = global;
        this.reward = reward;
        this.stagedRewards = stagedRewards;
    }

    public Condition getCondition() {
        return condition;
    }

    public boolean isOnlyOnce() {
        return onlyOnce;
    }

    public boolean isGlobal() {
        return global;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        if (displayName != null)
            return displayName;
        else
            return name;
    }

    public Reward getReward() {
        return reward;
    }

    public List<StagedReward> getStagedRewards() {
        return stagedRewards;
    }
}
