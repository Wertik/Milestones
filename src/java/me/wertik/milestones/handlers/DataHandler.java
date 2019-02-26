package me.wertik.milestones.handlers;

import me.wertik.milestones.ConfigLoader;
import me.wertik.milestones.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataHandler {

    private static HashMap<String, YamlConfiguration> files;
    private static HashMap<String, YamlConfiguration> globalFiles;

    private Main plugin;
    private ConfigLoader configLoader;

    public DataHandler() {
        plugin = Main.getInstance();
        configLoader = plugin.getConfigLoader();
    }

    public void loadFiles() {

        // Data folder
        File folder = new File(plugin.getDataFolder() + "/data");
        if (!folder.exists()) {
            folder.mkdir();
            plugin.getServer().getConsoleSender().sendMessage("§aCreating folder §f" + folder.getName());
        }

        files = new HashMap<>();
        configLoader.getPersonalMileNames().forEach(milestone -> createDataFile(milestone));

        globalFiles = new HashMap<>();
        configLoader.getGlobalMileNames().forEach(milestone -> createGlobalDataFile(milestone));
    }

    // Class that manipulates the data saved.
    /*
     * name == milestone name
     *
     * Files are created with the names of milestones. It doesn't matter so i just did it like this.
     *
     * CLEAR == set the score to 0
     * ADD == add 1 point to the score
     * SUB == inc. soon mby.
     * */

    // CLEAR

    // clear for exact player and exact milestone name
    public void clearScore(String playerName, String name) {
        files.get(name).set(playerName, 0);
    }

    // only for a player
    public void clearPlayerScore(String playerName) {
        for (YamlConfiguration yaml : files.values()) {
            yaml.set(playerName, 0);
        }
    }

    // only by milestone name
    public void clearMilestoneScores(String name) {
        for (String path : files.get(name).getKeys(false)) {
            files.get(name).set(path, 0);
        }
    }

    // ALL OF THEM. NO MERCY.
    public void clearScores() {
        for (String name : files.keySet()) {
            clearMilestoneScores(name);
        }
    }

    public void clearGlobalScore(String name) {
        globalFiles.get(name).set("Players", new ArrayList<>());
        globalFiles.get(name).set("Score", 0);
        saveDataFile(name);
    }

    // Clear all globalMilestones
    public void clearGlobalScores() {
        ConfigLoader configLoader = plugin.getConfigLoader();
        for (String globalMilestone : configLoader.getGlobalMileNames()) {
            clearMilestoneScores(globalMilestone);
        }
    }

    // ADD

    // add a point. :)
    public void addScore(String playerName, String name) {
        files.get(name).set(playerName, getScore(playerName, name) + 1);
        saveDataFile(name);
    }

    // add a global point
    public void addGlobalScore(String name, String playerName) {
        globalFiles.get(name).set("Score", getGlobalScore(name) + 1);
        List<String> playerNames = new ArrayList<>(getGlobalLoggedPlayers(name));
        if (!playerNames.contains(playerName))
            playerNames.add(playerName);
        globalFiles.get(name).set("Players", playerNames);
        saveDataFile(name);
    }

    public List<String> getGlobalLoggedPlayers(String name) {
        if (globalFiles.containsKey(name))
            return globalFiles.get(name).getStringList("Players");
        else
            return new ArrayList<>();
    }

    public int getGlobalScore(String name) {
        if (globalFiles.containsKey(name))
            return globalFiles.get(name).getInt("Score");
        else
            return 0;
    }

    public boolean isLogged(String playerName, String name) {
        return files.get(name).contains(playerName);
    }

    public boolean isLogged(String playerName) {
        ConfigLoader configLoader = plugin.getConfigLoader();
        List<String> milestones = configLoader.getMilestoneNames();

        for (String milestone : milestones) {
            if (isLogged(playerName, milestone))
                return true;
            else
                continue;
        }
        return false;
    }

    // GETTER
    public int getScore(String playerName, String name) {
        if (!files.get(name).contains(playerName))
            return 0;
        return files.get(name).getInt(playerName);
    }

    // FILE MANIPULATION
    public YamlConfiguration createDataFile(String name) {
        File file = new File(plugin.getDataFolder() + "/data/" + name + ".yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        files.put(name, yaml);

        if (!file.exists())
            plugin.getServer().getConsoleSender().sendMessage("§aCreated file for §f" + file.getName().replace(".yml", ""));

        saveDataFile(name);

        files.put(name, yaml);

        return yaml;
    }

    public YamlConfiguration createGlobalDataFile(String name) {
        File file = new File(plugin.getDataFolder() + "/data/" + name + ".yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        globalFiles.put(name, yaml);

        if (!file.exists()) {
            plugin.getServer().getConsoleSender().sendMessage("§aCreated file for §f" + file.getName().replace(".yml", ""));

            yaml.set("Score", 0);
            yaml.set("Players", new ArrayList<>());
        }

        saveDataFile(name);

        globalFiles.put(name, yaml);

        return yaml;
    }

    public void saveDataFile(String name) {
        File file = new File(plugin.getDataFolder() + "/data/" + name + ".yml");
        try {
            if (files.containsKey(name))
                files.get(name).save(file);
            else if (globalFiles.containsKey(name))
                globalFiles.get(name).save(file);
            else
                plugin.getServer().getConsoleSender().sendMessage("§cCould not save data file §f" + file.getName());
        } catch (IOException e) {
            plugin.getServer().getConsoleSender().sendMessage("§cCould not save data file §f" + file.getName());
        }
    }

    public void saveDataFiles() {
        for (String name : files.keySet()) {
            saveDataFile(name);
        }
        for (String name : globalFiles.keySet()) {
            saveDataFile(name);
        }
        plugin.getServer().getConsoleSender().sendMessage("§aData files saved.");
    }
}
