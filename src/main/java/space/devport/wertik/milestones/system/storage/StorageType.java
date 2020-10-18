package space.devport.wertik.milestones.system.storage;

import com.google.common.base.Strings;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public enum StorageType {

    INVALID(false),
    JSON,
    MYSQL("sql");

    @Getter
    private boolean valid = true;

    @Getter
    private final String[] aliases;

    StorageType(String... aliases) {
        this.aliases = aliases;
    }

    StorageType(boolean valid, String... aliases) {
        this.valid = valid;
        this.aliases = aliases;
    }

    @NotNull
    public static StorageType fromString(String str) {

        if (Strings.isNullOrEmpty(str))
            return StorageType.INVALID;

        try {
            return StorageType.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException ignored) {
        }

        return Arrays.stream(values())
                .filter(v -> Arrays.asList(v.getAliases()).contains(str.toLowerCase()))
                .findAny().orElse(StorageType.INVALID);
    }
}