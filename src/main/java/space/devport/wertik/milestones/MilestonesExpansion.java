package space.devport.wertik.milestones;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MilestonesExpansion extends PlaceholderExpansion {

    private final MilestonesPlugin plugin;

    public MilestonesExpansion(MilestonesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "milestones";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    /*
     * %milestones_<name>% -- score
     * */

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {

        if (player == null)
            return "no_player";
        // TODO
        return null;
    }
}
