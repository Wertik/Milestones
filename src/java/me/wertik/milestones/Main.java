package me.wertik.milestones;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.wertik.milestones.handlers.ConditionHandler;
import me.wertik.milestones.handlers.DataHandler;
import me.wertik.milestones.handlers.StorageHandler;
import me.wertik.milestones.listeners.EventListener;
import me.wertik.milestones.listeners.TestListner;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    private static Main instance;

    private static Permission perms;
    private static Chat chat;

    private ConfigLoader configLoader;
    private DataHandler dataHandler;
    private StorageHandler storageHandler;
    private ConditionHandler conditionHandler;

    public static Main getInstance() {
        return Main.instance;
    }

    // Let's do this.
    @Override
    public void onEnable() {
        ConsoleCommandSender console = getServer().getConsoleSender();
        console.sendMessage("§2Enabling milestones.");
        console.sendMessage("§f-------------------------------------");

        instance = this;
        configLoader = new ConfigLoader();
        dataHandler = new DataHandler();
        storageHandler = new StorageHandler();
        conditionHandler = new ConditionHandler();

        // Commands, listeners
        getCommand("milestones").setExecutor(new me.wertik.milestones.commands.Commands());

        getServer().getPluginManager().registerEvents(new EventListener(), this);

        // Placeholder API
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI").isEnabled()) {
            new PlaceholderAPIHook(this).hook();
            console.sendMessage("§aPlaceholder API hooked successfully.");
        }

        // WorldEdit
        if (this.getWorldEdit() == null) {
            console.sendMessage("§cWorld Edit is fine, why don't you use it?");
        } else
            console.sendMessage("§aWorld Edit hooked successfully.");

        // WorldGuard
        if (this.getWorldGuard() == null) {
            console.sendMessage("§cDo you want a useful plugin? WORLD GUARD!");
        } else
            console.sendMessage("§aWorld Guard hooked successfully.");

        configLoader.loadYamls();

        storageHandler.setYamls();

        configLoader.loadMilestones();

        dataHandler.loadFiles();

        console.sendMessage("§f-------------------------------------");
        console.sendMessage("§2Successfully enabled §f" + getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        ConsoleCommandSender console = getServer().getConsoleSender();
        storageHandler.saveStorage();
        dataHandler.saveDataFiles();
        console.sendMessage("§2Disabled.");
    }

    public void reload() {
        dataHandler.saveDataFiles();
        storageHandler.saveStorage();
        configLoader.loadYamls();
        configLoader.loadMilestones();
        dataHandler.loadFiles();
        reloadConfig();
        storageHandler.setYamls();
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public StorageHandler getStorageHandler() {
        return storageHandler;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    public static Permission getPerms() {
        return perms;
    }

    public static Chat getChat() {
        return chat;
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

    public List<String> getWorldNames() {
        List<String> worldNames = new ArrayList<>();
        getServer().getWorlds().forEach(world -> worldNames.add(world.getName()));
        return worldNames;
    }
}
