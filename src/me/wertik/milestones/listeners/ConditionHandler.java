package me.wertik.milestones.listeners;

import me.wertik.milestones.ConfigLoader;
import me.wertik.milestones.DataHandler;
import me.wertik.milestones.Main;
import me.wertik.milestones.objects.Condition;
import me.wertik.milestones.objects.Milestone;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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

    public void process(String type, String targetType, Player p) {

        List<Milestone> milestones = cload.getMilestones();

        for (Milestone milestone : milestones) {

            // Toggle checkers
            // player


            Condition condition = milestone.getCondition();

            if (!condition.getType().equalsIgnoreCase(type))
                continue;

            // Check for the target first, easier.
            if (condition.getTargetTypes().contains(targetType)) {

                if (condition.getBiomes().contains(p.getLocation().getBlock().getBiome().toString()))

                    // check tool type
                    if (condition.getToolTypes().contains(p.getInventory().getItemInMainHand().getType().toString()) || condition.getType().equalsIgnoreCase("blockplace")) {

                        // inventory items
                        for (String itemType : condition.getInInventory()) {

                            if (p.getInventory().contains(Material.valueOf(itemType)))
                                continue;
                            else
                                return;
                        }

                        // Rewards

                        if (milestone.isGlobal()) {
                            dataHandler.addGlobalScore(milestone.getName());
                        } else {
                            if (milestone.isOnlyOnce()) {
                                if (!dataHandler.isLogged(p.getName(), milestone.getName()))
                                    dataHandler.addScore(p.getName(), milestone.getName());
                            } else
                                dataHandler.addScore(p.getName(), milestone.getName());
                        }

                        // Messages
                        if (milestone.isBroadcast()) {
                            for (Player t : plugin.getServer().getOnlinePlayers()) {
                                t.sendMessage(milestone.getBroadcastMessage());
                            }
                        }

                        if (milestone.isInform()) {
                            p.sendMessage(milestone.getInformMessage());
                        }

                        // Commands
                        for (String command : milestone.getCommandsReward()) {
                            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
                        }
                    }
            }
        }
    }
}

