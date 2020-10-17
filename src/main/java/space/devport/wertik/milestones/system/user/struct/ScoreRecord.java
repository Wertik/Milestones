package space.devport.wertik.milestones.system.user.struct;

import lombok.Getter;

public class ScoreRecord {

    @Getter
    private final String name;
    @Getter
    private long score;

    public ScoreRecord(String name) {
        this.name = name;
    }

    public ScoreRecord(String name, long score) {
        this.name = name;
        this.score = score;
    }

    public long increment() {
        this.score++;
        return this.score;
    }

    public long update(long score) {
        this.score = score;
        return this.score;
    }
}