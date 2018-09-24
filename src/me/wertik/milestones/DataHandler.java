package me.wertik.milestones;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataHandler {

    ConfigLoader cload = new ConfigLoader();
    Main plugin = Main.getInstance();
    private static HashMap<String, YamlConfiguration> files;
    private static File globalMileFile;
    private static YamlConfiguration globalMiles;

    public DataHandler() {
    }

    public void loadFiles() {

        globalMileFile = new File(plugin.getDataFolder() + "/globalmilestones.yml");
        globalMiles = YamlConfiguration.loadConfiguration(globalMileFile);

        files = new HashMap<>();
        List<String> names = cload.getMileNames();

        for (String name : names) {
            if (!cload.getMilestone(name).isGlobal())
                createDataFile(name);
                // global milestones
            else {
                if (!globalMiles.contains(name)) {
                    globalMiles.set(name, 0);
                    saveGlobalMileFile();
                }
            }
        }
    }

    // Class that manipulates the data saved.
    /*
     * name == milestone name
     * p == player.. -.-"
     *
     * Files are created with the names of milestones. It doesn't matter so i just did it like this.
     *
     * CLEAR == set the score to 0
     * REMOVE == remove the caption
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
        globalMiles.set(name, 0);
    }

    // Clear all globalMilestones
    public void clearGlobalScores() {
        List<String> globalMilestones = new ArrayList<>(globalMiles.getKeys(false));

        for (String globalMilestone : globalMilestones) {
            globalMiles.set(globalMilestone, 0);
        }
    }

    // ADD

    // add a point. :)
    public void addScore(String playerName, String name) {
        files.get(name).set(playerName, getScore(playerName, name) + 1);
    }

    // add a global point
    public void addGlobalScore(String name) {
        globalMiles.set(name, getGlobalScore(name) + 1);
    }

    public int getGlobalScore(String name) {
        return globalMiles.getInt(name);
    }

    // Add player to the Scoreboard
    public void addPlayer(String playerName, String name) {
        files.get(name).set(playerName, 0);
    }

    public boolean isLogged(String playerName, String name) {

        if (files.get(name).contains(playerName))
            return true;
        else
            return false;
    }

    public boolean isLogged(String playerName) {
        List<String> milestones = cload.getMileNames();

        for (String milestone : milestones) {
            if (isLogged(playerName, milestone))
                return true;
            else
                continue;
        }

        return false;
    }

    public List<String> getLoggedPlayers() {
        List<String> playerNames = new ArrayList<>();

        for (YamlConfiguration yaml : files.values()) {
            playerNames.addAll(yaml.getKeys(false));
        }
        return playerNames;
    }

    // GETTER
    public int getScore(String playerName, String name) {
        if (!files.get(name).contains(playerName))
            addPlayer(playerName, name);
        return files.get(name).getInt(playerName);
    }

    // FILE MANIPULATION
    public YamlConfiguration createDataFile(String name) {
        File file = new File(plugin.getDataFolder() + "/data/" + name + ".yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        files.put(name, yaml);

        if (!file.exists()) {
            plugin.getServer().getConsoleSender().sendMessage("§aCreated file for §f" + file.getName().replace(".yml", ""));

            saveDataFile(name);
        }

        return yaml;
    }

    public void saveDataFile(String name) {
        File file = new File(plugin.getDataFolder() + "/data/" + name + ".yml");
        try {
            files.get(name).save(file);
        } catch (IOException e) {
            plugin.getServer().getConsoleSender().sendMessage("§cCould not save data file §f" + file.getName());
        }
    }

    public void saveDataFiles() {
        for (String name : files.keySet()) {
            saveDataFile(name);
        }
    }

    public void saveGlobalMileFile() {
        try {
            globalMiles.save(globalMileFile);
        } catch (IOException e) {
            plugin.getServer().getConsoleSender().sendMessage("§cCould not save data file §fglobalmilestones.yml");
        }
    }

    public void removeDataFile(String name) {
        File file = new File(plugin.getDataFolder() + "/data/" + name + ".yml");
        file.delete();
    }
}
