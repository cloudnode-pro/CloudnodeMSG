package pro.cloudnode.smp.cloudnodemsg.command;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

public abstract class Command implements TabCompleter, CommandExecutor {
    public boolean sendMessage(final @NotNull Audience recipient, final @NotNull Component message) {
        recipient.sendMessage(message);
        return true;
    }
}
