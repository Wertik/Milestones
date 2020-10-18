package space.devport.wertik.milestones.system.action.struct.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.system.action.struct.AbstractActionListener;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.ConditionRegistry;
import space.devport.wertik.milestones.system.milestone.struct.condition.impl.MessageCondition;

import java.util.Arrays;
import java.util.List;

public class PlayerActionListener extends AbstractActionListener {

    public PlayerActionListener(MilestonesPlugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull List<String> getRegisteredActions() {
        return Arrays.asList("join", "quit", "chat");
    }

    @Override
    public void registerConditions(@NotNull ConditionRegistry registry) {
        registry.setInstanceCreator("chat", MessageCondition::new);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {

        final Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            ActionContext context = new ActionContext();
            context.fromPlayer(player);
            handle("join", player, context);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {

        final Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            ActionContext context = new ActionContext();
            context.fromPlayer(player);
            handle("quit", player, context);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {

        final Player player = event.getPlayer();
        final String message = event.getMessage();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            ActionContext context = new ActionContext();
            context.fromPlayer(player)
                    .add(message);
            handle("chat", player, context);
        });
    }
}
