package space.devport.wertik.milestones.system.action.struct;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.system.milestone.struct.condition.ConditionRegistry;

import java.util.List;

public abstract class AbstractActionListener implements Listener {

    protected final MilestonesPlugin plugin;

    @Getter
    private boolean registered = false;

    public AbstractActionListener(MilestonesPlugin plugin) {
        this.plugin = plugin;
    }

    public void handle(String name, Player player, ActionContext context) {
        plugin.getActionRegistry().handle(name, player, context);
    }

    /**
     * Register the listener.
     */
    public void register() {
        plugin.getActionRegistry().register(this);
        this.registered = true;
    }

    /**
     * Unregister the listener.
     */
    public void unregister() {
        plugin.getActionRegistry().unregister(this);
    }

    /**
     * Return whether or not the listener can be registered.
     */
    public boolean canRegister() {
        return true;
    }

    public abstract List<String> getRegisteredActions();

    public abstract void registerConditions(ConditionRegistry registry);
}