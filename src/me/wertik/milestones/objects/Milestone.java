package me.wertik.milestones.objects;

import java.util.List;

public class Milestone {

    private Condition condition;
    private boolean onlyOnce;
    private boolean global;
    private String name;
    private boolean broadcast;
    private boolean inform;
    private String broadcastMessage;
    private String informMessage;
    private List<String> commandsReward;
    private String displayName;

    public Milestone(String name, String displayName, Condition condition, boolean onlyOnce, boolean global, boolean broadcast, String broadcastMessage, boolean inform,
                     String informMessage, List<String> commandsReward) {
        this.name = name;
        this.displayName = displayName;
        this.condition = condition;
        this.onlyOnce = onlyOnce;
        this.global = global;
        this.broadcast = broadcast;
        this.inform = inform;
        this.informMessage = informMessage;
        this.broadcastMessage = broadcastMessage;
        this.commandsReward = commandsReward;
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

    public boolean isBroadcast() {
        return broadcast;
    }

    public boolean isInform() {
        return inform;
    }

    public String getBroadcastMessage() {
        return broadcastMessage;
    }

    public String getInformMessage() {
        return informMessage;
    }

    public List<String> getCommandsReward() {
        return commandsReward;
    }

    public String getDisplayName() {
        if (displayName != null)
            return displayName;
        else
            return name;
    }
}
