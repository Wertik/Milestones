package me.wertik.milestones.handlers;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.wertik.milestones.ConfigLoader;
import me.wertik.milestones.Main;
import me.wertik.milestones.Utils;
import me.wertik.milestones.objects.Condition;
import me.wertik.milestones.objects.Milestone;
import me.wertik.milestones.objects.StagedReward;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ConditionHandler {

    public ConditionHandler() {
    }

    // Simplify adding new conditions to the list. :)
    /*
     *
     * Again, it's just easier for me. You'll find it absurd and ignorant.
     *
     * */

    ConfigLoader cload = new ConfigLoader();
    DataHandler dataHandler = new DataHandler();
    Main plugin = Main.getInstance();
    WorldGuardPlugin wg = plugin.getWorldGuard();
    Utils utils = new Utils();

    public void process(String type, String targetType, Player p) {
        for (Milestone milestone : cload.getMilestones()) {
            process(milestone, type, targetType, p);
        }
    }

    public void process(Milestone milestone, String type, String targetType, Player p) {

        // Toggle checkers
        // player
        // Too lazy..

        Condition condition = milestone.getCondition();

        // Type of the condition
        if (!condition.getType().equalsIgnoreCase(type))
            return;

        // targetTypes
        if (!condition.getTargetTypes().contains(targetType) && !condition.getTargetTypes().isEmpty())
            return;

        // biomeTypes
        if (!condition.getBiomes().contains(p.getLocation().getBlock().getBiome().toString()) && !condition.getBiomes().isEmpty())
            return;

        // toolTypes
        int i = 0;
        for (ItemStack tool : condition.getToolTypes()) {

            i++;

            if (condition.getType().equals("blockplace") || condition.getToolTypes().isEmpty()) {
                i = 0;
                break;
            }

            ItemStack mainHand = p.getInventory().getItemInMainHand();
            if (utils.compareItemStacks(tool, mainHand)) {
                i = 0;
                break;
            }
        }

        if (i == condition.getToolTypes().size())
            return;

        // regionNames
        LocalPlayer localPlayer = wg.wrapPlayer(p);
        Vector vector = localPlayer.getPosition();
        List<String> regionSet = wg.getRegionManager(p.getWorld()).getApplicableRegionsIDs(vector);

        if (!condition.getRegionNames().isEmpty() && regionSet.isEmpty())
            return;

        for (String region : regionSet) {

            if (condition.getRegionNames().isEmpty())
                break;

            if (condition.getRegionNames().contains(region))
                break;
            else
                return;
        }

        // inventory items
        for (ItemStack item : condition.getInInventory()) {

            if (condition.getInInventory().isEmpty())
                break;

            if (item.getAmount() == -1) {
                if (p.getInventory().contains(item.getType()))
                    continue;
                else
                    return;
            }

            if (p.getInventory().contains(item))
                continue;
            else
                return;
        }

        // Reward him.
        reward(milestone, p);
    }

    // reward system
    public void reward(Milestone milestone, Player p) {

        // Score
        if (milestone.isGlobal()) {
            dataHandler.addGlobalScore(milestone.getName());
        } else {
            if (milestone.isOnlyOnce()) {
                if (dataHandler.getScore(p.getName(), milestone.getName()) == 0) {
                    dataHandler.addScore(p.getName(), milestone.getName());
                } else
                    return;
            } else
                dataHandler.addScore(p.getName(), milestone.getName());
        }

        // Messages
        if (!milestone.getReward().getBroadcastMessage().equals("")) {
            for (Player t : plugin.getServer().getOnlinePlayers()) {
                t.sendMessage(cload.getFinalString(milestone.getReward().getBroadcastMessage(), p, milestone));
            }
        }

        if (!milestone.getReward().getInformMessage().equals("")) {
            p.sendMessage(cload.getFinalString(milestone.getReward().getInformMessage(), p, milestone));
        }

        // Commands
        for (String command : milestone.getReward().getCommands()) {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cload.parseString(command, p, milestone));
        }

        // Items
        for (ItemStack item : milestone.getReward().getItems()) {
            p.getInventory().addItem(item);
        }

        if (milestone.getStagedRewards() == null)
            return;

        // Staged Rewards
        int score = dataHandler.getScore(p.getName(), milestone.getName());

        for (StagedReward stagedReward : milestone.getStagedRewards()) {

            if (stagedReward.getCount() == score) {
                // Messages
                if (!stagedReward.getReward().getBroadcastMessage().equals("")) {
                    for (Player t : plugin.getServer().getOnlinePlayers()) {
                        t.sendMessage(cload.getFinalString(stagedReward.getReward().getBroadcastMessage(), p, milestone));
                    }
                }

                if (!stagedReward.getReward().getInformMessage().equals("")) {
                    p.sendMessage(cload.getFinalString(stagedReward.getReward().getInformMessage(), p, milestone));
                }

                // Commands
                for (String command : stagedReward.getReward().getCommands()) {
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cload.parseString(command, p, milestone));
                }

                // Items
                for (ItemStack item : stagedReward.getReward().getItems()) {
                    p.getInventory().addItem(item);
                }
            }
        }
    }
}

