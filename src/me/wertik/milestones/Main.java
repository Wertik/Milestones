package me.wertik.milestones;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.wertik.milestones.handlers.ConditionHandler;
import me.wertik.milestones.handlers.DataHandler;
import me.wertik.milestones.handlers.StorageHandler;
import me.wertik.milestones.listeners.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private static ConfigLoader configLoader;
    private static DataHandler dataHandler;
    private static StorageHandler storageHandler;
    private static Utils utils;
    private static ConditionHandler conditionHandler;

    public static Main getInstance() {
        return Main.instance;
    }

    // Let's do this.
    @Override
    public void onEnable() {
        ConsoleCommandSender console = getServer().getConsoleSender();
        console.sendMessage("§2Enabling milestones, sign your GDPR, i am going to steal your data.");
        console.sendMessage("§f-------------------------------------");

        instance = this;
        configLoader = new ConfigLoader();
        dataHandler = new DataHandler();
        storageHandler = new StorageHandler();
        utils = new Utils();

        // Commands, listeners
        getCommand("milestones").setExecutor(new me.wertik.milestones.commands.Commands());

        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);

        // Placeholder API
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI").isEnabled()) {
            new PlaceholderAPIHook(this).hook();
            console.sendMessage("§aPlaceholder API hooked successfuly.");
        }

        // WorldEdit
        if (this.getWorldEdit() == null) {
            console.sendMessage("§cWorld Edit is fine, why don't you use it?");
        } else
            console.sendMessage("§aWorld Edit hooked successfuly.");

        // WorldGuard
        if (this.getWorldGuard() == null) {
            console.sendMessage("§cDo you want a useful plugin? WORLD GUARD!");
        } else
            console.sendMessage("§aWorld Guard hooked successfuly.");

        configLoader.loadYamls();

        storageHandler.setYamls();

        configLoader.loadMilestones();

        dataHandler.loadFiles();

        console.sendMessage("§f-------------------------------------");
        console.sendMessage("§2Successfuly enabled Data Stealer §f" + getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        ConsoleCommandSender console = getServer().getConsoleSender();
        DataHandler dataHandler = new DataHandler();
        StorageHandler storageHandler = new StorageHandler();
        storageHandler.saveStorage();
        dataHandler.saveDataFiles();
        console.sendMessage("§2It's okay now, i got all data i could ever dream of. :)");
    }

    public ConfigLoader getConfigLoader() {return configLoader;}

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public StorageHandler getStorageHandler() {
        return storageHandler;
    }

    public Utils getUtils() {
        return utils;
    }

    public ConditionHandler getConditionHandler() {
        return conditionHandler;
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    public WorldEditPlugin getWorldEdit() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
            return null;
        }

        return (WorldEditPlugin) plugin;
    }
}
