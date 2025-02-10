package io.pritam11638.mythofy.economy.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.pritam11638.mythofy.Mythofy;
import io.pritam11638.mythofy.economy.MythofyCurrency;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class PayCommand implements BasicCommand {
    private final Mythofy plugin;

    public PayCommand(Mythofy plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull CommandSourceStack cSS, @NotNull String[] args) {
        if (args.length < 3) {
            cSS.getSender().sendMessage("Usage: /pay [player] [amount] [currency]");
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if (player == null) {
            cSS.getSender().sendMessage(Component.text("Please enter a valid player name!").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
            return;
        }

        long amount;

        try {
            amount = Long.parseLong(args[1]);
        } catch (NumberFormatException e) {
            cSS.getSender().sendMessage(Component.text("Please enter the amount in numbers.").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
            return;
        }

        MythofyCurrency currency = plugin.getCurrencyManager().getCurrency(args[2]);

        if (currency == null) {
            cSS.getSender().sendMessage(Component.text("Please enter a valid currency!").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
            return;
        }

        if (plugin.getCurrencyManager().getCurrency(args[2]).transfer(((Player) cSS.getSender()).getUniqueId(), player.getUniqueId(), amount)) {
            cSS.getSender().sendMessage(Component.text("Transferred %s to %s's account.".formatted(plugin.getCurrencyManager().getCurrency(args[2]).getFormatted(amount), player.getName())).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
            player.sendMessage(Component.text("Received %s from %s.".formatted(plugin.getCurrencyManager().getCurrency(args[2]).getFormatted(amount), cSS.getSender().getName())));
        } else {
            cSS.getSender().sendMessage(Component.text("You don't have enough balance.").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack cSS, @NotNull String[] args) {
        List<String> tabCompletes = new ArrayList<>();

        if (cSS.getSender() instanceof Player) {
            if (args.length == 0) {
                Bukkit.getOnlinePlayers().forEach(p -> tabCompletes.add(p.getName()));
            }

            if (args.length == 2) {
                plugin.getCurrencyManager().getCurrencies().forEach(c -> tabCompletes.add(c.getCurrency().name()));
            }
        }

        return tabCompletes;
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return sender instanceof Player;
    }
}
