package io.pritam11638.mythofy.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite implements DataSource {
    private final String path;
    private boolean isEnabled = false;

    public SQLite(String path) throws SQLException {
        this.path = path;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        if (connection != null && !connection.isClosed()) {
            isEnabled = true;
        }

        return connection;
    }

    @Override
    public void close() throws SQLException {
    }
}
