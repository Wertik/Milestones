package me.wertik.milestones.objects;

import me.wertik.milestones.Main;
import me.wertik.milestones.handlers.StorageHandler;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Reward {

    private StorageHandler storageHandler = Main.getInstance().getStorageHandler();

    private String broadcastMessage;
    private String informMessage;
    private List<String> commands;
    private List<String> itemNames;

    public Reward(List<String> commands, List<String> itemNames, String informMessage, String broadcastMessage) {
        this.broadcastMessage = broadcastMessage;
        this.commands = commands;
        this.itemNames = itemNames;
        this.informMessage = informMessage;
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

    public List<String> getItemNames() {
        return itemNames;
    }

    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();

        for (String name : itemNames) {
            items.add(storageHandler.getItem(name));
        }
        return items;
    }
}
