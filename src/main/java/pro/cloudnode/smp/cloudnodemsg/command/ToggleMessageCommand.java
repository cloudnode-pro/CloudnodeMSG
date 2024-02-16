package pro.cloudnode.smp.cloudnodemsg.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;
import pro.cloudnode.smp.cloudnodemsg.Message;
import pro.cloudnode.smp.cloudnodemsg.Permission;
import pro.cloudnode.smp.cloudnodemsg.error.NeverJoinedError;
import pro.cloudnode.smp.cloudnodemsg.error.NoPermissionError;
import pro.cloudnode.smp.cloudnodemsg.error.NotPlayerError;

import java.util.ArrayList;
import java.util.List;

public final class ToggleMessageCommand extends Command {
    @Override
    public boolean run(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission(Permission.TOGGLE) || (args.length == 1 && !sender.hasPermission(Permission.TOGGLE_OTHER)))
            return new NoPermissionError().send(sender);
        if (args.length == 1) {
            final @NotNull OfflinePlayer recipient = CloudnodeMSG.getInstance().getServer().getOfflinePlayer(args[0]);

            if (recipient.getPlayer() == null)
                return new NeverJoinedError(recipient).send(sender);

            if (Message.isIncomingEnabled(recipient.getPlayer())) {
                Message.incomingDisable(recipient.getPlayer());
                return sendMessage(sender, CloudnodeMSG.getInstance().config().toggleDisableOther(recipient));
            }

            Message.incomingEnable(recipient.getPlayer());
            return sendMessage(sender, CloudnodeMSG.getInstance().config().toggleEnableOther(recipient));
        }
        if (!(sender instanceof final @NotNull Player player)) return new NotPlayerError().send(sender);

        if (Message.isIncomingEnabled(player)) {
            Message.incomingDisable(player);
            return sendMessage(sender, CloudnodeMSG.getInstance().config().toggleDisable());
        }

        Message.incomingEnable(player);
        return sendMessage(sender, CloudnodeMSG.getInstance().config().toggleEnable());
    }

    @Override
    public @Nullable List<@NotNull String> tab(final @NotNull CommandSender sender, final @NotNull String label, final @NotNull String @NotNull [] args) {
        if (sender.hasPermission(Permission.TOGGLE_OTHER))
            return null;
        return new ArrayList<>();
    }
}
