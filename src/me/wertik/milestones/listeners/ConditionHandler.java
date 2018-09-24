package me.wertik.milestones.listeners;

import me.wertik.milestones.ConfigLoader;
import me.wertik.milestones.DataHandler;
import me.wertik.milestones.objects.Condition;
import me.wertik.milestones.objects.Milestone;
import org.bukkit.Bukkit;
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

    public void process(String type, String targetType, Player p) {

        List<Milestone> milestones = cload.getMilestones();

        for (Milestone milestone : milestones) {

            Condition condition = milestone.getCondition();

            if (!condition.getType().equalsIgnoreCase(type))
                continue;

            Bukkit.broadcastMessage("§aCheck 1!");

            // Check for the target first, easier.
            if (condition.getTargetTypes().contains(targetType)) {

                Bukkit.broadcastMessage("§aCheck 2!");

                // check tool type
                if (condition.getToolTypes().contains(p.getInventory().getItemInMainHand().getType().toString()) || condition.getType().equalsIgnoreCase("blockplace")) {

                    Bukkit.broadcastMessage("§aCheck 3!");

                    // inventory items
                    for (String itemType : condition.getInInventory()) {

                        if (p.getInventory().contains(Material.valueOf(itemType)))
                            continue;
                        else
                            return;
                    }

                    Bukkit.broadcastMessage("§aCheck 4!");

                    if (milestone.isOnlyOnce()) {
                        if (dataHandler.isLogged(p.getName(), milestone.getName())) {
                            return;
                        } else {
                            p.sendMessage("§3Ok, you good. Adding a point, but only once!");
                            if (!milestone.isGlobal())
                                dataHandler.addScore(p.getName(), milestone.getName());
                            else
                                dataHandler.addGlobalScore(milestone.getName());
                            return;
                        }
                    } else {
                        p.sendMessage("§3Ok, you good. Adding a point!");
                        if (!milestone.isGlobal())
                            dataHandler.addScore(p.getName(), milestone.getName());
                        else
                            dataHandler.addGlobalScore(milestone.getName());
                    }
                }
            }
        }
    }
}
