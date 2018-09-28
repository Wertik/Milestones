package me.wertik.milestones.objects;

public class Milestone {

    private Condition condition;
    private boolean onlyOnce;
    private boolean global;
    private String name;
    private String displayName;
    private Reward reward;

    public Milestone(String name, String displayName, Condition condition, Reward reward, boolean onlyOnce, boolean global) {
        this.name = name;
        this.displayName = displayName;
        this.condition = condition;
        this.onlyOnce = onlyOnce;
        this.global = global;
        this.reward = reward;
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
}
