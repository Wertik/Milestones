package me.wertik.milestones.objects;

import me.wertik.milestones.ConfigLoader;
import me.wertik.milestones.Main;
import me.wertik.milestones.Utils;
import me.wertik.milestones.handlers.StorageHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Reward {

    private StorageHandler storageHandler;
    private Main plugin;
    private ConfigLoader configLoader;

    // Broadcast to all players
    private List<String> broadcastMessage;

    // Seend to player
    private List<String> informMessage;

    // Commands executed by console
    private List<String> consoleCommands;

    // Commands executed by player
    private List<String> playerCommands;

    // Items to give to the player
    private List<ItemStack> rewardItems;
    private List<String> rewardItemsList;

    public Reward(List<String> consoleCommands, List<String> playerCommands, List<String> rewardItemsList, List<String> informMessage, List<String> broadcastMessage) {
        this.broadcastMessage = Utils.checkStringList(broadcastMessage);
        this.consoleCommands = Utils.checkStringList(consoleCommands);
        this.playerCommands = Utils.checkStringList(playerCommands);
        this.informMessage = Utils.checkStringList(informMessage);
        this.rewardItemsList = Utils.checkStringList(rewardItemsList);

        plugin = Main.getInstance();
        configLoader = plugin.getConfigLoader();
        storageHandler = plugin.getStorageHandler();

        this.rewardItems = storageHandler.parseForItemPlaceholders(Utils.checkStringList(rewardItemsList));
    }

    public List<String> getRewardItemsList() {
        return rewardItemsList;
    }

    public List<String> getBroadcastMessage() {
        return broadcastMessage;
    }

    public List<String> getInformMessage() {
        return informMessage;
    }

    public List<String> getConsoleCommands() {
        return consoleCommands;
    }

    public List<ItemStack> getRewardItems() {
        return rewardItems;
    }

    public List<String> getPlayerCommands() {
        return playerCommands;
    }

    // Easier.. ;)
    public void give(Player player, Milestone milestone) {

        // Messages
        if (!broadcastMessage.isEmpty())
            for (Player target : plugin.getServer().getOnlinePlayers()) {
                broadcastMessage.forEach(line -> target.sendMessage(Utils.color(Utils.parse(line, player, milestone))));
            }

        if (!informMessage.isEmpty())
            informMessage.forEach(line -> player.sendMessage(Utils.color(Utils.parse(line, player, milestone))));

        // Commands
        if (!consoleCommands.isEmpty())
            consoleCommands.forEach(command -> plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), Utils.parse(command, player, milestone)));

        // Player Commands
        if (!playerCommands.isEmpty())
            playerCommands.forEach(command -> player.performCommand(Utils.parse(command, player, milestone)));

        // Items
        if (!rewardItems.isEmpty())
            rewardItems.forEach(item -> player.getInventory().addItem(item));
    }
}
