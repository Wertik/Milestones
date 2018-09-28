package me.wertik.milestones.listeners;

import me.wertik.milestones.handlers.ConditionHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    ConditionHandler conditionHandler = new ConditionHandler();

    @EventHandler
    public void onPlace(PlayerJoinEvent e) {
        conditionHandler.process("playerjoin", null, e.getPlayer());
    }
}
