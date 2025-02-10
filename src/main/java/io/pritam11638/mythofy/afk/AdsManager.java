package io.pritam11638.mythofy.afk;

import io.pritam11638.mythofy.Mythofy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class AdsManager {
    private int adsCycleSpeed = 20;
    private List<String> ads = new ArrayList<>();
    private Map<UUID, BukkitTask> displayTasks = new HashMap<>();
    private final Mythofy plugin;

    public AdsManager(Mythofy plugin) {
        if (plugin.getConfig().getKeys(false).contains("afk-ads")) {
            ads.addAll(plugin.getConfig().getStringList("afk-ads"));
        } else {
            plugin.getLogger().info("No afk-ads found.");
        }

        if (plugin.getConfig().getKeys(false).contains("afk-ads-cycle-speed")) {
            adsCycleSpeed = plugin.getConfig().getInt("afk-ads-cycle-speed");
        } else {
            plugin.getLogger().info("Afk Ads cycle speed not set! Using default 20s.");
        }

        this.plugin = plugin;

        registerEvents();
    }

    public void startAfk(UUID uuid) {
        if (displayTasks.containsKey(uuid)) {
            displayTasks.get(uuid).cancel();
            displayTasks.remove(uuid);
        }

        Player playerC = Bukkit.getPlayer(uuid);

        if (playerC != null && playerC.isOnline()) {
            playerC.playerListName(Component.text("[AFK]").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false).append(Component.text(" ").append(playerC.playerListName())));
        }

        displayTasks.put(uuid, plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            Player player = Bukkit.getPlayer(uuid);

            if (player == null || !player.isOnline()) {
                stopAfk(uuid);
                return;
            }

            player.sendActionBar(Component.text(ads.get(new Random().nextInt(ads.size()))).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        }, adsCycleSpeed*20L, adsCycleSpeed*20L));
    }

    public void stopAfk(UUID uuid) {
        if (displayTasks.containsKey(uuid)) {
            displayTasks.get(uuid).cancel();
            displayTasks.remove(uuid);
        }

        Bukkit.getPlayer(uuid).playerListName(Component.text(Bukkit.getPlayer(uuid).getName()).decoration(TextDecoration.ITALIC, false));
        Bukkit.getPlayer(uuid).sendMessage(Component.text("You are no longer AFK!").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
    }

    public boolean isAfk(UUID uuid) {
        return displayTasks.containsKey(uuid);
    }

    private void registerEvents() {
        plugin.registerEvent(PlayerMoveEvent.class, e -> {
            if (isAfk(e.getPlayer().getUniqueId())) {
                e.getPlayer().performCommand("afk");
            }
        });

        plugin.registerEvent(PlayerInteractEvent.class, e -> {
            if (isAfk(e.getPlayer().getUniqueId())) {
                e.getPlayer().performCommand("afk");
            }
        });
    }
}
