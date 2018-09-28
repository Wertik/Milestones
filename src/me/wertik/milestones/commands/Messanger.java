package me.wertik.milestones.commands;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.wertik.milestones.ConfigLoader;
import me.wertik.milestones.Main;
import me.wertik.milestones.handlers.DataHandler;
import me.wertik.milestones.objects.Milestone;
import me.wertik.milestones.objects.StagedReward;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Messanger {

    DataHandler dataHandler = new DataHandler();
    ConfigLoader cload = new ConfigLoader();
    Main plugin = Main.getInstance();
    WorldGuardPlugin wg = plugin.getWorldGuard();

    public Messanger() {
    }

    // Methods to simplify sending messages in commands. (It looks easier only for me)
    /*
     *
     * I'm gonna add some language file after completing the system, don't worry.
     *
     * */

    public void help(CommandSender p) {
        p.sendMessage("§dThis should be the help page.");
        p.sendMessage("§3/mile reload §7== §3reload yaml paths\n" +
                "§3/mile toggle (player/global/*) (milestone/*) §7== §3toggle data collecting\n" +
                "§3/mile clear (player/global/*) (milestone/*) §7== §3clear data\n" +
                "§3/mile list (params::planned) §7== " + "§3list milestone data\n" +
                "§3/mile stats (player/global) §7== §3player/global stats\n" +
                "§3/mile info (milestone) §7== §3info about a milestone\n" +
                "§3/mile additem (name) §7== §3add item in your hand to the data storage\n" +
                "§3/mile credits §7== §3authors, version, stuff.");
    }

    public void stats(CommandSender p, String targetName) {

        p.sendMessage("§dScores for player §f" + targetName);

        for (String milestone : cload.getPersonalMileNames()) {
            p.sendMessage("§3Score of §f" + milestone + "§3 is §f" + dataHandler.getScore(targetName, milestone));
        }
    }

    public void info(CommandSender p, String name) {

        Milestone milestone = cload.getMilestone(name);

        p.sendMessage("§dInfo about §f" + milestone.getName());

        // print info
        p.sendMessage(" §3Display name: §f" + milestone.getDisplayName());
        p.sendMessage(" §3Global: §f" + milestone.isGlobal());
        p.sendMessage(" §3Only once: §f" + milestone.isOnlyOnce());
        p.sendMessage(" §3Type: §f" + milestone.getCondition().getType());
        if (!milestone.getReward().getBroadcastMessage().equals("") && !milestone.getReward().getInformMessage().equals("") && !milestone.getReward().getCommands().isEmpty() && !milestone.getReward().getItemNames().isEmpty())
            p.sendMessage(" §aRewards:");
        if (!milestone.getReward().getBroadcastMessage().equals(""))
            p.sendMessage("  §3Broadcast message: §f" + milestone.getReward().getBroadcastMessage());
        if (!milestone.getReward().getInformMessage().equals(""))
            p.sendMessage("  §3Inform message: §f" + milestone.getReward().getInformMessage());
        if (!milestone.getReward().getCommands().isEmpty()) {
            p.sendMessage("  §3Commands:");
            for (String command : milestone.getReward().getCommands()) {
                p.sendMessage(" §8- §f" + command);
            }
        }
        // Items
        if (!milestone.getReward().getItemNames().isEmpty()) {
            p.sendMessage("  §3Items:");
            for (String itemName : milestone.getReward().getItemNames()) {
                p.sendMessage(" §8- §f" + itemName);
            }
        }
        if (milestone.getStagedRewards() != null) {
            p.sendMessage(" §aStaged:");
            for (StagedReward stagedReward : milestone.getStagedRewards()) {
                p.sendMessage("  §8- §f" + stagedReward.getCount());
            }
        }
        p.sendMessage(" §aConditions:");
        if (!milestone.getCondition().getToolTypes().isEmpty()) {
            p.sendMessage("  §3Tool Types:");
            for (ItemStack tool : milestone.getCondition().getToolTypes()) {
                p.sendMessage("  §8- §f" + tool.getType());
            }
        }
        if (!milestone.getCondition().getTargetTypes().isEmpty()) {
            p.sendMessage("  §3Target Types:");
            for (String target : milestone.getCondition().getTargetTypes()) {
                p.sendMessage("  §8- §f" + target);
            }
        }
        if (!milestone.getCondition().getInInventory().isEmpty()) {
            p.sendMessage("  §3In-inventory item types:");
            for (ItemStack item : milestone.getCondition().getInInventory()) {
                p.sendMessage("  §8- §f" + item.getType());
            }
        }
        if (!milestone.getCondition().getBiomes().isEmpty()) {
            p.sendMessage("  §3Biome types:");
            for (String biome : milestone.getCondition().getBiomes()) {
                p.sendMessage("  §8- §f" + biome);
            }
        }
        if (wg != null) {
            if (!milestone.getCondition().getRegionNames().isEmpty()) {
                p.sendMessage("  §3Regions:");
                for (String region : milestone.getCondition().getRegionNames()) {
                    p.sendMessage("  §8- §f" + region);
                }
            }
        }
    }

    public void list(CommandSender p) {
        p.sendMessage("§dList of all milestones:");
        for (String name : cload.getMileNames()) {
            p.sendMessage("§f" + name);
        }
    }

    public void statsGlobal(CommandSender p) {

        List<String> milestones = cload.getGlobalMileNames();

        p.sendMessage("§dGlobal scores:");

        for (String milestone : milestones) {

            p.sendMessage("§3Score of §f" + milestone + " §3is §f" + dataHandler.getGlobalScore(milestone));

        }
    }
}
