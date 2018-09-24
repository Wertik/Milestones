package me.wertik.milestones.commands;

import me.wertik.milestones.ConfigLoader;
import me.wertik.milestones.DataHandler;
import me.wertik.milestones.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    ConfigLoader cload = new ConfigLoader();
    DataHandler dataHandler = new DataHandler();
    Main plugin = Main.getInstance();
    Messanger mess = new Messanger();

    /*
     * Soo... plans:
     * /milestones
     * (aliases: mile, miles)
     *
     * /mile reload == reload yaml paths
     * /mile toggle (player/all) (specific/"") == toggle data logging
     * /mile clear (player/all) == clear data
     * /mile remove == remove data
     * /mile list (params::planned) == list milestone data
     * /mile stats (player/global)
     *
     * */

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // Fuu..
        if (args.length == 0) {
            sender.sendMessage("This should be the help page.");
            sender.sendMessage("/mile reload == reload yaml paths\n" +
                    "/mile toggle (player/global/*) (milestone/*) == toggle data collecting\n" +
                    "/mile clear (player/global/*) (milestone/*) == clear data\n" +
                    "/mile list (params::planned) == list milestone data\n" +
                    "/mile stats (player/global) == player/global stats\n" +
                    "/mile info (milestone) == info about a milestone\n" +
                    "/mile credits == authors, version, stuff."
            );
        } else {

            switch (args[0]) {
                case "reload":
                    cload.loadMilestones();
                    cload.loadYamls();
                    dataHandler.loadFiles();
                    plugin.reloadConfig();
                    sender.sendMessage("§3Should be reloaded.");
                    break;
                case "toggle":

                    break;
                case "clear":

                    if (args.length < 3) {
                        sender.sendMessage("§cThat's not enough.. /mile clear (player/global/*) (milestone/*)");
                        return true;
                    } else if (args.length > 3) {
                        sender.sendMessage("§cThat's too much. Stop plox.");
                        return true;
                    }

                    // /mile clear (player/global/*) (milestone/*)
                    if (args[1].equalsIgnoreCase("global")) {

                        if (args[2].equalsIgnoreCase("*")) {
                            dataHandler.clearGlobalScores();
                            sender.sendMessage("§cData wipe you requested should be done sir.");
                        } else {
                            if (cload.getGlobalMileNames().contains(args[2])) {
                                dataHandler.clearGlobalScore(args[2]);
                                sender.sendMessage("§cData wipe you requested should be done sir.");
                            } else
                                sender.sendMessage("§cThat milestone is non-existant.");
                        }

                        return true;
                    } else if (args[1].equalsIgnoreCase("*")) {

                        if (args[2].equalsIgnoreCase("*")) {
                            dataHandler.clearScores();
                            sender.sendMessage("§cIt's gone.. and it's your fault..");
                            return true;
                        }

                        dataHandler.clearMilestoneScores(args[2]);
                        sender.sendMessage("§cWipeout performed!");
                        return true;
                    } else {
                        // player
                        if (args[2].equalsIgnoreCase("*")) {
                            dataHandler.clearPlayerScore(args[1]);
                            sender.sendMessage("§cAaaand he's out.");
                            return true;
                        }

                        if (cload.getPersonalMileNames().contains(args[2])) {
                            dataHandler.clearScore(args[1], args[2]);
                            sender.sendMessage("§cDone.");
                        } else
                            sender.sendMessage("§cStop bullshitting me, that one's not real.");
                    }
                    break;
                case "stats":
                    // /mile stats (player/global)

                    // lenght checkers.
                    if (args.length == 1) {
                        // data for sender (if player)
                        if (sender instanceof Player) {
                            mess.stats(sender, sender.getName());
                            return true;
                        } else
                            sender.sendMessage("§cYou're not a player. Console don't play stuff.");

                    } else if (args.length > 2) {
                        sender.sendMessage("§cThat's too much args, /mile stats (player/'global') ");
                        return true;
                    }

                    // global
                    if (args[1].equalsIgnoreCase("global")) {

                        mess.statsGlobal(sender);

                        // player
                    } else {

                        // target
                        String targetName = args[1];

                        if (dataHandler.isLogged(targetName)) {
                            mess.stats(sender, targetName);
                            return true;
                        } else
                            sender.sendMessage("§cPlayer is not logged, or you just made his name up.");
                    }
                    break;
                case "list":

                    if (args.length > 1) {
                        sender.sendMessage("§cToo many args tho. /mile list");
                        return true;
                    }

                    mess.list(sender);
                    break;
                case "info":
                    if (args.length > 2) {
                        sender.sendMessage("§cThat's too much tho, stop it. /mile info (milestone)");
                    } else if (args.length < 2) {
                        sender.sendMessage("§cSpecify milestone name plz.");
                    } else {

                        if (cload.getMileNames().contains(args[1])) {
                            mess.info(sender, args[1]);
                        } else
                            sender.sendMessage("§cThat one is from another universe. Use the list to display the ones from Earth-12");

                    }
                    break;
                case "credits":
                    sender.sendMessage("§fWertik1206 §3could say 'I am your father' to me. Noone else.");
                    sender.sendMessage("§3Current version is §f" + plugin.getDescription().getVersion());
                    break;
            }
        }
        return false;
    }
}
