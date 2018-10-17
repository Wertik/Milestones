package me.wertik.milestones.listeners;

import me.wertik.milestones.Main;
import me.wertik.milestones.handlers.ConditionHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private ConditionHandler conditionHandler = Main.getInstance().getConditionHandler();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        conditionHandler.process("blockbreak", e.getBlock().getType().toString(), e.getPlayer());
    }
}
