package me.wertik.milestones.commands;

import me.wertik.milestones.ConfigLoader;
import me.wertik.milestones.objects.Condition;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor {

    ConfigLoader cload = new ConfigLoader();

    /*
     * Soo... plans:
     * /milestones
     * (aliases: mile, miles)
     * /mile reset
     * /mile reload
     * /mile toggle (player/all) (world/all) (specific/"")
     * */

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Condition condition = cload.getCondition("testing-milestone");

        List<String> infolist = new ArrayList<>();

        infolist.add("Type: " + condition.getType());
        infolist.add("InInventoryTypes: " + condition.getInInventory());
        infolist.add("TargetTypes: " + condition.getTargetTypes());

        switch (condition.getType()) {
            case "blockbreak":
                infolist.add("ToolTypes: " + condition.getToolTypes());
                break;
            case "blockplace":
                break;
            case "entitykill":
                infolist.add("ToolTypes: " + condition.getToolTypes());
                break;
        }

        for (String line : infolist) {
            commandSender.sendMessage(line);
        }

        return false;
    }
}
