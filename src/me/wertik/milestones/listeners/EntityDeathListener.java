package me.wertik.milestones.listeners;

import me.wertik.milestones.Main;
import me.wertik.milestones.handlers.ConditionHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {

    private ConditionHandler conditionHandler = Main.getInstance().getConditionHandler();

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() instanceof Player)
            conditionHandler.process("entitykill", e.getEntity().getType().toString(), e.getEntity().getKiller());
    }
}
