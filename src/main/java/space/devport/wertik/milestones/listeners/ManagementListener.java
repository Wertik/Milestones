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

    // Load user on join
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getUserManager().loadUser(player.getUniqueId());
    }

    // Unload on quit
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getUserManager().unloadUser(player.getUniqueId());
    }
}