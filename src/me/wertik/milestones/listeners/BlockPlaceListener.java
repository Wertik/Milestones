package me.wertik.milestones.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    ConditionHandler conditionHandler = new ConditionHandler();

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        conditionHandler.process("blockplace", e.getBlock().getType().toString(), e.getPlayer());
    }
}
