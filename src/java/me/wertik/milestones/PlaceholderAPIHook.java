package me.wertik.milestones;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import me.wertik.milestones.handlers.DataHandler;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook extends EZPlaceholderHook {
    /*
     * Plans:
     * %milestones_<name>%
     * Well, that seems easy.
     * */

    private Main plugin;

    @Override
    public String onPlaceholderRequest(Player p, String params) {

        DataHandler dataHandler = plugin.getDataHandler();
        ConfigLoader configLoader = plugin.getConfigLoader();

        if (p == null)
            return "";

        // %milestones_<name>%

        if (configLoader.getMileNames().contains(params))
            return String.valueOf(dataHandler.getScore(p.getName(), params));

        p.sendMessage(configLoader.getMileNames().toString());

        return null;
    }

    public PlaceholderAPIHook(Main plugin) {
        super(plugin, "milestones");
        this.plugin = plugin;
    }
}
