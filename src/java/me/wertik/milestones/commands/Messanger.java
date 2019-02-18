package me.wertik.milestones.commands;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.wertik.milestones.ConfigLoader;
import me.wertik.milestones.Main;
import me.wertik.milestones.objects.Milestone;
import me.wertik.milestones.objects.StagedReward;
import org.bukkit.command.CommandSender;

public class Messanger {

    private Main plugin;
    private ConfigLoader configLoader;
    private WorldGuardPlugin worldGuard;

    public Messanger() {
        plugin = Main.getInstance();
        configLoader = plugin.getConfigLoader();
        worldGuard = plugin.getWorldGuard();
    }

    // Methods to simplify sending messages in commands. (It looks easier only for me)
    /*
     *
     * I'm gonna add some language file after completing the system, don't worry.
     *
     * */

    public void help(CommandSender p) {
        p.sendMessage("§8§m----§r §3Milestones Help Page §8§m----§r");
        p.sendMessage("§3/mile reload §8- §7Reloads the config.\n" +
                "§3/mile toggle (player/global/*) (milestone/*) §8- §7Toggles data collection.\n" +
                "§3/mile clear (player/global/*) (milestone/*) §8- §7Clears stored data.\n" +
                "§3/mile list §8- §7List milestones.\n" +
                "§3/mile stats (player/global) §8- §7Player/global milestone stats.\n" +
                "§3/mile info (milestone) §8- §7Info about a milestone.\n" +
                "§3/mile additem (name) §8- §7Adds item in your hand to the data storage by a name.\n"
        );
    }

    public void stats(CommandSender p, String targetName) {
        p.sendMessage("§8§m--§r §3Scores for player §f" + targetName + " §8§m--");
        Main.getInstance().getConfigLoader().getPersonalMilestones().forEach(milestone -> p.sendMessage("§3" + milestone.getName() + " §8- §f" + Main.getInstance().getDataHandler().getScore(targetName, milestone.getName())));
    }

    public void info(CommandSender p, String milstoneName) {
        Milestone milestone = configLoader.getMilestone(milstoneName);

        p.sendMessage("§dInfo about §f" + milestone.getName());

        // print info
        p.sendMessage(" §3Display name: §f" + milestone.getDisplayName());
        p.sendMessage(" §3Global: §f" + milestone.isGlobal());
        p.sendMessage(" §3Only once: §f" + milestone.isOnlyOnce());
        p.sendMessage(" §3Type: §f" + milestone.getConditions().type().toString());

        if (!milestone.getReward().getBroadcastMessage().equals("") && !milestone.getReward().getInformMessage().equals("") && !milestone.getReward().getCommands().isEmpty() && !milestone.getReward().getRewardItems().isEmpty()) {
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
            if (!milestone.getReward().getRewardItems().isEmpty()) {
                p.sendMessage("  §3Items:");
                for (String itemName : milestone.getReward().getRewardItemsList()) {
                    p.sendMessage(" §8- §f" + itemName);
                }
            }
        }

        if (!milestone.getStagedRewards().isEmpty()) {
            p.sendMessage(" §aStaged:");
            for (StagedReward stagedReward : milestone.getStagedRewards()) {
                p.sendMessage("  §8- §f" + stagedReward.getCount());
            }
        }

        p.sendMessage(" §aConditions:");
        if (!milestone.getConditions().baseCondition().getInHandItems().isEmpty()) {
            p.sendMessage("  §3Tool Types:");
            for (String itemName : milestone.getConditions().baseCondition().getInHandItemsList()) {
                p.sendMessage("  §8- §f" + itemName);
            }
        }

        if (!milestone.getConditions().getTargetTypes().isEmpty()) {
            p.sendMessage("  §3Target Types:");
            for (String target : milestone.getConditions().getTargetTypes()) {
                p.sendMessage("  §8- §f" + target);
            }
        }

        if (!milestone.getConditions().baseCondition().getInInventoryList().isEmpty()) {
            p.sendMessage("  §3In-inventory item types:");
            for (String itemName : milestone.getConditions().baseCondition().getInInventoryList()) {
                p.sendMessage(" §8- §f" + itemName);
            }
        }

        if (!milestone.getConditions().baseCondition().getBiomeTypes().isEmpty()) {
            p.sendMessage("  §3Biome types:");
            for (String biome : milestone.getConditions().baseCondition().getBiomeTypes()) {
                p.sendMessage("  §8- §f" + biome);
            }
        }

        if (!milestone.getConditions().baseCondition().getWorldNames().isEmpty()) {
            p.sendMessage("  §3World name:");
            for (String worldName : milestone.getConditions().baseCondition().getWorldNames()) {
                p.sendMessage("  §8- §f" + worldName);
            }
        }

        if (worldGuard != null) {
            if (!milestone.getConditions().baseCondition().getRegionNames().isEmpty()) {
                p.sendMessage("  §3Regions:");
                for (String region : milestone.getConditions().baseCondition().getRegionNames()) {
                    p.sendMessage("  §8- §f" + region);
                }
            }
        }
    }

    public void list(CommandSender p) {
        ConfigLoader configLoader = plugin.getConfigLoader();
        String types = "§3List of milestones:§f";
        for (String name : configLoader.getMileNames()) {
            if (configLoader.getMileNames().indexOf(name) == (configLoader.getMileNames().size() - 1)) {
                types = types + " " + name + ".";
                break;
            }
            types = types + " " + name + ",";
        }
        p.sendMessage(types);
    }

    public void statsGlobal(CommandSender p) {
        p.sendMessage("§8§m--§r §3Global scores §8§m--");
        Main.getInstance().getConfigLoader().getGlobalMileNames().forEach(milestone -> p.sendMessage("§3" + milestone + " §8- §f" + Main.getInstance().getDataHandler().getGlobalScore(milestone)));
    }
}
