package io.pritam11638.mythofy.economy.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.pritam11638.mythofy.Mythofy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class BalanceCommand implements BasicCommand {
    private final Mythofy plugin;

    public BalanceCommand(Mythofy plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull CommandSourceStack cSS, @NotNull String[] args) {
        if (args.length == 0) {
            cSS.getSender().sendMessage("Usage: /balance [currency]");
            return;
        }

        if (plugin.getCurrencyManager().getCurrency(args[0]) != null) {
            cSS.getSender().sendMessage(Component.text("Your %s balance is %s".formatted(args[0], plugin.getCurrencyManager().getCurrency(args[0]).getFormattedBalance(((Player) cSS.getSender()).getUniqueId()))).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        } else {
            cSS.getSender().sendMessage(Component.text("Please enter a valid currency name.").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack cSS, @NotNull String[] args) {
        List<String> tabCompletes = new ArrayList<>();

        if (args.length == 0) {
            plugin.getCurrencyManager().getCurrencies().forEach(mythofyCurrency -> tabCompletes.add(mythofyCurrency.getCurrency().name()));
        }

        return tabCompletes;
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return sender instanceof Player;
    }
}
