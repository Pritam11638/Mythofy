package io.pritam11638.mythofy.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataSource {
    boolean isEnabled();
    Connection getConnection() throws SQLException;
    void close() throws SQLException;
}
