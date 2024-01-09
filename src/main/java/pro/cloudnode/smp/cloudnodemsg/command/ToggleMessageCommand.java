package pro.cloudnode.smp.cloudnodemsg.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;
import pro.cloudnode.smp.cloudnodemsg.Permission;
import pro.cloudnode.smp.cloudnodemsg.error.NoPermissionError;
import pro.cloudnode.smp.cloudnodemsg.message.Message;

import java.util.List;
import java.util.Objects;

public class ToggleMessageCommand extends Command {
    public static final @NotNull String usage = "<player>";
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(Permission.TOGGLE) || (args.length == 1 && !sender.hasPermission(Permission.TOGGLE_OTHER))) return new NoPermissionError().send(sender);
        if (args.length == 1) {
            final @NotNull OfflinePlayer recipient = CloudnodeMSG.getInstance().getServer().getOfflinePlayer(args[0]);

            Message.setNoIncoming(recipient);
            sendMessage(sender, Message.getNoIncoming(recipient) ?
                    CloudnodeMSG.getInstance().config().toggleDisableOther(Objects.requireNonNull(recipient.getName())) :
                    CloudnodeMSG.getInstance().config().toggleEnableOther(Objects.requireNonNull(recipient.getName()))
            );
            return true;
        }

        Message.setNoIncoming(Objects.requireNonNull(Message.offlinePlayer(sender)));
        sendMessage(sender, Message.getNoIncoming(Message.offlinePlayer(sender)) ?
                CloudnodeMSG.getInstance().config().toggleDisable() :
                CloudnodeMSG.getInstance().config().toggleEnable()
        );
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
