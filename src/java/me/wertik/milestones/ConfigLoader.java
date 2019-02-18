package me.wertik.milestones;

import me.wertik.milestones.handlers.DataHandler;
import me.wertik.milestones.handlers.StorageHandler;
import me.wertik.milestones.objects.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigLoader {

    public static FileConfiguration config;
    public static YamlConfiguration miles;
    public static File mileFile;
    private static HashMap<String, Milestone> milestones;

    private Main plugin;
    private ConfigLoader configLoader;
    private StorageHandler storageHandler;

    public ConfigLoader() {
        plugin = Main.getInstance();
        configLoader = plugin.getConfigLoader();
        storageHandler = plugin.getStorageHandler();
    }

    // THE FUN-CTIONSSSSS, did you get it? Huh.
    /*
     * Loading stuff from milestones.yml and config.yml, also some useless string and config methods.
     * idk how to name the classes actually, just ignore it.
     * */

    public void loadYamls() {
        // CF
        File configFile = new File(plugin.getDataFolder() + "/config.yml");

        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
            plugin.getServer().getConsoleSender().sendMessage("§aGenerated default §f" + configFile.getName());
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        // Milestone file
        mileFile = new File(plugin.getDataFolder() + "/milestones.yml");

        if (!mileFile.exists()) {
            plugin.saveResource("milestones.yml", false);
            plugin.getServer().getConsoleSender().sendMessage("§aGenerated default §f" + mileFile.getName());
        }

        miles = YamlConfiguration.loadConfiguration(mileFile);
    }

    public void loadMilestones() {
        milestones = setMilestones();
    }

    public Milestone getMilestone(String name) {
        if (milestones.containsKey(name))
            return milestones.get(name);
        else
            return null;
    }

    public Milestone loadMilestone(String milestoneName) {
        ExactCondition exactCondition = loadExactCondition(milestoneName);
        Reward reward = loadReward(milestoneName);

        ConfigurationSection section = miles.getConfigurationSection(milestoneName);

        String displayName = section.getString("display-name");

        boolean perPlayer = section.getBoolean("global-milestone");
        boolean onlyOnce = section.getBoolean("log-only-once");

        Milestone milestone = new Milestone(milestoneName, displayName, exactCondition, reward, loadStagedRewards(milestoneName), onlyOnce, perPlayer);

        return milestone;
    }

    public Reward loadReward(String name) {
        ConfigurationSection section = miles.getConfigurationSection(name);

        String broadcastMessage = section.getString("rewards.broadcast-message");
        String informMessage = section.getString("rewards.inform-message");

        List<String> commands = stringList(section, "rewards.commands");

        return new Reward(commands, stringList(section, "rewards.items"), Utils.checkString(broadcastMessage), Utils.checkString(informMessage));
    }

    public List<StagedReward> loadStagedRewards(String name) {
        ConfigurationSection section = miles.getConfigurationSection(name);

        if (!miles.contains(name + ".staged-rewards"))
            return null;

        List<StagedReward> stagedRewards = new ArrayList<>();
        List<String> stagedNames = new ArrayList<>(section.getConfigurationSection("staged-rewards").getKeys(false));

        for (String staged : stagedNames) {

            ConfigurationSection section1 = section.getConfigurationSection("staged-rewards."+staged);

            String broadcastMessage = section.getString("staged-rewards." + staged + ".broadcast-message");
            String informMessage = section.getString("staged-rewards." + staged + ".inform-message");

            List<String> commands = stringList(section1, "commands");

            Reward reward = new Reward(commands, stringList(section1, "items"), Utils.checkString(broadcastMessage), Utils.checkString(informMessage));

            StagedReward stagedReward = new StagedReward(Integer.valueOf(staged), reward);

            stagedRewards.add(stagedReward);
        }

        return stagedRewards;
    }

    /*
     * Condition breaks into:
     * type <- mobkill/blockbreak/blockplace
     * inInv
     * toolTypes - weapon/tool
     * biomeTypes
     * regionNames
     * worldNames
     * */

    private List<String> stringList(ConfigurationSection section, String path) {
        if (section.contains(path))
            return section.getStringList(path);
        else
            return new ArrayList<>();
    }

    public ExactCondition loadExactCondition(String milestoneName) {

        ConfigurationSection section = miles.getConfigurationSection(milestoneName);

        List<String> biomes = stringList(section, "conditions.biomes");
        List<String> regionNames = stringList(section, "conditions.regions");
        List<String> worldNames = stringList(section, "conditions.worlds");
        List<String> targets = stringList(section, "conditions.targets");

        MilestoneType milestoneType = MilestoneType.fromString(section.getString("type"));

        if (milestoneType.equals(MilestoneType.PLAYER_JOIN) || milestoneType.equals(MilestoneType.PLAYER_QUIT) || milestoneType.equals(MilestoneType.BLOCK_PLACE))
            return new ExactCondition(milestoneType, new BaseCondition(stringList(section, "conditions.in-inventory"), biomes, regionNames, worldNames, stringList(section, "conditions.tools")));
        else
            return new ExactCondition(milestoneType, new BaseCondition(stringList(section, "conditions.in-inventory"), biomes, regionNames, worldNames, stringList(section, "conditions.tools")), targets);
    }

    public HashMap<String, Milestone> setMilestones() {
        HashMap<String, Milestone> milestones = new HashMap<>();

        for (String name : getMileNames()) {
            milestones.put(name, loadMilestone(name));
        }

        return milestones;
    }

    public List<Milestone> getMilestones() {
        return new ArrayList<>(milestones.values());
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

        DataHandler dataHandler = plugin.getDataHandler();

        msg = parseString(msg, p);
        if (msg.contains("%milestone_name%"))
            msg = msg.replace("%milestone_name%", milestone.getName());
        if (msg.contains("%milestone_displayName%"))
            msg = msg.replace("%milestone_displayName%", milestone.getDisplayName());
        if (msg.contains("%milestone_score%"))
            msg = msg.replace("%milestone_score%", String.valueOf(dataHandler.getScore(p.getName(), milestone.getName())));
        return msg;
    }

    public List<String> parseStringList(List<String> msg, Player p) {
        return null;
    }

    public List<String> parseStringList(List<String> msg, Player p, Milestone milestone) {
        return null;
    }

    public String getFinalString(String msg, Player p) {
        return Utils.color(parseString(msg, p));
    }

    public String getFinalString(String msg, Player p, Milestone milestone) {
        return Utils.color(parseString(msg, p, milestone));
    }
}
