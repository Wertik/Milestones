package me.wertik.milestones;

import me.wertik.milestones.handlers.StorageHandler;
import me.wertik.milestones.objects.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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

        List<String> broadcastMessage = stringList(section, "rewards.broadcast-message");
        List<String> informMessage = stringList(section, "rewards.inform-message");

        List<String> consoleCommands = stringList(section, "rewards.console-commands");
        List<String> playerCommands = stringList(section, "rewards.player-commands");

        return new Reward(consoleCommands, playerCommands, stringList(section, "rewards.items"), broadcastMessage, informMessage);
    }

    public List<StagedReward> loadStagedRewards(String name) {
        ConfigurationSection section = miles.getConfigurationSection(name);

        if (!miles.contains(name + ".staged-rewards"))
            return new ArrayList<>();

        List<StagedReward> stagedRewards = new ArrayList<>();
        List<String> stagedNames = new ArrayList<>(section.getConfigurationSection("staged-rewards").getKeys(false));

        for (String staged : stagedNames) {

            ConfigurationSection section1 = section.getConfigurationSection("staged-rewards." + staged);

            List<String> broadcastMessage = stringList(section1, "broadcast-message");
            List<String> informMessage = stringList(section1, "inform-message");

            List<String> consoleCommands = stringList(section1, "console-commands");
            List<String> playerCommands = stringList(section1, "player-commands");

            boolean repeat = section1.getBoolean("repeat");
            boolean denyNormal = section1.getBoolean("deny-normal");

            Reward reward = new Reward(consoleCommands, playerCommands, stringList(section1, "items"), broadcastMessage, informMessage);

            StagedReward stagedReward = new StagedReward(Integer.valueOf(staged), reward, repeat, denyNormal);

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
            return new ExactCondition(milestoneType, new BaseCondition(stringList(section, "conditions.in-inventory"), biomes, regionNames, worldNames, stringList(section, "conditions.tools"), stringList(section, "conditions.permissions")));
        else
            return new ExactCondition(milestoneType, new BaseCondition(stringList(section, "conditions.in-inventory"), biomes, regionNames, worldNames, stringList(section, "conditions.tools"), stringList(section, "conditions.permissions")), targets);
    }

    public HashMap<String, Milestone> setMilestones() {
        HashMap<String, Milestone> milestones = new HashMap<>();

        for (String name : getMilestoneNames()) {
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

    public List<String> getMilestoneNames() {
        return new ArrayList<>(miles.getKeys(false));
    }

    public List<String> getPersonalMileNames() {
        List<Milestone> milestones = getPersonalMilestones();
        List<String> milestoneNames = new ArrayList<>();

        for (Milestone milestone : milestones) {
            milestoneNames.add(milestone.getName());
        }

        return milestoneNames;
    }

    public List<String> getGlobalMileNames() {
        List<Milestone> milestones = getGlobalMilestones();
        List<String> milenames = new ArrayList<>();

        for (Milestone milestone : milestones) {
            milenames.add(milestone.getName());
        }

        return milenames;
    }

    public String getFinalString(String msg, Player p) {
        return Utils.color(Utils.parse(msg, p));
    }

    public String getFinalString(String msg, Player p, Milestone milestone) {
        return Utils.color(Utils.parse(msg, p, milestone));
    }
}
