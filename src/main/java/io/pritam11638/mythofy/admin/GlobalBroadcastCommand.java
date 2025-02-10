package io.pritam11638.mythofy.admin;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@SuppressWarnings("UnstableApiUsage")
public class GlobalBroadcastCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack cSS, @NotNull String[] args) {
        if (args.length == 0) {
            cSS.getSender().sendMessage(Component.text("Please enter a message!").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, true));
        } else {
            StringBuilder message = new StringBuilder();

            Arrays.stream(args).iterator().forEachRemaining(s -> message.append(s).append(" "));

            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Component.text("[BROADCAST] ").color(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false).append(Component.text(message.toString()).color(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false))));
        }
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return sender.hasPermission("mythofy.admin");
    }
}
