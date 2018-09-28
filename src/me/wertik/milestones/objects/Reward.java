package me.wertik.milestones.objects;

import java.util.List;

public class Reward {

    private String broadcastMessage;
    private String informMessage;
    private List<String> commands;
    private List<String> itemNames;

    public Reward(List<String> commands, List<String> itemNames, String informMessage, String broadcastMessage) {
        this.broadcastMessage = broadcastMessage;
        this.informMessage = informMessage;
        this.commands = commands;
        this.itemNames = itemNames;
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
}
