package me.wertik.milestones;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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
    public void clearScore(Player p, String name) {
    }

    // only for a player
    public void clearScore(Player p) {
    }

    // only by milestone name
    public void clearScore(String name) {
    }

    public void clearGlobalScore(String name) {
        globalMiles.set(name, 0);
        saveGlobalMileFile();
    }

    // Clear all globalMilestones
    public void clearGlobalScores() {
        List<String> globalMilestones = new ArrayList<>(globalMiles.getKeys(false));

        for (String globalMilestone : globalMilestones) {
            globalMiles.set(globalMilestone, 0);
        }

        saveGlobalMileFile();
    }

    // ADD

    // add a point. :)
    public void addScore(Player p, String name) {
        files.get(name).set(p.getName(), getScore(p, name) + 1);
        saveDataFile(name);
    }

    // add a global point
    public void addGlobalScore(String name) {
        globalMiles.set(name, getGlobalScore(name) + 1);
        saveGlobalMileFile();
    }

    public int getGlobalScore(String name) {
        return globalMiles.getInt(name);
    }

    // Add player to the Scoreboard
    public void addPlayer(Player p, String name) {
        files.get(name).set(p.getName(), 0);
        saveDataFile(name);
    }

    public boolean isLogged(Player p, String name) {
        if (files.get(name).contains(p.getName()))
            return true;
        else
            return false;
    }

    public boolean isLogged(Player p) {
        List<String> milestones = cload.getMileNames();

        for (String milestone : milestones) {
            if (isLogged(p, milestone))
                return true;
            else
                continue;
        }

        return false;
    }

    // GETTER
    public int getScore(Player p, String name) {
        if (!files.get(name).contains(p.getName()))
            addPlayer(p, name);
        return files.get(name).getInt(p.getName());
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
