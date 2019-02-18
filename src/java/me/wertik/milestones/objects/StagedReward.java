package me.wertik.milestones.objects;

import org.bukkit.entity.Player;

public class StagedReward {

    private int count;
    private Reward reward;

    public StagedReward(int count, Reward reward) {
        this.reward = reward;
        this.count = count;
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
}
