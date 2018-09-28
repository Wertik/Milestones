package me.wertik.milestones;

import me.wertik.milestones.handlers.StorageHandler;
import me.wertik.milestones.objects.Condition;
import me.wertik.milestones.objects.Milestone;
import me.wertik.milestones.objects.Reward;
import me.wertik.milestones.objects.StagedReward;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

    private Main plugin = Main.getInstance();
    private Utils utils = new Utils();
    private StorageHandler storageHandler = new StorageHandler();
    public static FileConfiguration config;
    public static YamlConfiguration miles;
    public static File mileFile;
    public static File storageFile;
    public static YamlConfiguration storage;

    private static HashMap<String, Milestone> milestones;

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
        Bukkit.broadcastMessage("Milestones: " + milestones.keySet());
        return milestones.get(name);
    }

    public Milestone createMilestone(String name) {
        Condition condition = getCondition(name);
        Reward reward = getReward(name);

        ConfigurationSection section = miles.getConfigurationSection(name);

        String displayName = section.getString("display-name");

        boolean perPlayer = section.getBoolean("global-milestone");
        boolean onlyOnce = section.getBoolean("log-only-once");

        Milestone milestone = new Milestone(name, displayName, condition, reward, getStagedRewards(name), onlyOnce, perPlayer);

        return milestone;
    }

    // commmands, items, broadcast, inform
    public Reward getReward(String name) {
        ConfigurationSection section = miles.getConfigurationSection(name);

        String broadcastMessage = section.getString("rewards.broadcast-message");
        String informMessage = section.getString("rewards.inform-message");

        List<String> commands = section.getStringList("rewards.commands");
        List<String> itemNames = section.getStringList("rewards.items");

        Reward reward = new Reward(commands, itemNames, utils.checkString(broadcastMessage), utils.checkString(informMessage));

        return reward;
    }

    // Staged rewards
    public List<StagedReward> getStagedRewards(String name) {
        ConfigurationSection section = miles.getConfigurationSection(name);

        if (!miles.contains(name+".staged-rewards"))
            return null;

        List<StagedReward> stagedRewards = new ArrayList<>();
        List<String> stagedNames = new ArrayList<>(section.getConfigurationSection("staged-rewards").getKeys(false));

        for (String staged : stagedNames) {

            String broadcastMessage = section.getString("staged-rewards." + staged + ".broadcast-message");
            String informMessage = section.getString("staged-rewards." + staged + ".inform-message");

            List<String> commands = section.getStringList("staged-rewards." + staged + ".commands");
            List<String> itemNames = section.getStringList("staged-rewards." + staged + ".items");

            Reward reward = new Reward(commands, itemNames, utils.checkString(broadcastMessage), utils.checkString(informMessage));

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
     * */

    public Condition getCondition(String name) {
        // get type, sort by it
        ConfigurationSection section = miles.getConfigurationSection(name);

        String type = section.getString("type");

        // inInventory
        List<String> inInventoryString = section.getStringList("conditions.in-inventory");
        List<ItemStack> inInventory = new ArrayList<>();

        for (String row : inInventoryString) {
            if (storageHandler.lookForItem(row)) {
                inInventory.add(storageHandler.parseForItem(row));
                continue;
            }

            inInventory.add(new ItemStack(Material.valueOf(row), -1));
        }

        // toolTypes
        List<String> toolTypesString = section.getStringList("conditions.tool-types");
        List<ItemStack> toolTypes = new ArrayList<>();

        for (String row : toolTypesString) {
            if (storageHandler.lookForItem(row)) {
                toolTypes.add(storageHandler.parseForItem(row));
                continue;
            }

            toolTypes.add(new ItemStack(Material.valueOf(row), -1));
        }

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

    public HashMap<String, Milestone> setMilestones() {
        HashMap<String, Milestone> milestones = new HashMap<>();

        Bukkit.broadcastMessage("Miles: " + getMileNames());

        for (String name : getMileNames()) {
            milestones.put(name, createMilestone(name));
        }

        Bukkit.broadcastMessage("Milestones: " + milestones.keySet());

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
