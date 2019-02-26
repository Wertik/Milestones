package me.wertik.milestones.objects;

import org.bukkit.entity.Player;

public class StagedReward {

    private int count;
    private Reward reward;
    private boolean repeat;
    private boolean denyNormal;

    public StagedReward(int count, Reward reward, boolean repeat, boolean denyNormal) {
        this.reward = reward;
        this.count = count;
        this.repeat = repeat;
        this.denyNormal = denyNormal;
    }

    public int getCount() {
        return count;
    }

    public Reward getReward() {
        return reward;
    }

    public void give(Player player, Milestone milestone) {
        reward.give(player, milestone);
    }

    public boolean isRepeat() {
        return repeat;
    }

    public boolean isDenyNormal() {
        return denyNormal;
    }
}
