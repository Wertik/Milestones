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
    public static File milefile;

    public ConfigLoader() {
    }

    // THE FUN-CTIONSSSSS, did you get it? Huh.
    /*
    * Loading stuff from milestones.yml and config.yml, also some useless string and config methods.
    * idk how to name the classes actualy, just ignore it.
    * */

    public void loadYamls() {
        config = plugin.getConfig();
        milefile = new File(plugin.getDataFolder() + "/milestones.yml");
        miles = YamlConfiguration.loadConfiguration(milefile);
    }

    public Milestone getMilestone(String name) {
        Condition condition = getCondition(name);

        boolean perPlayer = miles.getBoolean(name + ".global-milestone");
        boolean onlyOnce = miles.getBoolean(name + ".log-only-once");

        return new Milestone(name, condition, onlyOnce, perPlayer);
    }

    public List<Milestone> getMilestones() {
        List<Milestone> milestones = new ArrayList<>();

        for (String name : getMileList()) {
            milestones.add(getMilestone(name));
        }

        return milestones;
    }

    public List<String> getMileList() {
        return new ArrayList<>(miles.getKeys(false));
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
