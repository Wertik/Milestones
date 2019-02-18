package me.wertik.milestones.objects;

public enum MilestoneType {
    BLOCK_BREAK("blockbreak"),
    BLOCK_PLACE("blockplace"),
    ENTITY_KILL("entitykill"),
    PLAYER_JOIN("playerjoin"),
    PLAYER_QUIT("playerkill"),
    PLAYER_CHAT("playerchat");

    private String type;

    MilestoneType(String type) {
        this.type = type;
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
}
