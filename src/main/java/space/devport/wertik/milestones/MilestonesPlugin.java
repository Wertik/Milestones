package space.devport.wertik.milestones;

import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import space.devport.utils.DevportPlugin;
import space.devport.utils.UsageFlag;
import space.devport.wertik.milestones.commands.MilestoneCommand;
import space.devport.wertik.milestones.commands.subcommands.ReloadSubCommand;
import space.devport.wertik.milestones.system.ReloadableTask;
import space.devport.wertik.milestones.system.action.ActionRegistry;
import space.devport.wertik.milestones.system.milestone.MilestoneManager;
import space.devport.wertik.milestones.system.milestone.struct.condition.ConditionRegistry;
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

        this.actionRegistry = new ActionRegistry(this);
        actionRegistry.registerListeners();

        this.conditionRegistry = new ConditionRegistry(this);

        this.milestoneManager = new MilestoneManager(this);
        milestoneManager.load();

        this.userManager = new UserManager(this);
        setupStorage();

        // Load online players
        userManager.load(Bukkit.getOnlinePlayers().stream()
                .map(Player::getUniqueId)
                .collect(Collectors.toSet()));

        addMainCommand(new MilestoneCommand())
                .addSubCommand(new ReloadSubCommand(this));

        // Setup auto save
        this.saveTask = new ReloadableTask(this)
                .load(configuration, "auto-save")
                .setup(() -> this.userManager.saveAll())
                .start();
    }

    @Override
    public void onPluginDisable() {
        // Execute instantly on disable, async tasks are cut.
        userManager.saveAll().join();
        this.actionRegistry.unregister();
    }

    @Override
    public void onReload() {
        milestoneManager.load();
        saveTask.reload(configuration, "auto-save");
    }

    private void setupStorage() {
        String type = configuration.getString("storage.type", "json");
        switch (type.toLowerCase()) {
            case "json":
            default:
                JsonStorage jsonStorage = new JsonStorage();
                this.userManager.initStorage(jsonStorage);
            case "mysql":
                MySQLConnection connection = new MySQLConnection(configuration.getString("storage.mysql.host", "localhost"),
                        getConfig().getInt("storage.mysql.port", 3306),
                        configuration.getString("storage.mysql.user", "root"),
                        configuration.getString("storage.mysql.pass"),
                        configuration.getString("storage.mysql.database"));

                MySQLUserStorage userStorage = new MySQLUserStorage(connection);
                userStorage.initialize(milestoneManager.getMilestones());
                this.userManager.initStorage(userStorage);
        }
    }

    @Override
    public UsageFlag[] usageFlags() {
        return new UsageFlag[]{UsageFlag.CONFIGURATION, UsageFlag.CUSTOMISATION, UsageFlag.COMMANDS, UsageFlag.LANGUAGE};
    }
}