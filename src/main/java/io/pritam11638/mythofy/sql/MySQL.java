package io.pritam11638.mythofy.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import io.pritam11638.mythofy.Mythofy;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL implements DataSource {
    private final HikariConfig config = new HikariConfig();
    private HikariDataSource dataSource;

    public MySQL(Mythofy plugin) {
        init(plugin.getConfig());
    }

    private void init(FileConfiguration pluginConfig) {
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s",
                pluginConfig.getString("mysql.hostname"),
                pluginConfig.getString("mysql.port"),
                pluginConfig.getString("mysql.database-name")));
        config.setUsername(pluginConfig.getString("mysql.username"));
        config.setPassword(pluginConfig.getString("mysql.password"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setConnectionTestQuery("SELECT 1");

        try {
            dataSource = new HikariDataSource(config);
        } catch (HikariPool.PoolInitializationException e) {
            dataSource = null;
        }
    }

    @Override
    public boolean isEnabled() {
        if (dataSource == null || dataSource.isClosed()) {
            return false;
        }

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            return stmt.execute("SELECT 1");
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}