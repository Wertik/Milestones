package me.wertik.milestones.commands;

import me.wertik.milestones.ConfigLoader;
import me.wertik.milestones.DataHandler;
import me.wertik.milestones.objects.Milestone;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Messanger {

    DataHandler dataHandler = new DataHandler();
    ConfigLoader cload = new ConfigLoader();

    public Messanger() {
    }

    // Methods to simplify sending messages in commands. (It looks easier only for me)

    public void help(Player p) {
    }

    public void stats(Player p) {
    }

    public void stats(CommandSender p, Player t) {

        p.sendMessage("§3Scores for player §f" + t.getName());

        for (String milestone : cload.getPersonalMileNames()) {
            p.sendMessage("§3Score of §f" + milestone + "§3 is §f" + dataHandler.getScore(t, milestone));
        }
    }

    public void list(Player p) {
    }

    public void statsGlobal(CommandSender p) {

        List<Milestone> milestones = cload.getGlobalMilestones();

        p.sendMessage("§dGlobal scores:");

        for (Milestone milestone : milestones) {

            p.sendMessage("§3Score of §f" + milestone.getName() + " §3is §f" + dataHandler.getGlobalScore(milestone.getName()));

        }
    }
}
