package me.wertik.milestones.listeners;

import me.wertik.milestones.handlers.ConditionHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    ConditionHandler conditionHandler = new ConditionHandler();

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        conditionHandler.process("playerquit", null, e.getPlayer());
    }
}
