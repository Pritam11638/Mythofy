package io.pritam11638.mythofy.admin;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.pritam11638.mythofy.Mythofy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@SuppressWarnings("UnstableApiUsage")
public class StaffChatCommand implements BasicCommand {
    private final Mythofy plugin;

    public StaffChatCommand(Mythofy plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull CommandSourceStack cSS, @NotNull String[] args) {
        if (args.length == 0) {
            cSS.getSender().sendMessage(Component.text("Please enter a message!").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        } else {
            StringBuilder message = new StringBuilder();

            Arrays.stream(args).iterator().forEachRemaining(s -> message.append(s).append(" "));

            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player.hasPermission("mythofy.admin")) {
                    player.sendMessage(Component.text("[STAFFCHAT]").color(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false).append(Component.text(" " + cSS.getSender().getName() + " >> " + message)));
                }
            });
        }
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return sender.hasPermission("mythofy.admin");
    }
}
