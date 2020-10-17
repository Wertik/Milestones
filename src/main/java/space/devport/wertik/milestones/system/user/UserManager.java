package space.devport.wertik.milestones.system.user;

import org.jetbrains.annotations.Nullable;
import space.devport.utils.ConsoleOutput;
import space.devport.wertik.milestones.MilestonesPlugin;
import space.devport.wertik.milestones.system.storage.UserStorage;
import space.devport.wertik.milestones.system.user.struct.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserManager {

    private final MilestonesPlugin plugin;

    private final Map<UUID, User> loadedUsers = new HashMap<>();

    private UserStorage storage;

    public UserManager(MilestonesPlugin plugin) {
        this.plugin = plugin;
    }

    public void initStorage(UserStorage storage) {
        this.storage = storage;
    }

    public CompletableFuture<Void> saveAll() {
        return CompletableFuture.runAsync(() -> {
            final Set<User> users = new HashSet<>(this.loadedUsers.values());

            //TODO Stack the futures.
            for (User user : users)
                saveUser(user);

            ConsoleOutput.getInstance().debug("Saved " + this.loadedUsers.size() + " users...");
        });
    }

    public User getUser(UUID uniqueID) {
        User user = this.loadedUsers.get(uniqueID);
        if (user == null)
            user = loadUser(uniqueID);
        return user;
    }

    public User getOrCreateUser(UUID uniqueID) {
        User user = getUser(uniqueID);
        return user == null ? createUser(uniqueID) : user;
    }

    public User createUser(UUID uniqueID) {
        User user = new User(uniqueID);
        this.loadedUsers.put(uniqueID, user);
        ConsoleOutput.getInstance().debug("Created user " + user.getName());
        return user;
    }

    @Nullable
    public User loadUser(UUID uniqueID) {
        User user = storage.loadUser(uniqueID).join();
        if (user != null) {
            this.loadedUsers.put(uniqueID, user);
            ConsoleOutput.getInstance().debug("Loaded user " + user.getName());
        }
        return user;
    }

    public CompletableFuture<Void> saveUser(UUID uniqueID) {
        return storage.saveUser(getUser(uniqueID));
    }

    public CompletableFuture<Void> saveUser(User user) {
        return storage.saveUser(user).thenAccept((unused) -> ConsoleOutput.getInstance().debug("Saved user " + user.getName()));
    }

    public CompletableFuture<Void> unloadUser(UUID uniqueID) {
        this.loadedUsers.remove(uniqueID);
        return saveUser(uniqueID);
    }

    public boolean isLoaded(UUID uniqueID) {
        return this.loadedUsers.containsKey(uniqueID);
    }

    /**
     * Load a set of users.
     */
    public CompletableFuture<Set<User>> load(Set<UUID> players) {
        return CompletableFuture.supplyAsync(() -> {
            Set<User> users = new HashSet<>();

            for (UUID uniqueID : players)
                users.add(loadUser(uniqueID));

            ConsoleOutput.getInstance().debug("Loaded " + users.size() + " user(s)...");
            return users;
        });
    }

    public Set<User> getUsers(Predicate<User> condition) {
        return this.loadedUsers.values().stream().filter(condition).collect(Collectors.toSet());
    }
}