package me.wertik.milestones.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {

    ConditionHandler conditionHandler = new ConditionHandler();

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() instanceof Player)
            conditionHandler.process("entitykill", e.getEntity().getType().toString(), e.getEntity().getKiller());
    }
}
