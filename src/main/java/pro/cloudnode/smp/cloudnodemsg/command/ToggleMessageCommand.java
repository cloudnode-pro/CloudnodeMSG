package pro.cloudnode.smp.cloudnodemsg.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;
import pro.cloudnode.smp.cloudnodemsg.Permission;
import pro.cloudnode.smp.cloudnodemsg.error.NeverJoinedError;
import pro.cloudnode.smp.cloudnodemsg.error.NoPermissionError;
import pro.cloudnode.smp.cloudnodemsg.error.NotPlayerError;
import pro.cloudnode.smp.cloudnodemsg.message.Message;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ToggleMessageCommand extends Command {
    public static final @NotNull String usage = "<player>";

    @Override
    public boolean run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(Permission.TOGGLE) || (args.length == 1 && !sender.hasPermission(Permission.TOGGLE_OTHER))) return new NoPermissionError().send(sender);
        if (args.length == 1) {
            final @NotNull OfflinePlayer recipient = CloudnodeMSG.getInstance().getServer().getOfflinePlayer(args[0]);

            if (recipient.getPlayer() == null) return new NeverJoinedError(Optional.ofNullable(recipient.getName()).orElse("Unknown Player")).send(sender);

            if (Message.isIncomeEnabled(recipient.getPlayer())) {
                Message.incomeDisable(recipient.getPlayer());
                sendMessage(sender, CloudnodeMSG.getInstance().config().toggleDisableOther(Optional.of(recipient.getPlayer().getName()).orElse("Unknown Player")));

                return true;
            }

            Message.incomeEnable(recipient.getPlayer());
            sendMessage(sender, CloudnodeMSG.getInstance().config().toggleEnableOther(Optional.of(recipient.getPlayer().getName()).orElse("Unknown Player")));

            return true;
        }
        if (args.length > 1) return sendMessage(sender, CloudnodeMSG.getInstance().config().usage(label, usage));
        if (!(sender instanceof final @NotNull Player player)) return new NotPlayerError().send(sender);

        if (Message.isIncomeEnabled(player)) {
            Message.incomeDisable(player);
            sendMessage(sender, CloudnodeMSG.getInstance().config().toggleDisable());

            return true;
        }

        Message.incomeEnable(player);
        sendMessage(sender, CloudnodeMSG.getInstance().config().toggleEnable());

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
