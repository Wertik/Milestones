package space.devport.wertik.milestones.system.storage.mysql;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.devport.utils.ConsoleOutput;
import space.devport.utils.utility.FastUUID;
import space.devport.wertik.milestones.system.user.struct.ScoreRecord;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class MySQLStorage {

    private final MySQLConnection connection;
    @Getter
    private final String table;

    public MySQLStorage(MySQLConnection connection, String table) {
        this.connection = connection;
        this.table = table;
    }

    public void initialize() {
        CompletableFuture.runAsync(() -> connection.execute(Query.CREATE_RECORD_TABLE.get(table)))
                .thenRun(() -> ConsoleOutput.getInstance().debug("MySQL storage with table " + table + " initialized."))
                .exceptionally((exc) -> {
                    if (ConsoleOutput.getInstance().isDebug())
                        exc.printStackTrace();
                    return null;
                });
    }

    public @NotNull CompletableFuture<ScoreRecord> loadRecord(@NotNull UUID uniqueID) {
        return CompletableFuture.supplyAsync(() -> {
            ResultSet resultSet = connection.executeQuery(Query.GET_RECORD.get(table), FastUUID.toString(uniqueID));

            ScoreRecord record = new ScoreRecord(table);
            try {
                if (resultSet.next()) {
                    long score = resultSet.getLong("score");
                    record.update(score);
                } else return null;
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
            return record;
        }).exceptionally((e) -> {
            if (ConsoleOutput.getInstance().isDebug())
                e.printStackTrace();
            return null;
        });
    }

    public void deleteRecord(@NotNull UUID uniqueID) {
        CompletableFuture.runAsync(() -> connection.execute(Query.DELETE_RECORD.get(table), FastUUID.toString(uniqueID))).exceptionally((e) -> {
            if (ConsoleOutput.getInstance().isDebug())
                e.printStackTrace();
            return null;
        });
    }

    public void updateRecord(@NotNull UUID uniqueID, @Nullable ScoreRecord record) {

        if (record == null) {
            ConsoleOutput.getInstance().debug("User " + uniqueID + " has no records for this milestone, purging.");
            deleteRecord(uniqueID);
            return;
        }

        CompletableFuture.runAsync(() -> {
            String stringUUID = FastUUID.toString(uniqueID);
            connection.execute(Query.UPDATE_RECORD.get(table),
                    stringUUID,
                    record.getScore(),
                    stringUUID,
                    record.getScore());
        }).exceptionally((e) -> {
            if (ConsoleOutput.getInstance().isDebug())
                e.printStackTrace();
            return null;
        });
    }
}
