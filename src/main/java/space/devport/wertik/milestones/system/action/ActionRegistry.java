package space.devport.wertik.milestones.system.action;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
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

    public void register(AbstractActionListener abstractListener) {
        this.registeredListeners.add(abstractListener);
        this.plugin.registerListener(abstractListener);

        abstractListener.registerConditionLoaders(this.plugin.getConditionRegistry());
    }

    public void handle(String name, Player player, ActionContext context) {
        for (Milestone milestone : plugin.getMilestoneManager().getLoadedMilestones().values()) {
            if (milestone.getActionName().equalsIgnoreCase(name) && milestone.getCondition().check(player, context)) {

                // Milestone with the correct action
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

                    // Increment score
                    User user = plugin.getUserManager().getOrCreateUser(player.getUniqueId());
                    user.incrementScore(milestone.getName());

                    // Fire rewards
                    milestone.getRewards().give(player);
                });
            }
        }
    }
}
