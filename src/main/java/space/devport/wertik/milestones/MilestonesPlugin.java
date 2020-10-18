package space.devport.wertik.milestones;

import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import space.devport.utils.ConsoleOutput;
import space.devport.utils.DevportPlugin;
import space.devport.utils.UsageFlag;
import space.devport.utils.commands.MainCommand;
import space.devport.wertik.milestones.commands.MilestoneCommand;
import space.devport.wertik.milestones.commands.subcommands.ReloadSubCommand;
import space.devport.wertik.milestones.listeners.ManagementListener;
import space.devport.wertik.milestones.system.ReloadableTask;
import space.devport.wertik.milestones.system.action.ActionRegistry;
import space.devport.wertik.milestones.system.milestone.MilestoneManager;
import space.devport.wertik.milestones.system.milestone.struct.condition.ConditionRegistry;
import space.devport.wertik.milestones.system.storage.StorageType;
import space.devport.wertik.milestones.system.storage.UserStorage;
import space.devport.wertik.milestones.system.storage.json.JsonStorage;
import space.devport.wertik.milestones.system.storage.mysql.MySQLConnection;
import space.devport.wertik.milestones.system.storage.mysql.MySQLUserStorage;
import space.devport.wertik.milestones.system.user.UserManager;

import java.util.stream.Collectors;

public class MilestonesPlugin extends DevportPlugin {

    @Getter
    private ActionRegistry actionRegistry;

    @Getter
    private ConditionRegistry conditionRegistry;

    @Getter
    private MilestoneManager milestoneManager;

    @Getter
    private UserManager userManager;

    @Getter
    private ReloadableTask saveTask;

    @Getter
    private Permission permissions;
    @Getter
    private Chat chat;

    @Getter
    private StorageType currentStorage = StorageType.INVALID;

    private MainCommand milestoneCommand;

    public static MilestonesPlugin getInstance() {
        return getPlugin(MilestonesPlugin.class);
    }

    @Override
    public void onPluginEnable() {

        //TODO
        // + PlaceholderAPI expansion
        // + setupChat();
        // + setupPermissions();
        // + setupWorldEdit();
        // + setupWorldGuard();

        this.conditionRegistry = new ConditionRegistry(this);

        this.actionRegistry = new ActionRegistry(this);
        actionRegistry.registerListeners();

        this.milestoneManager = new MilestoneManager(this);
        milestoneManager.load();

        // Load again later, in case some actions got registered.
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {
            milestoneManager.loadAdditional();
            actionRegistry.unregisterUnused();
        }, 1L);

        this.userManager = new UserManager(this);
        this.currentStorage = setupStorage();

        // Add only commands, that don't need the storage to be setup.
        milestoneCommand = addMainCommand(new MilestoneCommand())
                .addSubCommand(new ReloadSubCommand(this));

        if (!currentStorage.isValid()) {
            ConsoleOutput.getInstance().err("A valid storage couldn't be initialized.");
            ConsoleOutput.getInstance().err("The plugin will not function properly.");
            ConsoleOutput.getInstance().err("Fix the issue and reload the plugin.");
            return;
        }

        addStorageDependantCommands(milestoneCommand);

        // Load online players
        userManager.load(Bukkit.getOnlinePlayers().stream()
                .map(Player::getUniqueId)
                .collect(Collectors.toSet()));

        registerListener(new ManagementListener(this));

        // Setup auto save
        this.saveTask = new ReloadableTask(this)
                .load(configuration, "auto-save")
                .setup(() -> this.userManager.saveAll())
                .start();
    }

    private void addStorageDependantCommands(MainCommand mainCommand) {
        //TODO Add other commands
    }

    @Override
    public void onPluginDisable() {
        // Execute instantly on disable, async tasks are cut.
        userManager.saveAll().join();
        this.actionRegistry.unregister();
    }

    @Override
    public void onReload() {

        StorageType newType = parseStorageType();

        ConsoleOutput.getInstance().debug("Storage type: " + currentStorage + " -> " + newType);

        milestoneManager.load();

        // Balance action listeners
        actionRegistry.registerUsed();
        actionRegistry.unregisterUnused();

        // Storage changes
        if (newType != currentStorage || currentStorage == StorageType.INVALID) {

            if (currentStorage.isValid())
                this.userManager.saveAll();

            newType = setupStorage();

            if (!newType.isValid()) {
                ConsoleOutput.getInstance().err("A valid storage couldn't be initialized.");
                ConsoleOutput.getInstance().err("The plugin will not function properly.");
                ConsoleOutput.getInstance().err("Fix the issue and reload the plugin.");
                return;
            }

            if (newType.isValid() && !currentStorage.isValid()) {

                registerListener(new ManagementListener(this));
                addStorageDependantCommands(milestoneCommand);

                this.currentStorage = newType;
            }

            // Load online players
            userManager.load(Bukkit.getOnlinePlayers().stream()
                    .map(Player::getUniqueId)
                    .collect(Collectors.toSet()));
        }

        // Setup auto save
        if (saveTask == null)
            this.saveTask = new ReloadableTask(this)
                    .load(configuration, "auto-save")
                    .setup(() -> this.userManager.saveAll())
                    .start();
        else
            saveTask.reload(configuration, "auto-save");
    }

    private StorageType parseStorageType() {
        String type = configuration.getString("storage.type", "json");
        return StorageType.fromString(type);
    }

    private StorageType setupStorage() {

        StorageType storageType = parseStorageType();
        UserStorage userStorage = null;

        switch (storageType) {
            case JSON:
                userStorage = new JsonStorage();
                break;
            case MYSQL:
                MySQLConnection connection = new MySQLConnection(configuration.getString("storage.mysql.host", "localhost"),
                        getConfig().getInt("storage.mysql.port", 3306),
                        configuration.getString("storage.mysql.user", "root"),
                        configuration.getString("storage.mysql.pass"),
                        configuration.getString("storage.mysql.database"));

                try {
                    connection.connect();
                } catch (IllegalStateException e) {
                    ConsoleOutput.getInstance().err("Could not connect to MySQL database, reason: " + e.getMessage());
                    if (ConsoleOutput.getInstance().isDebug())
                        e.printStackTrace();
                    break;
                }

                MySQLUserStorage mySQLUserStorage = new MySQLUserStorage(connection);
                mySQLUserStorage.initialize(milestoneManager.getMilestones());
                userStorage = mySQLUserStorage;
                break;
        }

        if (userStorage != null) {
            this.userManager.initStorage(userStorage);
            return storageType;
        }

        return StorageType.INVALID;
    }

    @Override
    public UsageFlag[] usageFlags() {
        return new UsageFlag[]{UsageFlag.CONFIGURATION, UsageFlag.CUSTOMISATION, UsageFlag.COMMANDS, UsageFlag.LANGUAGE};
    }
}