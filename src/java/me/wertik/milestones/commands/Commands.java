package me.wertik.milestones.commands;

import me.wertik.milestones.ConfigLoader;
import me.wertik.milestones.Main;
import me.wertik.milestones.handlers.DataHandler;
import me.wertik.milestones.handlers.StorageHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private Main plugin;
    private Messanger messanger;
    private DataHandler dataHandler;
    private StorageHandler storageHandler;
    private ConfigLoader configLoader;

    public Commands() {
        plugin = Main.getInstance();
        messanger = new Messanger();
        configLoader = plugin.getConfigLoader();
        storageHandler = plugin.getStorageHandler();
        dataHandler = plugin.getDataHandler();
    }

    /*
     * Soo... plans:
     * /milestones
     * (aliases: mile, miles)
     *
     * /mile reload == reload yaml paths
     * /mile toggle (player/*) (world/*) (milestone/*) == toggle data logging
     * /mile clear (player/*) == clear data
     * /mile list (params::planned) == list milestone data
     * /mile stats (player/global)
     *
     * */

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // Fuu..
        if (args.length == 0) {
            messanger.help(sender);
        } else {

            switch (args[0]) {
                case "reload":
                    Main.getInstance().reload();
                    sender.sendMessage("§3Should be reloaded.");
                    break;
                case "toggle":
                    // /mile toggle player/*
                    if (args.length > 4) {
                        sender.sendMessage("§cToo many args.");
                        sender.sendMessage("§c/mile toggle (player/*) (world/*) (milestone/*)");
                        return true;
                    } else if (args.length < 4) {
                        sender.sendMessage("§cNot enough arguments.");
                        sender.sendMessage("§c/mile toggle (player/*) (world/*) (milestone/*)");
                        return true;
                    }

                    String playerName = args[1];
                    String worldName = args[2];
                    String milestoneName = args[3];

                    if (!Main.getInstance().getConfigLoader().getMileNames().contains(milestoneName)) {
                        sender.sendMessage("§cThat milestone does not exist.");
                        sender.sendMessage("§c/mile toggle (player/*) (world/*) (milestone/*)");
                        sender.sendMessage("§e! §7See /mile list for available ones.");
                        return true;
                    }

                    Main.getInstance().getStorageHandler().toggleLogger(playerName, milestoneName, worldName);

                    if (playerName.equalsIgnoreCase("*")) {
                        playerName = "all players";
                    }

                    if (worldName.equalsIgnoreCase("*")) {
                        worldName = "all worlds";
                    }
                    sender.sendMessage("§3Logger of §f" + milestoneName + " §3for §f" + playerName + " §3in §f" + worldName + "§3toggled to §f" + Main.getInstance().getStorageHandler().isToggled(args[1], args[3], args[2]) + " §3state.");
                    sender.sendMessage("§e! §7You can fix any errors in datastorage.yml file. Listed players are disabled.");
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
                            if (configLoader.getGlobalMileNames().contains(args[2])) {
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

                        if (configLoader.getPersonalMileNames().contains(args[2])) {
                            dataHandler.clearScore(args[1], args[2]);
                            sender.sendMessage("§cDone.");
                        } else
                            sender.sendMessage("§cStop bullshitting me, that one's not real.");
                    }
                    break;
                case "stats":
                    // /mile stats (player/global)

                    // length checkers.
                    if (args.length == 1) {
                        // data for sender (if player)
                        if (sender instanceof Player) {
                            messanger.stats(sender, sender.getName());
                            return true;
                        } else
                            sender.sendMessage("§cYou're not a player. Console don't play stuff.");

                    } else if (args.length > 2) {
                        sender.sendMessage("§cThat's too much args, /mile stats (player/'global') ");
                        return true;
                    }

                    // global
                    if (args[1].equalsIgnoreCase("global")) {

                        messanger.statsGlobal(sender);

                        // player
                    } else {

                        // target
                        String targetName = args[1];

                        if (dataHandler.isLogged(targetName)) {
                            messanger.stats(sender, targetName);
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

                    messanger.list(sender);
                    break;
                case "info":
                    if (args.length > 2) {
                        sender.sendMessage("§cThat's too much tho, stop it. /mile info (milestone)");
                    } else if (args.length < 2) {
                        sender.sendMessage("§cSpecify milestone name plz.");
                    } else {

                        if (configLoader.getMileNames().contains(args[1])) {
                            messanger.info(sender, args[1]);
                        } else
                            sender.sendMessage("§cThat one is from another universe. Use the list to display the ones from Earth-12");

                    }
                    break;
                case "additem":
                    // /mile additem (name)
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("§cOnly players please.");
                        return false;
                    }

                    Player p = (Player) sender;

                    if (args.length < 2) {
                        p.sendMessage("§cNot enough. /mile additem (name)");
                        return false;
                    } else if (args.length > 2) {
                        p.sendMessage("§cThat's too much,.. /mile additem (name)");
                        return false;
                    }

                    if (p.getInventory().getItemInMainHand().getType().toString().equals("AIR")) {
                        p.sendMessage("§cAir is useless to save, instead, consider breathing it.");
                        return false;
                    }

                    storageHandler.saveItem(args[1], p.getInventory().getItemInMainHand());
                    p.sendMessage("§3Item added. Use §f%item_" + args[1] + "§f%§3 to reference it.");
                    break;
                case "getitem":
                    // /mile getitem (name)
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("§cOnly players please.");
                        return false;
                    }

                    p = (Player) sender;

                    if (args.length < 2) {
                        p.sendMessage("§cNot enough. /mile getitem (name)");
                        return false;
                    } else if (args.length > 2) {
                        p.sendMessage("§cThat's too much,.. /mile getitem (name)");
                        return false;
                    }

                    if (!storageHandler.getItemNames().contains(args[1])) {
                        p.sendMessage("§cThat item is not listed.");
                        return false;
                    }

                    p.getInventory().addItem(storageHandler.getItem(args[1]));
                    p.sendMessage("§3Given!");
                    break;
                default:
                    messanger.help(sender);
                    break;
            }
        }
        return false;
    }
}
