package me.wertik.milestones.listeners;

import me.wertik.milestones.Main;
import me.wertik.milestones.handlers.ConditionHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private ConditionHandler conditionHandler = Main.getInstance().getConditionHandler();

    @EventHandler
    public void onPlace(PlayerJoinEvent e) {
        conditionHandler.process("playerjoin", null, e.getPlayer());
    }
}
