package me.wertik.milestones.listeners;

import me.wertik.milestones.events.EntityKillMilestoneCollectEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TestListner implements Listener {

    @EventHandler
    public void onMilestoneCollect(EntityKillMilestoneCollectEvent e) {
        e.getPlayer().sendMessage("§cCollect event fired.. ");
    }
}
