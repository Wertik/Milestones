package space.devport.wertik.milestones.system.action;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import space.devport.utils.ConsoleOutput;
import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.system.action.struct.AbstractActionListener;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.action.struct.impl.BlockActionListener;
import space.devport.wertik.milestones.system.action.struct.impl.EntityActionListener;
import space.devport.wertik.milestones.system.action.struct.impl.PlayerActionListener;
import space.devport.wertik.milestones.system.action.struct.impl.WorldActionListener;
import space.devport.wertik.milestones.system.milestone.struct.Milestone;
import space.devport.wertik.milestones.system.user.struct.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActionRegistry {

    private final MilestonesPlugin plugin;

    private final List<AbstractActionListener> loadedListeners = new ArrayList<>();

    public ActionRegistry(MilestonesPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Register the listener. Only internal, use AbstractActionListener#register() instead.
     */
    @ApiStatus.Internal
    public void register(AbstractActionListener abstractListener) {
        this.loadedListeners.add(abstractListener);
        this.plugin.registerListener(abstractListener);

        abstractListener.registerConditions(this.plugin.getConditionRegistry());
        ConsoleOutput.getInstance().debug("Registered action listener " + abstractListener.getClass().getSimpleName() + " that's providing action types " + abstractListener.getRegisteredActions().toString());
    }

    public void registerListeners() {
        new BlockActionListener(plugin).register();
        new EntityActionListener(plugin).register();
        new PlayerActionListener(plugin).register();
        new WorldActionListener(plugin).register();
    }

    /**
     * Run on reload, in case anything has changed.
     */
    public void registerUsed() {
        List<String> usedActions = composeUsedActions();
        for (AbstractActionListener listener : this.loadedListeners) {
            if (!listener.isRegistered() && listener.getRegisteredActions().stream().anyMatch(usedActions::contains)) {
                listener.register();
                ConsoleOutput.getInstance().debug("Registered listener " + listener.getClass().getSimpleName() + " with actions " + listener.getRegisteredActions().toString());
            }
        }
    }

    /**
     * Unregister all listeners.
     */
    public void unregisterListeners() {
        for (AbstractActionListener listener : loadedListeners) {
            HandlerList.unregisterAll(listener);
        }
        this.loadedListeners.clear();
    }

    /**
     * Unregister action listeners that are not used by any milestones.
     */
    public void unregisterUnused() {
        List<String> usedActions = composeUsedActions();
        for (AbstractActionListener listener : this.loadedListeners) {
            if (listener.isRegistered() && listener.getRegisteredActions().stream().noneMatch(usedActions::contains)) {
                listener.unregister();
                ConsoleOutput.getInstance().debug("Unregistered unused action listener " + listener.getClass().getSimpleName() + " with actions " + listener.getRegisteredActions().toString());
            }
        }
    }

    private List<String> composeUsedActions() {
        return this.plugin.getMilestoneManager().getLoadedMilestones().values().stream()
                .map(Milestone::getActionName)
                .collect(Collectors.toList());
    }

    public void unregister(AbstractActionListener abstractActionListener) {
        this.loadedListeners.remove(abstractActionListener);
    }

    public boolean isRegistered(String name) {
        return this.loadedListeners.stream().anyMatch(a -> a.getRegisteredActions().contains(name) && a.isRegistered());
    }

    /**
     * Handle an incoming event.
     */
    public void handle(String name, Player player, ActionContext context) {

        ConsoleOutput.getInstance().debug("Caught action " + name + " caused by " + player.getName() + " with context " + context.toString());

        for (Milestone milestone : plugin.getMilestoneManager().getLoadedMilestones().values()) {

            if (!milestone.getActionName().equalsIgnoreCase(name) || !milestone.getCondition().check(player, context))
                continue;

            // Milestone with the correct action
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

                // Increment score
                User user = plugin.getUserManager().getOrCreateUser(player.getUniqueId());
                user.incrementScore(milestone.getName());

                // Fire rewards
                milestone.getRewards().give(user, milestone.getName());

                ConsoleOutput.getInstance().debug("Incremented score for " + player.getName() + " in milestone " + milestone.getName() + " to " + user.getScore(milestone.getName()));
            });
        }
    }
}
