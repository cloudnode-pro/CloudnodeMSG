package pro.cloudnode.smp.cloudnodemsg.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;
import pro.cloudnode.smp.cloudnodemsg.Permission;
import pro.cloudnode.smp.cloudnodemsg.error.NoPermissionError;
import pro.cloudnode.smp.cloudnodemsg.error.NotPlayerError;
import pro.cloudnode.smp.cloudnodemsg.message.Message;

import java.util.List;
import java.util.Objects;

public class ToggleMessageCommand extends Command {
    public static final @NotNull String usage = "<player>";

    @Override
    public boolean run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(Permission.TOGGLE) || (args.length == 1 && !sender.hasPermission(Permission.TOGGLE_OTHER))) return new NoPermissionError().send(sender);
        if (args.length == 1) {
            final @NotNull Player recipient = Objects.requireNonNull(CloudnodeMSG.getInstance().getServer().getPlayer(args[0]));

            if (Message.isIncomeEnabled(recipient)) {
                Message.incomeDisable(recipient);
                sendMessage(sender, CloudnodeMSG.getInstance().config().toggleDisableOther(Objects.requireNonNull(recipient.getName())));

                return true;
            }

            Message.incomeEnable(recipient);
            sendMessage(sender, CloudnodeMSG.getInstance().config().toggleEnableOther(Objects.requireNonNull(recipient.getName())));

            return true;
        }
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
