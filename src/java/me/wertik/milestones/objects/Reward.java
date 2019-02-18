package me.wertik.milestones.objects;

import me.wertik.milestones.ConfigLoader;
import me.wertik.milestones.Main;
import me.wertik.milestones.handlers.StorageHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Reward {

    private StorageHandler storageHandler;
    private Main plugin;
    private ConfigLoader configLoader;

    // Broadcast to all players
    private String broadcastMessage;

    // Seend to player
    private String informMessage;

    // Commands executed by console
    private List<String> commands;

    // Items to give to the player
    private List<ItemStack> rewardItems;
    private List<String> rewardItemsList;

    // Todo rewrite messages to String List
    public Reward(List<String> commands, List<String> rewardItemsList, String informMessage, String broadcastMessage) {
        this.broadcastMessage = broadcastMessage;
        this.commands = commands;
        this.informMessage = informMessage;
        this.rewardItemsList = rewardItemsList;

        plugin = Main.getInstance();
        configLoader = plugin.getConfigLoader();
        storageHandler = plugin.getStorageHandler();

        this.rewardItems = storageHandler.parseForItemPlaceholders(rewardItemsList);
    }

    public List<String> getRewardItemsList() {
        return rewardItemsList;
    }

    public String getBroadcastMessage() {
        return broadcastMessage;
    }

    public String getInformMessage() {
        return informMessage;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<ItemStack> getRewardItems() {
        return rewardItems;
    }

    // Easier.. ;)
    public void give(Player player, Milestone milestone) {
        // Messages
        if (!broadcastMessage.equals(""))
            for (Player target : plugin.getServer().getOnlinePlayers()) {
                target.sendMessage(configLoader.getFinalString(broadcastMessage, player, milestone));
            }

        if (!informMessage.equals(""))
            player.sendMessage(configLoader.getFinalString(informMessage, player, milestone));


        // Commands
        for (String command : milestone.getReward().getCommands())
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), configLoader.parseString(command, player, milestone));


        // Items
        for (ItemStack item : rewardItems)
            player.getInventory().addItem(item);
    }
}
