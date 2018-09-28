package me.wertik.milestones.listeners;

import me.wertik.milestones.handlers.ConditionHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    ConditionHandler conditionHandler = new ConditionHandler();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        conditionHandler.process("playerchat", e.getMessage(), e.getPlayer());
    }
}
