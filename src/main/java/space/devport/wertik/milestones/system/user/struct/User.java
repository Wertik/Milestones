package space.devport.wertik.milestones.system.user.struct;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class User {

    @Getter
    private final UUID uniqueID;

    private final Map<String, ScoreRecord> scores = new HashMap<>();

    public User(UUID uniqueID) {
        this.uniqueID = uniqueID;
    }

    @Nullable
    public String getName() {
        return getOfflinePlayer().getName();
    }

    @NotNull
    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uniqueID);
    }

    @Nullable
    public Player getPlayer() {
        return Bukkit.getPlayer(uniqueID);
    }

    @Nullable
    public ScoreRecord getRecord(String name) {
        return this.scores.get(name);
    }

    /**
     * Creates or overwrites a record.
     */
    @NotNull
    public ScoreRecord createRecord(String name, int score) {
        ScoreRecord record = new ScoreRecord(name, score);
        this.scores.put(name, record);
        return record;
    }

    @NotNull
    public ScoreRecord getOrCreateRecord(String name) {
        return this.scores.containsKey(name) ? this.scores.get(name) : createRecord(name, 0);
    }

    public void updateRecord(String name, ScoreRecord record) {
        this.scores.put(name, record);
    }

    public long getScore(String name) {
        ScoreRecord record = getRecord(name);
        return record == null ? 0 : record.getScore();
    }

    public long incrementScore(String name) {
        return getOrCreateRecord(name).increment();
    }

    public long updateScore(String name, int value) {
        return getOrCreateRecord(name).update(value);
    }

    public void resetScore(String name) {
        this.scores.remove(name);
    }

    public void resetScores() {
        this.scores.clear();
    }

    public boolean isEmpty() {
        return this.scores.isEmpty() || this.scores.values().stream().allMatch(score -> score.getScore() == 0);
    }

    public Map<String, ScoreRecord> getScores() {
        return Collections.unmodifiableMap(scores);
    }
}
