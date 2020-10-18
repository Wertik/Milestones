package space.devport.wertik.milestones.system.storage.mysql;

import com.google.common.base.Strings;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import space.devport.utils.ConsoleOutput;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLConnection {

    private HikariDataSource hikari;

    @Getter
    private boolean connected = false;

    private final String host;
    private final int port;
    private final String user;
    private final String password;
    private final String database;

    public MySQLConnection(String host, int port, String user, String password, String database) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
    }

    public void connect() {
        if (Strings.isNullOrEmpty(host))
            throw new IllegalArgumentException("Host cannot be null.");

        this.hikari = new HikariDataSource();
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

        hikari.addDataSourceProperty("serverName", host);
        hikari.addDataSourceProperty("port", port);
        hikari.addDataSourceProperty("databaseName", database);

        hikari.addDataSourceProperty("user", user);
        hikari.addDataSourceProperty("password", password);
        hikari.addDataSourceProperty("characterEncoding", "utf8");
        hikari.addDataSourceProperty("useUnicode", "true");

        hikari.setReadOnly(false);

        try {
            hikari.validate();
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        }

        Connection connection;
        try {
            connection = hikari.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        if (connection == null)
            throw new IllegalStateException("No connection established.");

        this.connected = true;
    }

    public void execute(String query, Object... parameters) {

        if (!isConnected()) return;

        try (Connection connection = hikari.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    statement.setObject(i + 1, parameters[i]);
                }
            }

            statement.execute();
            ConsoleOutput.getInstance().debug("Executed statement " + statement.toString());
        } catch (SQLException e) {
            if (ConsoleOutput.getInstance().isDebug())
                e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query, Object... parameters) {

        if (!isConnected()) return null;

        try (Connection connection = hikari.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    statement.setObject(i + 1, parameters[i]);
                }
            }

            CachedRowSet resultCached = RowSetProvider.newFactory().createCachedRowSet();
            ResultSet resultSet = statement.executeQuery();

            ConsoleOutput.getInstance().debug("Executed query " + statement.toString());

            resultCached.populate(resultSet);
            resultSet.close();

            return resultCached;
        } catch (SQLException e) {
            if (ConsoleOutput.getInstance().isDebug())
                e.printStackTrace();
        }

        return null;
    }
}
