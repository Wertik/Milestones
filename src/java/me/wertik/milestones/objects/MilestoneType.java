package me.wertik.milestones.objects;

public enum MilestoneType {
    BLOCK_BREAK("blockbreak", true),
    BLOCK_PLACE("blockplace"),
    ENTITY_KILL("entitykill", true),
    PLAYER_JOIN("playerjoin"),
    PLAYER_QUIT("playerquit"),
    PLAYER_CHAT("playerchat", true);

    private String type;
    private boolean requiredTarget;

    MilestoneType(String type, boolean requiresTarget) {
        this.type = type;
        this.requiredTarget = requiresTarget;
    }

    MilestoneType(String type) {
        this.type = type;
        requiredTarget = false;
    }

    public String stringType() {
        return type;
    }

    public static MilestoneType fromString(String string) {
        for (MilestoneType type :values()) {
            if (type.stringType().equalsIgnoreCase(string))
                return type;
        }
        return null;
    }

    public String getType() {
        return type;
    }

     public boolean requiresTarget() {
        return requiredTarget;
    }
}
