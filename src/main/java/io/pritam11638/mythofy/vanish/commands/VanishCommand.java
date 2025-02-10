package io.pritam11638.mythofy.vanish.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.pritam11638.mythofy.Mythofy;
import io.pritam11638.mythofy.vanish.VanishManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class VanishCommand implements BasicCommand {
   private final Mythofy plugin;

   public VanishCommand(Mythofy plugin) {
       this.plugin = plugin;
   }

    @Override
    public void execute(@NotNull CommandSourceStack cSS, @NotNull String[] args) {
        VanishManager vm = new VanishManager(plugin);

        if (args.length == 0) {
            if (cSS.getSender() instanceof Player) {
                if (vm.toggleVanish((Player) cSS.getSender())) {
                    cSS.getSender().sendMessage(Component.text("You are now vanished!").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
                } else {
                    cSS.getSender().sendMessage(Component.text("You are now visible!").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
                }
            } else {
                if (cSS.getSender().hasPermission("mythofy.vanish.others")) {
                    cSS.getSender().sendMessage(Component.text("Please enter a player name!").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
                } else {
                    cSS.getSender().sendMessage("This command can only be run as a player!");
                }
            }
        } else {
            if (cSS.getSender().hasPermission("mythofy.vanish.others")) {
                Player target = Bukkit.getPlayer(args[0]);

                if (target == null) {
                    cSS.getSender().sendMessage(Component.text("Please enter a valid player name!"));
                    return;
                }

                if (vm.toggleVanish(target)) {
                    target.sendMessage(Component.text("You are now vanished!").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
                    cSS.getSender().sendMessage(Component.text("%s is now vanished!".formatted(target.getName())).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
                } else {
                    target.sendMessage(Component.text("You are now visible!").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
                    cSS.getSender().sendMessage(Component.text("%s is now visible!".formatted(target.getName())).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
                }
            }
        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack cSS, @NotNull String[] args) {
       List<String> tabCompletions = new ArrayList<>();

       if (args.length == 1 && cSS.getSender().hasPermission("mythofy.vanish.others")) {
           Bukkit.getOnlinePlayers().forEach(p -> tabCompletions.add(p.getName()));
       }

       return tabCompletions;
    }
}
