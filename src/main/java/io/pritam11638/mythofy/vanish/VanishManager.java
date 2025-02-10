package io.pritam11638.mythofy.vanish;

import io.pritam11638.mythofy.Mythofy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishManager {
    private final static Set<UUID> vanished = new HashSet<>();
    private static boolean init = false;
    private final Mythofy plugin;

    public VanishManager(Mythofy plugin) {
        this.plugin = plugin;

        if (init) {
            init(plugin);
        }
    }

    public static void init(Mythofy plugin) {
        init = true;
        plugin.registerEvent(PlayerQuitEvent.class, e -> {
            vanished.remove(e.getPlayer().getUniqueId());

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.showPlayer(plugin, e.getPlayer());
            });
        });
    }

    public boolean toggleVanish(Player player) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (p == player) return;

            if (vanished.contains(p.getUniqueId())) {
                vanished.remove(p.getUniqueId());
                p.showPlayer(plugin, player);
            } else {
                vanished.add(p.getUniqueId());
                if (player.hasPermission("mythofy.vanish.super") && !p.hasPermission("mythofy.vanish.super")) {
                    p.hidePlayer(plugin, player);
                } else if (player.hasPermission("mythofy.vanish.1") && !p.hasPermission("mythofy.vanish.1")) {
                    p.hidePlayer(plugin, player);
                } else if (player.hasPermission("mythofy.vanish.2") && !p.hasPermission("mythofy.vanish.2")) {
                    p.hidePlayer(plugin, player);
                } else if (player.hasPermission("mythofy.vanish.3") && !p.hasPermission("mythofy.vanish.3")) {
                    p.hidePlayer(plugin, player);
                }
            }
        });

        return vanished.contains(player.getUniqueId());
    }
}
