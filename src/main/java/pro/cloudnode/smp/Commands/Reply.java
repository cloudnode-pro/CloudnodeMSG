package pro.cloudnode.smp.Commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static pro.cloudnode.smp.CloudnodeMSG.*;

public class Reply implements @NotNull CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof @NotNull Player)) return true;
        Player player = (@NotNull Player) sender;

        if (!player.hasPermission("cloudnodemsg.use")) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) You do not have permission to run that command!"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) Please specify a message to send to the player!"));
            return true;
        }

        if (replyHashMap.get(player.getUniqueId().toString()) == null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) You have not messaged anyone recently. Please try again later!"));
            return true;
        }

        for (@NotNull Player recipient: server.getOnlinePlayers()) {
            if (recipient.getUniqueId().toString().equalsIgnoreCase(replyHashMap.get(player.getUniqueId().toString()))) {
                @NotNull StringBuilder stringBuilder = new StringBuilder();
                for (String arg: args) stringBuilder.append(arg).append(" ");

                recipient.sendMessage(MiniMessage.miniMessage().deserialize(configFile.getString("incoming").replace("<sender>", player.getName()).replace("<message>", stringBuilder)));
                player.sendMessage(MiniMessage.miniMessage().deserialize(configFile.getString("outgoing").replace("<recipient>", recipient.getName()).replace("<message>", stringBuilder)));
                return true;
            }
        }
        player.sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) This user was not found or went offline. Check your spelling and try again!"));
        return true;
    }
}
