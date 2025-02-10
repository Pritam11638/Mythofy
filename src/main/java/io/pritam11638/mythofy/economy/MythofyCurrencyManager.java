package io.pritam11638.mythofy.economy;

import io.pritam11638.mythofy.Mythofy;
import io.pritam11638.mythofy.sql.DataSource;
import lombok.Getter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
public class MythofyCurrencyManager {
    private final Set<MythofyCurrency> currencies = new HashSet<>();

    public MythofyCurrencyManager(Mythofy plugin) {
        if (plugin.getConfig().contains("economy", true)) {
            for (String currency : plugin.getConfig().getConfigurationSection("economy").getKeys(false)) {
                currencies.add(new MythofyCurrency(new Currency(plugin.getConfig().getString("economy.%s.name".formatted(currency)), plugin.getConfig().getString("economy.%s.symbol".formatted(currency)))));
            }
        }

        init(plugin);
    }

    private void init(Mythofy plugin) {
        DataSource dataSource = plugin.getDataSource();

        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            currencies.forEach(currency -> {
                try {
                    statement.addBatch("""
                            CREATE TABLE IF NOT EXISTS currency_%s(
                                uuid VARCHAR(36) PRIMARY KEY UNIQUE NOT NULL,
                                balance LONG NOT NULL
                            );
                            """.formatted(currency.getCurrency().name()));

                    statement.executeBatch();

                    ResultSet result = statement.executeQuery("SELECT * FROM currency_%s;".formatted(currency.getCurrency().name()));

                    while (result.next()) {
                        UUID uuid = UUID.fromString(result.getString("uuid"));
                        long balance = result.getLong("balance");

                        currency.deposit(uuid, balance);
                    }
                } catch (SQLException ignored) {
                }
            });
        } catch (SQLException ignored) {}
    }

    public void save(Mythofy plugin) {
        DataSource dataSource = plugin.getDataSource();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            currencies.forEach(currency -> {
                String tableName = "currency_" + currency.getCurrency().name();
                for (Map.Entry<UUID, Long> entry : currency.getBalances().entrySet()) {
                    String uuid = entry.getKey().toString();
                    long balance = entry.getValue();
                    String sql = """
                    REPLACE INTO %s (uuid, balance)
                    VALUES ('%s', %d)
                    """.formatted(tableName, uuid, balance);
                    try {
                        statement.addBatch(sql);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MythofyCurrency getCurrency(String name) {
        return currencies.stream().filter(currency -> currency.getCurrency().name().equals(name)).findFirst().orElse(null);
    }
}