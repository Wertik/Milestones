package me.wertik.milestones;

import me.wertik.milestones.commands.Commands;
import me.wertik.milestones.listeners.BlockBreakListener;
import me.wertik.milestones.listeners.BlockPlaceListener;
import me.wertik.milestones.listeners.EntityDeathListener;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {

    private static Main instance;

    private static void setInstance(Main instance) {
        Main.instance = instance;
    }

    public static Main getInstance() {
        return Main.instance;
    }

    // Let's do it.
    @Override
    public void onEnable() {
        ConsoleCommandSender console = getServer().getConsoleSender();
        console.sendMessage("§2Enabling milestones, sign your GDPR, i'm gonna steal your data.");

        setInstance(this);

        // Files
        // CF
        File configfile = new File(getDataFolder() + "/config.yml");
        FileConfiguration config = getConfig();

        if (!configfile.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
            console.sendMessage("§aGenerated default §f" + configfile.getName());
        }

        // Milestone file
        File milefile = new File(getDataFolder() + "/milestones.yml");

        if (!milefile.exists()) {
            saveResource("milestones.yml", false);
            YamlConfiguration mileyaml = YamlConfiguration.loadConfiguration(milefile);
            mileyaml.options().copyDefaults(true);
            try {
                mileyaml.save(milefile);
            } catch (IOException e) {
                console.sendMessage("§cCould not save the file, that's bad tho.");
            }
            console.sendMessage("§aGenerated default §f" + milefile.getName());
        }

        // Other data
        File storageFile = new File(getDataFolder() + "/datastorage.yml");

        if (!storageFile.exists()) {
            saveResource("datastorage.yml", false);
            YamlConfiguration storageYaml = YamlConfiguration.loadConfiguration(milefile);
            storageYaml.options().copyDefaults(true);
            try {
                storageYaml.save(storageFile);
            } catch (IOException e) {
                console.sendMessage("§cCould not save the file, that's bad tho.");
            }
            console.sendMessage("§aGenerated default §f" + storageFile.getName());
        }

        // Global Milestones
        File globalMileFile = new File(getDataFolder() + "/globalmilestones.yml");

        if (!globalMileFile.exists()) {
            saveResource("globalmilestones.yml", false);
            YamlConfiguration globalMiles = YamlConfiguration.loadConfiguration(globalMileFile);
            globalMiles.options().copyDefaults(true);
            try {
                globalMiles.save(globalMileFile);
            } catch (IOException e) {
                console.sendMessage("§cCould not save the file, that's bad tho.");
            }
            console.sendMessage("§aGenerated default §f" + globalMileFile.getName());
        }

        // Data folder
        File folder = new File(getDataFolder() + "/data");
        if (!folder.exists()) {
            folder.mkdir();
            console.sendMessage("§aCreating folder §f" + folder.getName());
        }

        // Commands, listeners
        getCommand("milestones").setExecutor(new Commands());

        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);

        // Placeholder API
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI").isEnabled()) {
            new PlaceholderAPIHook(this).hook();
            console.sendMessage("§aPlaceholder API hooked successfuly.");
        }

        ConfigLoader cload = new ConfigLoader();
        cload.loadYamls();
        cload.loadMilestones();

        DataHandler dataHandler = new DataHandler();
        dataHandler.loadFiles();
    }

    @Override
    public void onDisable() {
        ConsoleCommandSender console = getServer().getConsoleSender();
        DataHandler dataHandler = new DataHandler();
        dataHandler.saveGlobalMileFile();
        dataHandler.saveDataFiles();
        console.sendMessage("§2It's okay now, i got all data i could ever dream of. :)");
    }
}
