package me.wertik.milestones;

import me.wertik.milestones.objects.Condition;
import me.wertik.milestones.objects.Milestone;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigLoader {

    private Main plugin = Main.getInstance();
    public static FileConfiguration config;
    public static YamlConfiguration miles;
    public static File mileFile;
    public static File storageFile;
    public static YamlConfiguration storage;

    private static List<Milestone> milestones;

    public ConfigLoader() {
    }

    // THE FUN-CTIONSSSSS, did you get it? Huh.
    /*
     * Loading stuff from milestones.yml and config.yml, also some useless string and config methods.
     * idk how to name the classes actualy, just ignore it.
     * */

    public void loadYamls() {
        config = plugin.getConfig();
        mileFile = new File(plugin.getDataFolder() + "/milestones.yml");
        miles = YamlConfiguration.loadConfiguration(mileFile);
        storageFile = new File(plugin.getDataFolder() + "/datastorage.yml");
        storage = YamlConfiguration.loadConfiguration(storageFile);
    }

    // Toggles
    public void togglePlayer(String playerName) {
    }

    public void toggleMilestone(String name) {
    }

    public boolean getPlayerToggle(String playerName) {
        return false;
    }

    public boolean getMilestoneToggle(String name) {
        return false;
    }

    public void loadMilestones() {
        milestones = setMilestones();
    }

    public Milestone getMilestone(String name) {
        Condition condition = getCondition(name);

        String displayName = miles.getString(name + ".display-name");

        boolean perPlayer = miles.getBoolean(name + ".global-milestone");
        boolean onlyOnce = miles.getBoolean(name + ".log-only-once");
        boolean broadcast = miles.getBoolean(name + ".broadcast");
        boolean inform = miles.getBoolean(name + ".inform-player");

        String broadcastMessage = miles.getString(name + ".broadcast-message");
        String informMessage = miles.getString(name + ".inform-message");

        List<String> commands = miles.getStringList(name + ".commands");

        return new Milestone(name, displayName, condition, onlyOnce, perPlayer, broadcast, broadcastMessage, inform, informMessage, commands);
    }

    public List<Milestone> setMilestones() {
        List<Milestone> milestones = new ArrayList<>();

        for (String name : getMileNames()) {
            milestones.add(getMilestone(name));
        }

        return milestones;
    }

    public List<Milestone> getMilestones() {
        return milestones;
    }

    public List<Milestone> getGlobalMilestones() {
        List<Milestone> output = new ArrayList<>();

        for (Milestone milestone : getMilestones()) {
            if (milestone.isGlobal())
                output.add(milestone);
        }

        return output;
    }

    public List<Milestone> getPersonalMilestones() {
        List<Milestone> output = new ArrayList<>();

        for (Milestone milestone : getMilestones()) {
            if (!milestone.isGlobal())
                output.add(milestone);
        }

        return output;
    }

    public List<String> getMileNames() {
        return new ArrayList<>(miles.getKeys(false));
    }

    public List<String> getPersonalMileNames() {
        List<Milestone> milestones = getPersonalMilestones();
        List<String> milenames = new ArrayList<>();

        for (Milestone milestone : milestones) {
            milenames.add(milestone.getName());
        }

        return milenames;
    }

    public List<String> getGlobalMileNames() {
        List<Milestone> milestones = getGlobalMilestones();
        List<String> milenames = new ArrayList<>();

        for (Milestone milestone : milestones) {
            milenames.add(milestone.getName());
        }

        return milenames;
    }

    /*
     * Condition breaks into:
     * type <- mobkill/blockbreak/blockplace
     * inInv
     * toolTypes - weapon/tool
     * biomeTypes
     * */

    public Condition getCondition(String name) {
        // get type, sort by it
        ConfigurationSection section = miles.getConfigurationSection(name);

        String type = section.getString("type");

        List<String> inInventory = section.getStringList("conditions.in-inventory");
        List<String> toolTypes = section.getStringList("conditions.tool-types");
        List<String> biomes = section.getStringList("conditions.biome-types");
        List<String> regionNames = section.getStringList("conditions.region-names");

        Condition condition = null;

        switch (type) {
            case "entitykill":
                List<String> mobTypes = section.getStringList("conditions.mob-types");
                condition = new Condition(type, inInventory, toolTypes, mobTypes, biomes, regionNames);
                break;
            case "blockbreak":
                List<String> blockTypes = section.getStringList("conditions.block-types");
                condition = new Condition(type, inInventory, toolTypes, blockTypes, biomes, regionNames);
                break;
            case "blockplace":
                blockTypes = section.getStringList("conditions.block-types");
                condition = new Condition(type, inInventory, toolTypes, blockTypes, biomes, regionNames);
                break;
        }

        if (condition == null) {
            plugin.getServer().getConsoleSender().sendMessage("§cLol, you made a mistake in the config. Typos are common, check it.");
        }

        return condition;
    }

    public String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    // Placeholders..
    /*
     * %player%
     * idk, lol..
     *
     *
     * */

    public String parseString(String msg, Player p) {
        if (msg.contains("%player%"))
            msg = msg.replace("%player%", p.getName());
        return msg;
    }

    /*
     * %milestone_name%
     * %milestone_displayName%
     *
     * */

    public String parseString(String msg, Player p, Milestone milestone) {
        msg = parseString(msg, p);
        if (msg.contains("%milestone_name%"))
            msg = msg.replace("%milestone_name%", milestone.getName());
        if (msg.contains("%milestone_displayName%"))
            msg = msg.replace("%milestone_displayName%", milestone.getDisplayName());
        return msg;
    }

    public List<String> parseStringList(List<String> msg, Player p) {
        return null;
    }

    public List<String> parseStringList(List<String> msg, Player p, Milestone milestone) {
        return null;
    }

    public String getFinalString(String msg, Player p) {
        return format(parseString(msg, p));
    }

    public String getFinalString(String msg, Player p, Milestone milestone) {
        return format(parseString(msg, p, milestone));
    }
}
