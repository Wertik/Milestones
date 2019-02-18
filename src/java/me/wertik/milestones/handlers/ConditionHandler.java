package me.wertik.milestones.handlers;

import me.wertik.milestones.Main;
import me.wertik.milestones.events.EntityKillMilestoneCollectEvent;
import me.wertik.milestones.objects.ExactCondition;
import me.wertik.milestones.objects.Milestone;
import me.wertik.milestones.objects.StagedReward;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ConditionHandler {

    // Simplify adding new conditions to the list. :)
    /*
     *
     * Again, it's just easier for me. You'll find it absurd and ignorant.
     *
     * */

    private Main plugin;

    public ConditionHandler() {
        plugin = Main.getInstance();
    }

    public void process(Player player, Entity entity) {
        for (Milestone milestone : Main.getInstance().getConfigLoader().getMilestones()) {

            ExactCondition condition = milestone.getConditions();

            if (!condition.check(player, entity))
                return;

            // Reward him.
            reward(milestone, player);

            // Fire the event
            plugin.getServer().getPluginManager().callEvent(new EntityKillMilestoneCollectEvent(player, milestone, entity));
        }
    }

    public void process(Player player) {
        for (Milestone milestone : Main.getInstance().getConfigLoader().getMilestones()) {
            ExactCondition condition = milestone.getConditions();

            if (!condition.check(player))
                return;

            // Reward him.
            reward(milestone, player);
        }
    }

    public void process(Player player, String message) {
        for (Milestone milestone : Main.getInstance().getConfigLoader().getMilestones()) {
            ExactCondition condition = milestone.getConditions();

            if (!condition.check(player, message))
                return;

            // Reward him.
            reward(milestone, player);
        }
    }

    public void process(Player player, Block block) {
        for (Milestone milestone : Main.getInstance().getConfigLoader().getMilestones()) {
            player.sendMessage("§aParsing for " + milestone.getName());

            ExactCondition condition = milestone.getConditions();

            if (!condition.check(player, block))
                continue;

            // Reward him.
            reward(milestone, player);
        }
    }

    // reward system
    public void reward(Milestone milestone, Player player) {

        DataHandler dataHandler = plugin.getDataHandler();

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

        milestone.getReward().give(player, milestone);

        if (milestone.getStagedRewards().isEmpty())
            return;

        // Staged Rewards
        int score = dataHandler.getScore(player.getName(), milestone.getName());

        for (StagedReward stagedReward : milestone.getStagedRewards())
            if (stagedReward.getCount() == score)
                stagedReward.give(player, milestone);
    }
}

