package me.wertik.milestones.objects;

public class Milestone {

    private Condition condition;
    private boolean onlyOnce;
    private boolean global;
    private String name;

    public Milestone(String name, Condition condition, boolean onlyOnce, boolean global) {
        this.name = name;
        this.condition = condition;
        this.onlyOnce = onlyOnce;
        this.global = global;
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
}
