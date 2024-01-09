package pro.cloudnode.smp.cloudnodemsg.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;
import pro.cloudnode.smp.cloudnodemsg.Permission;
import pro.cloudnode.smp.cloudnodemsg.error.CannotIgnoreError;
import pro.cloudnode.smp.cloudnodemsg.error.NeverJoinedError;
import pro.cloudnode.smp.cloudnodemsg.error.NoPermissionError;
import pro.cloudnode.smp.cloudnodemsg.error.NotPlayerError;
import pro.cloudnode.smp.cloudnodemsg.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class IgnoreCommand extends Command {
    public static final @NotNull String usage = "<player>";

    @Override
    public boolean run(final @NotNull CommandSender sender, final @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission(Permission.IGNORE)) return new NoPermissionError().send(sender);
        if (!(sender instanceof final @NotNull Player player)) return new NotPlayerError().send(sender);
        if (args.length == 0) return sendMessage(player, CloudnodeMSG.getInstance().config().usage(label, usage));
        final @NotNull OfflinePlayer target = CloudnodeMSG.getInstance().getServer().getOfflinePlayer(args[0]);
        if (Message.isIgnored(player, target)) return unignore(player, target);
        return ignore(player, target);
    }

    public static boolean ignore(final @NotNull Player player, final @NotNull OfflinePlayer target) {
        if (target.isOnline() && Objects.requireNonNull(target.getPlayer()).hasPermission(Permission.IGNORE_IMMUNE)) return new CannotIgnoreError(Optional.ofNullable(target.getName()).orElse("Unknown Player")).send(player);
        if (!target.isOnline() && !target.hasPlayedBefore()) return new NeverJoinedError(Optional.ofNullable(target.getName()).orElse("Unknown Player")).send(player);
        Message.ignore(player, target);
        return sendMessage(player, CloudnodeMSG.getInstance().config().ignored(Optional.ofNullable(target.getName()).orElse("Unknown Player")));
    }

    public static boolean unignore(final @NotNull Player player, final @NotNull OfflinePlayer target) {
        Message.unignore(player, target);
        return sendMessage(player, CloudnodeMSG.getInstance().config().unignored(Optional.ofNullable(target.getName()).orElse("Unknown Player")));
    }

    @Override
    public @Nullable List<@NotNull String> onTabComplete(final @NotNull CommandSender sender, final @NotNull org.bukkit.command.Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        if (args.length == 1 && sender.hasPermission(Permission.IGNORE) && sender instanceof Player) return null;
        return new ArrayList<>();
    }
}
