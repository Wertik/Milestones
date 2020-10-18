package space.devport.wertik.milestones;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.devport.wertik.milestones.system.user.struct.User;

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
     * %milestones_score_<name>% -- score
     * */

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {

        if (player == null)
            return "no_player";

        String[] args = params.split("_");
        if (args.length == 0)
            return "not_enough_args";

        if (args[0].equalsIgnoreCase("score")) {
            if (args.length < 2)
                return "not_enough_args";

            String milestoneName = args[1];
            if (!plugin.getMilestoneManager().isValid(milestoneName))
                return "invalid_milestone";

            User user = plugin.getUserManager().getOrCreateUser(player.getUniqueId());
            return String.valueOf(user.getScore(milestoneName));
        }
        return "invalid_params";
    }
}
