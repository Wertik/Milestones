package space.devport.wertik.milestones.system.storage.json;

import org.jetbrains.annotations.NotNull;
import space.devport.wertik.milestones.exceptions.NotImplementedException;
import space.devport.wertik.milestones.system.storage.UserStorage;
import space.devport.wertik.milestones.system.user.struct.User;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class JsonStorage implements UserStorage {

    @Override
    public @NotNull CompletableFuture<User> loadUser(@NotNull UUID uniqueID) {
        throw new NotImplementedException("Json storage is not implemented, use MySQL.");
    }

    @Override
    public void deleteUser(@NotNull UUID uniqueID) {
        throw new NotImplementedException("Json storage is not implemented, use MySQL.");
    }

    @Override
    public void saveUser(@NotNull User user) {
        throw new NotImplementedException("Json storage is not implemented, use MySQL.");
    }

    @Override
    public void saveUsers(@NotNull Set<User> users) {
        throw new NotImplementedException("Json storage is not implemented, use MySQL.");
    }
}
