package me.wertik.milestones;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook extends EZPlaceholderHook {
    /*
     * Plans:
     * %milestones_<name>%
     * Well, that seems easy.
     * */

    private Main plugin;
    private DataHandler dataHandler = new DataHandler();
    private ConfigLoader cload = new ConfigLoader();

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if (p == null)
            return "";

        // %milestones_<name>%

        if (cload.getMileNames().contains(params))
            return String.valueOf(dataHandler.getScore(p, params));

        return null;
    }

    public PlaceholderAPIHook(Main plugin) {
        super(plugin, "milestones");
        this.plugin = plugin;
    }
}
