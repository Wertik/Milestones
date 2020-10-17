package space.devport.wertik.milestones.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import space.devport.wertik.milestones.MilestonesPlugin;

public class ManagementListener implements Listener {

    private final MilestonesPlugin plugin;

    public ManagementListener(MilestonesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getUserManager().isLoaded(player.getUniqueId())) {
            plugin.getUserManager().loadUser(player.getUniqueId());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        plugin.getUserManager().unloadUser(player.getUniqueId());
    }
}