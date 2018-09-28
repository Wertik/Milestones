package me.wertik.milestones.listeners;

import me.wertik.milestones.handlers.ConditionHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    ConditionHandler conditionHandler = new ConditionHandler();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        conditionHandler.process("blockbreak", e.getBlock().getType().toString(), e.getPlayer());
    }
}
