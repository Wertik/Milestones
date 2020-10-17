package space.devport.wertik.milestones.system.storage.mysql;

import org.jetbrains.annotations.NotNull;
import space.devport.utils.ConsoleOutput;
import space.devport.wertik.milestones.system.storage.UserStorage;
import space.devport.wertik.milestones.system.user.struct.ScoreRecord;
import space.devport.wertik.milestones.system.user.struct.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MySQLUserStorage implements UserStorage {

    private final MySQLConnection connection;
    private final Map<String, MySQLStorage> connections = new HashMap<>();

    public MySQLUserStorage(MySQLConnection connection) {
        this.connection = connection;
    }

    public void initialize(Set<String> tables) {
        for (String table : tables) {
            MySQLStorage storage = new MySQLStorage(connection, table);
            storage.initialize();
            connections.put(table, storage);
        }
    }

    @Override
    public @NotNull CompletableFuture<User> loadUser(@NotNull UUID uniqueID) {
        return CompletableFuture.supplyAsync(() -> {
            User user = new User(uniqueID);
            //TODO Replace with stacked CompletableFutures instead of #join()
            for (MySQLStorage storage : connections.values()) {
                ScoreRecord record = storage.loadRecord(uniqueID).join();
                if (record != null)
                    user.updateRecord(storage.getTable(), record);
            }
            return user;
        });
    }

    @Override
    public CompletableFuture<Void> deleteUser(@NotNull UUID uniqueID) {
        return CompletableFuture.runAsync(() -> {
            for (MySQLStorage storage : connections.values()) {
                storage.deleteRecord(uniqueID);
            }
        });
    }

    @Override
    public CompletableFuture<Void> saveUser(@NotNull User user) {
        return CompletableFuture.runAsync(() -> {
            for (MySQLStorage storage : connections.values()) {
                storage.updateRecord(user.getUniqueID(), user.getRecord(storage.getTable()));
            }
        });
    }

    @Override
    public CompletableFuture<Void> saveUsers(@NotNull Set<User> users) {
        return CompletableFuture.runAsync(() -> {
            for (User user : users)
                saveUser(user);
        }).exceptionally((e) -> {
            if (ConsoleOutput.getInstance().isDebug())
                e.printStackTrace();
            return null;
        });
    }
}