package me.wertik.milestones;

import me.wertik.milestones.objects.Condition;
import me.wertik.milestones.objects.Milestone;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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
    public void togglePlayer(String playerName) {}

    public void toggleMilestone(String name) {}

    public boolean getPlayerToggle(String playerName) {return false;}

    public boolean getMilestoneToggle(String name) {return false;}

    public void loadMilestones() {
        milestones = setMilestones();
    }

    public Milestone getMilestone(String name) {
        Condition condition = getCondition(name);

        boolean perPlayer = miles.getBoolean(name + ".global-milestone");
        boolean onlyOnce = miles.getBoolean(name + ".log-only-once");

        return new Milestone(name, condition, onlyOnce, perPlayer);
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
     * */

    public Condition getCondition(String name) {
        // get type, sort by it
        ConfigurationSection section = miles.getConfigurationSection(name);

        String type = section.getString("type");

        List<String> inInventory = section.getStringList("conditions.in-inventory");
        List<String> toolTypes = section.getStringList("conditions.tool-types");

        Condition condition = null;

        switch (type) {
            case "entitykill":
                List<String> mobTypes = section.getStringList("conditions.mob-types");
                condition = new Condition(type, inInventory, toolTypes, mobTypes);
                break;
            case "blockbreak":
                List<String> blockTypes = section.getStringList("conditions.block-types");
                condition = new Condition(type, inInventory, toolTypes, blockTypes);
                break;
            case "blockplace":
                blockTypes = section.getStringList("conditions.block-types");
                condition = new Condition(type, inInventory, toolTypes, blockTypes);
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
}
