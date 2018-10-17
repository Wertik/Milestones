package me.wertik.milestones.listeners;

import me.wertik.milestones.Main;
import me.wertik.milestones.handlers.ConditionHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    private ConditionHandler conditionHandler = Main.getInstance().getConditionHandler();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        conditionHandler.process("blockbreak", e.getBlock().getType().toString(), e.getPlayer());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        conditionHandler.process("blockplace", e.getBlock().getType().toString(), e.getPlayer());
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() instanceof Player)
            conditionHandler.process("entitykill", e.getEntity().getType().toString(), e.getEntity().getKiller());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        conditionHandler.process("playerchat", e.getMessage(), e.getPlayer());
    }

    @EventHandler
    public void onPlace(PlayerJoinEvent e) {
        conditionHandler.process("playerjoin", null, e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        conditionHandler.process("playerquit", null, e.getPlayer());
    }

}
