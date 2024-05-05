package pro.cloudnode.smp.cloudnodemsg.command;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;
import pro.cloudnode.smp.cloudnodemsg.Message;
import pro.cloudnode.smp.cloudnodemsg.Permission;
import pro.cloudnode.smp.cloudnodemsg.error.NoPermissionError;
import pro.cloudnode.smp.cloudnodemsg.error.NotInTeamError;
import pro.cloudnode.smp.cloudnodemsg.error.NotPlayerError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeamMessageCommand extends Command {
    @Override
    public boolean run(final @NotNull CommandSender sender, final @NotNull String label, final @NotNull String @NotNull [] args) {
        if (!sender.hasPermission(Permission.USE_TEAM)) return new NoPermissionError().send(sender);
        if (!(sender instanceof final @NotNull Player player)) return new NotPlayerError().send(sender);
        final @NotNull Optional<@NotNull Team> team = Optional.ofNullable(player.getScoreboard().getPlayerTeam(player));
        if (team.isEmpty()) return new NotInTeamError().send(player);
        if (args.length == 0) {
            if (Message.hasTeamChannel(player)) {
                Message.exitTeamChannel(player);
                return sendMessage(player, CloudnodeMSG.getInstance().config().channelTeamClosed(player.getName(), team.get()));
            }
            else {
                Message.createTeamChannel(player);
                return sendMessage(player, CloudnodeMSG.getInstance().config().channelTeamCreated(player.getName(), team.get()));
            }
        }
        return sendTeamMessage(player, team.get(), Component.text(String.join(" ", args)));
    }

    @Override
    public @Nullable List<@NotNull String> onTabComplete(final @NotNull CommandSender sender, final org.bukkit.command.@NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        return new ArrayList<>();
    }

    /**
     * Send message to online team members
     */
    public static boolean sendTeamMessage(final @NotNull Player sender, final @NotNull Team team, final @NotNull Component message) {
        for (final @NotNull Player player : sender.getServer().getOnlinePlayers()) {
            if (Message.isIgnored(player, sender)) continue;
            if (Optional.ofNullable(player.getScoreboard().getPlayerTeam(player)).map(t -> t.equals(team)).orElse(false))
                sendMessage(player, CloudnodeMSG.getInstance().config().team(sender.getName(), team, message));
            else if (player.hasPermission(Permission.SPY)) sendMessage(player, CloudnodeMSG.getInstance().config().teamSpy(sender.getName(), team, message));
        }
        sender.getServer().getConsoleSender().sendMessage(CloudnodeMSG.getInstance().config().teamSpy(sender.getName(), team, message));

        return true;
    }
}
