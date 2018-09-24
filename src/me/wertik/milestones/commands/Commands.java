package me.wertik.milestones.commands;

import me.wertik.milestones.ConfigLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    ConfigLoader cload = new ConfigLoader();

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
                    "/mile toggle (player/all) (specific/\"\") == toggle data logging\n" +
                    "/mile clear (player/all) == clear data\n" +
                    "/mile remove == remove data\n" +
                    "/mile list (params::planned) == list milestone data\n" +
                    "/mile stats (player/global)");
        } else {

            switch (args[0]) {
                case "reload":
                case "toggle":
                case "clear":
                case "remove":
                case "stats":
                    // /mile stats (player/global)

                    // lenght checkers.
                    if (args.length == 1) {
                        // data for sender (if player)
                        if (sender instanceof Player) {



                        }
                    } else if (args.length > 2) {

                    }

                    break;
                case "list":
            }


        }
        return false;
    }
}
