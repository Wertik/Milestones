package me.wertik.milestones;

import me.wertik.milestones.handlers.DataHandler;
import me.wertik.milestones.handlers.StorageHandler;
import me.wertik.milestones.objects.Condition;
import me.wertik.milestones.objects.Milestone;
import me.wertik.milestones.objects.Reward;
import me.wertik.milestones.objects.StagedReward;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigLoader {

    public static FileConfiguration config;
    public static YamlConfiguration miles;
    public static File mileFile;
    private static HashMap<String, Milestone> milestones;
    private Main plugin = Main.getInstance();
    private Utils utils = plugin.getUtils();
    private StorageHandler storageHandler = plugin.getStorageHandler();
    private DataHandler dataHandler = plugin.getDataHandler();

    public ConfigLoader() {
    }

    // THE FUN-CTIONSSSSS, did you get it? Huh.
    /*
     * Loading stuff from milestones.yml and config.yml, also some useless string and config methods.
     * idk how to name the classes actualy, just ignore it.
     * */

    public void loadYamls() {
        // CF
        File configFile = new File(plugin.getDataFolder() + "/config.yml");

        if (!configFile.exists()) {
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveConfig();
            plugin.getServer().getConsoleSender().sendMessage("§aGenerated default §f" + configFile.getName());
        }

        config = plugin.getConfig();

        // Milestone file
        mileFile = new File(plugin.getDataFolder() + "/milestones.yml");

        if (!mileFile.exists()) {
            plugin.saveResource("milestones.yml", false);
            miles = YamlConfiguration.loadConfiguration(mileFile);
            miles.options().copyDefaults(true);
            try {
                miles.save(mileFile);
            } catch (IOException e) {
                plugin.getServer().getConsoleSender().sendMessage("§cCould not save the file, that's bad tho.");
            }
            plugin.getServer().getConsoleSender().sendMessage("§aGenerated default §f" + mileFile.getName());
        }
    }

    // Toggles, not done yet.
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

        if (!miles.contains(name + ".staged-rewards"))
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
                if (storageHandler.getItemNames().contains(storageHandler.parseForItemName(row)))
                    toolTypes.add(storageHandler.parseForItem(row));
                continue;
            }

            toolTypes.add(new ItemStack(Material.valueOf(row), -1));
        }

        List<String> biomes = section.getStringList("conditions.biome-types");
        List<String> regionNames = section.getStringList("conditions.region-names");

        List<String> targets = section.getStringList("conditions.targets");

        Condition condition = new Condition(type, inInventory, toolTypes, targets, biomes, regionNames);

        switch (type) {
            case "playerjoin":
                //                                            (inHand)
                condition = new Condition(type, inInventory, toolTypes, null, biomes, regionNames);
                break;
            case "playerquit":
                //                                            (inHand)
                condition = new Condition(type, inInventory, toolTypes, null, biomes, regionNames);
                break;
        }

        if (condition == null) {
            plugin.getServer().getConsoleSender().sendMessage("§cLol, you made a mistake in the config. Typos are common, check it.");
        }

        return condition;
    }

    public HashMap<String, Milestone> setMilestones() {
        HashMap<String, Milestone> milestones = new HashMap<>();

        for (String name : getMileNames()) {
            milestones.put(name, createMilestone(name));
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
        return format(parseString(msg, p));
    }

    public String getFinalString(String msg, Player p, Milestone milestone) {
        return format(parseString(msg, p, milestone));
    }
}
