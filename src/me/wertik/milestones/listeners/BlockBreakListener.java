package me.wertik.milestones.listeners;

import me.wertik.milestones.ConfigLoader;
import me.wertik.milestones.DataHandler;
import me.wertik.milestones.objects.Condition;
import me.wertik.milestones.objects.Milestone;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class BlockBreakListener implements Listener {

    ConfigLoader cload = new ConfigLoader();
    DataHandler dataHandler = new DataHandler();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        List<Milestone> milestones = cload.getMilestones();

        for (Milestone milestone : milestones) {
            Condition condition = milestone.getCondition();

            // Check for the target first, easier.
            if (condition.getTargetTypes().contains(e.getBlock().getType().toString())) {

                // check tool type
                if (condition.getToolTypes().contains(e.getPlayer().getInventory().getItemInMainHand().getType().toString())) {

                    // inventory items
                    for (String itemType : condition.getInInventory()) {

                        if (e.getPlayer().getInventory().contains(Material.valueOf(itemType)))
                            continue;
                        else
                            return;
                    }

                    if (milestone.isOnlyOnce()) {
                        if (dataHandler.isLogged(e.getPlayer(), milestone.getName())) {
                            return;
                        } else {
                            e.getPlayer().sendMessage("§3Ok, you good. Adding a point!");
                            if (!milestone.isGlobal())
                                dataHandler.addScore(e.getPlayer(), milestone.getName());
                            else
                                dataHandler.addGlobalScore(milestone.getName());
                        }
                    } else {
                        e.getPlayer().sendMessage("§3Ok, you good. Adding a point!");
                        if (!milestone.isGlobal())
                            dataHandler.addScore(e.getPlayer(), milestone.getName());
                        else
                            dataHandler.addGlobalScore(milestone.getName());
                    }
                }
            }
        }
    }
}
