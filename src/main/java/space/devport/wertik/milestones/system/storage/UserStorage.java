package space.devport.wertik.milestones.system.storage;

import org.jetbrains.annotations.NotNull;
import space.devport.wertik.milestones.system.user.struct.User;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserStorage {

    @NotNull CompletableFuture<User> loadUser(@NotNull UUID uniqueID);

    CompletableFuture<Void> deleteUser(@NotNull UUID uniqueID);

    CompletableFuture<Void> saveUser(@NotNull User user);

    CompletableFuture<Void> saveUsers(@NotNull Set<User> users);
}