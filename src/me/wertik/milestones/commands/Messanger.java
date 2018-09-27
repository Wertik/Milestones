package me.wertik.milestones.commands;

import me.wertik.milestones.ConfigLoader;
import me.wertik.milestones.DataHandler;
import me.wertik.milestones.objects.Milestone;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Messanger {

    DataHandler dataHandler = new DataHandler();
    ConfigLoader cload = new ConfigLoader();

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
        p.sendMessage(" §3Broadcast message: §f" + milestone.getBroadcastMessage());
        p.sendMessage(" §3Inform message: §f" + milestone.getInformMessage());
        p.sendMessage(" §3Commands:");
        for (String command : milestone.getCommandsReward()) {
            p.sendMessage(" §8- §f" + command);
        }
        p.sendMessage(" §aConditions:");
        if (milestone.getCondition().getToolTypes() != null) {
            p.sendMessage("  §3Tool Types:");
            for (String tool : milestone.getCondition().getToolTypes()) {
                p.sendMessage("  §8- §f" + tool);
            }
        }
        if (milestone.getCondition().getTargetTypes() != null) {
            p.sendMessage("  §3Target Types:");
            for (String target : milestone.getCondition().getTargetTypes()) {
                p.sendMessage("  §8- §f" + target);
            }
        }
        if (milestone.getCondition().getInInventory() != null) {
            p.sendMessage("  §3In-inventory item types:");
            for (String item : milestone.getCondition().getInInventory()) {
                p.sendMessage("  §8- §f" + item);
            }
        }
        if (milestone.getCondition().getBiomes() != null) {
            p.sendMessage("  §3Biome types:");
            for (String biome : milestone.getCondition().getBiomes()) {
                p.sendMessage("  §8- §f" + biome);
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
