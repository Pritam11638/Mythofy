package io.pritam11638.mythofy;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.pritam11638.mythofy.admin.GlobalBroadcastCommand;
import io.pritam11638.mythofy.admin.StaffChatCommand;
import io.pritam11638.mythofy.afk.AdsManager;
import io.pritam11638.mythofy.afk.commands.AfkCommand;
import io.pritam11638.mythofy.economy.MythofyCurrencyManager;
import io.pritam11638.mythofy.economy.commands.AddBalanceCommand;
import io.pritam11638.mythofy.economy.commands.BalanceCommand;
import io.pritam11638.mythofy.economy.commands.PayCommand;
import io.pritam11638.mythofy.economy.commands.SetBalanceCommand;
import io.pritam11638.mythofy.event.ListeningConsumer;
import io.pritam11638.mythofy.sql.DataSource;
import io.pritam11638.mythofy.sql.MySQL;
import io.pritam11638.mythofy.sql.SQLite;
import io.pritam11638.mythofy.vanish.commands.VanishCommand;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

@SuppressWarnings("UnstableApiUsage")
@Getter
public final class Mythofy extends JavaPlugin {
    private DataSource dataSource;
    private MythofyCurrencyManager currencyManager;
    private AdsManager adsManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (getConfig().getBoolean("mysql.enabled")) {
            getLogger().info("Connecting to MySQL...");
            dataSource = new MySQL(this);

            if (!dataSource.isEnabled()) {
                getLogger().info("Couldn't connect to MySQL database.");
            } else {
                getLogger().info("Connected to MySQL database.");
            }
        }

        if (dataSource == null || !dataSource.isEnabled()) {
            getLogger().info("Connecting to SQLite...");
            try {
                dataSource = new SQLite(getDataFolder().getAbsolutePath() + "/mythofy.sqlite3");
                getLogger().info("Connected to SQLite database.");
            } catch (SQLException e) {
                getLogger().severe(e.getMessage());
                getLogger().info("Couldn't connect to SQLite database.");
                getLogger().severe("Plugin relies heavily on SQL for persistence, shutting down plugin...");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }

        currencyManager = new MythofyCurrencyManager(this);
        adsManager = new AdsManager(this);

        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> currencyManager.save(this), 100L, 100L);

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register("balance", new BalanceCommand(this));
            commands.registrar().register("pay", new PayCommand(this));
            commands.registrar().register("setbalance", new SetBalanceCommand(this));
            commands.registrar().register("addbalance", new AddBalanceCommand(this));

            commands.registrar().register("vanish", new VanishCommand(this));

            commands.registrar().register("staffchat", new StaffChatCommand(this));
            commands.registrar().register("globalbroadcast", new GlobalBroadcastCommand());

            commands.registrar().register("afk", new AfkCommand(this));
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        currencyManager.save(this);
    }

    public <T extends Event> void registerEvent(Class<T> eventClass, ListeningConsumer<T> consumer) {
        getServer().getPluginManager().registerEvent(eventClass, consumer, EventPriority.NORMAL, (listener, event) -> {
            if (eventClass.isInstance(event)) {
                consumer.accept(eventClass.cast(event));
            }
        }, this);
    }
}
