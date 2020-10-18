package space.devport.wertik.milestones.system.action;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import space.devport.utils.ConsoleOutput;
import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.system.action.struct.AbstractActionListener;
import space.devport.wertik.milestones.system.action.struct.ActionContext;
import space.devport.wertik.milestones.system.action.struct.impl.BlockActionListener;
import space.devport.wertik.milestones.system.action.struct.impl.KillActionListener;
import space.devport.wertik.milestones.system.milestone.struct.Milestone;
import space.devport.wertik.milestones.system.user.struct.User;

import java.util.ArrayList;
import java.util.List;

public class ActionRegistry {

    //TODO Allow external sources to register an action before milestones are loaded.

    private final MilestonesPlugin plugin;

    private final List<AbstractActionListener> registeredListeners = new ArrayList<>();

    public ActionRegistry(MilestonesPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerListeners() {
        new BlockActionListener(plugin).register();
        new KillActionListener(plugin).register();
    }

    public void unregister() {
        for (AbstractActionListener listener : registeredListeners) {
            HandlerList.unregisterAll(listener);
        }
        this.registeredListeners.clear();
    }

    public boolean isRegistered(String name) {
        return this.registeredListeners.stream().anyMatch(a -> a.getRegisteredActions().contains(name));
    }

    public void register(AbstractActionListener abstractListener) {
        this.registeredListeners.add(abstractListener);
        this.plugin.registerListener(abstractListener);

        abstractListener.registerConditionLoaders(this.plugin.getConditionRegistry());
        ConsoleOutput.getInstance().debug("Registered action listener " + abstractListener.getClass().getSimpleName() + " that's providing action types " + abstractListener.getRegisteredActions().toString());
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
                //TODO Run rewards sync inside
                Bukkit.getScheduler().runTask(plugin, () -> milestone.getRewards().give(player));

                ConsoleOutput.getInstance().debug("Incremented score for " + player.getName() + " in milestone " + milestone.getName() + " to " + user.getScore(milestone.getName()));
            });
        }
    }
}
