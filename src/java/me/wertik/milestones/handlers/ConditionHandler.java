package me.wertik.milestones.handlers;

import me.wertik.milestones.Main;
import me.wertik.milestones.events.MilestoneCollectEvent;
import me.wertik.milestones.objects.ExactCondition;
import me.wertik.milestones.objects.Milestone;
import me.wertik.milestones.objects.StagedReward;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ConditionHandler {

    //
    /*
     *
     * The main processes are handled here.
     *
     * */

    private Main plugin;
    private DataHandler dataHandler;

    public ConditionHandler() {
        plugin = Main.getInstance();
        dataHandler = plugin.getDataHandler();
    }

    public void process(Player player, Entity entity) {
        for (Milestone milestone : Main.getInstance().getConfigLoader().getMilestones()) {

            ExactCondition condition = milestone.getConditions();

            if (!condition.check(player, entity))
                return;

            // Reward him.
            reward(milestone, player);

            // Fire the event
            plugin.getServer().getPluginManager().callEvent(new MilestoneCollectEvent(player, milestone, entity));
        }
    }

    public void process(Player player) {
        for (Milestone milestone : Main.getInstance().getConfigLoader().getMilestones()) {
            ExactCondition condition = milestone.getConditions();

            if (!condition.check(player))
                return;

            // Reward him.
            reward(milestone, player);

            plugin.getServer().getPluginManager().callEvent(new MilestoneCollectEvent(player, milestone));
        }
    }

    public void process(Player player, String message) {
        for (Milestone milestone : Main.getInstance().getConfigLoader().getMilestones()) {
            ExactCondition condition = milestone.getConditions();

            if (!condition.check(player, message))
                return;

            // Reward him.
            reward(milestone, player);

            plugin.getServer().getPluginManager().callEvent(new MilestoneCollectEvent(player, milestone, message));
        }
    }

    public void process(Player player, Block block) {
        for (Milestone milestone : Main.getInstance().getConfigLoader().getMilestones()) {
            ExactCondition condition = milestone.getConditions();

            if (!condition.check(player, block))
                continue;

            // Reward him.
            reward(milestone, player);

            plugin.getServer().getPluginManager().callEvent(new MilestoneCollectEvent(player, milestone, block));
        }
    }

    // reward system
    public void reward(Milestone milestone, Player player) {

        // Score
        if (milestone.isGlobal()) {
            if (milestone.isOnlyOnce()) {

                if (!dataHandler.getGlobalLoggedPlayers(milestone.getName()).contains(player.getName()))
                    return;
                else
                    dataHandler.addGlobalScore(milestone.getName(), player.getName());
            } else
                dataHandler.addGlobalScore(milestone.getName(), player.getName());
        } else {
            if (milestone.isOnlyOnce()) {
                if (!dataHandler.isLogged(player.getName(), milestone.getName()))
                    dataHandler.addScore(milestone.getName(), player.getName());
                else
                    return;
            } else
                dataHandler.addScore(player.getName(), milestone.getName());
        }

        // Staged Rewards
        int score = dataHandler.getScore(player.getName(), milestone.getName());
        boolean stagedRewarded = false;

        if (!milestone.getStagedRewards().isEmpty())
            for (StagedReward stagedReward : milestone.getStagedRewards()) {
                if (stagedReward.getCount() == score) {
                    stagedReward.give(player, milestone);
                    if (stagedReward.isDenyNormal())
                        stagedRewarded = true;
                } else if (stagedReward.isRepeat() && ((score % stagedReward.getCount()) == 0)) {
                    stagedReward.give(player, milestone);
                    if (stagedReward.isDenyNormal())
                        stagedRewarded = true;
                }
            }

        if (!stagedRewarded)
            milestone.getReward().give(player, milestone);
    }
}

