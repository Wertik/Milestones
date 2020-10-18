package space.devport.wertik.milestones.system.action.struct.impl;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.system.action.struct.AbstractActionListener;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.milestone.struct.condition.ConditionRegistry;
import space.devport.wertik.milestones.system.milestone.struct.condition.impl.MessageCondition;

import java.util.Collections;
import java.util.List;

public class PlayerActionListener extends AbstractActionListener {

    public PlayerActionListener(MilestonesPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ActionContext context = new ActionContext();
        context.fromPlayer(event.getPlayer());
        handle("join", event.getPlayer(), context);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        ActionContext context = new ActionContext();
        context.fromPlayer(event.getPlayer());
        handle("quit", event.getPlayer(), context);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        ActionContext context = new ActionContext();
        context.fromPlayer(event.getPlayer())
                .add(event.getMessage());
        handle("chat", event.getPlayer(), context);
    }

    @Override
    public void registerConditionLoaders(ConditionRegistry registry) {
        registry.setInstanceCreator("chat", MessageCondition::new);
    }

    @Override
    public List<String> getRegisteredActions() {
        return Collections.singletonList("join");
    }
}
