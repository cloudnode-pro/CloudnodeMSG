package pro.cloudnode.smp.cloudnodemsg.command;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

public abstract class Command implements TabCompleter, CommandExecutor {
    public static boolean sendMessage(final @NotNull Audience recipient, final @NotNull Component message) {
        recipient.sendMessage(message);
        return true;
    }

    public abstract boolean run(final @NotNull CommandSender sender, final @NotNull String label, final @NotNull String @NotNull [] args);

    @Override
    public final boolean onCommand(final @NotNull CommandSender sender, final @NotNull org.bukkit.command.Command command, final @NotNull String label, @NotNull String @NotNull [] args) {
        CloudnodeMSG.getInstance().getServer().getScheduler().runTaskAsynchronously(CloudnodeMSG.getInstance(), () -> {
            final boolean ignored = run(sender, label, args);
        });
        return true;
    }
}
