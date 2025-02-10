package io.pritam11638.mythofy.afk.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.pritam11638.mythofy.Mythofy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("UnstableApiUsage")
public class AfkCommand implements BasicCommand {
    private final Mythofy plugin;

    public AfkCommand(Mythofy plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull CommandSourceStack cSS, @NotNull String[] args) {
        if (plugin.getAdsManager().isAfk(((Player) cSS.getSender()).getUniqueId())) {
            plugin.getAdsManager().stopAfk(((Player) cSS.getSender()).getUniqueId());
        } else {
            cSS.getSender().sendMessage(Component.text("You are now afk!", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
            plugin.getAdsManager().startAfk(((Player) cSS.getSender()).getUniqueId());
        }
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return sender instanceof Player;
    }
}
