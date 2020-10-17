package space.devport.wertik.milestones.system.storage;

import org.jetbrains.annotations.NotNull;
import space.devport.wertik.milestones.system.user.struct.User;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserStorage {
    @NotNull CompletableFuture<User> loadUser(@NotNull UUID uniqueID);

    void deleteUser(@NotNull UUID uniqueID);

    void saveUser(@NotNull User user);

    void saveUsers(@NotNull Set<User> users);
}