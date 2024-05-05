package pro.cloudnode.smp.cloudnodemsg.command;

import io.papermc.paper.plugin.configuration.PluginMeta;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;
import pro.cloudnode.smp.cloudnodemsg.Permission;
import pro.cloudnode.smp.cloudnodemsg.error.NoPermissionError;

import java.util.ArrayList;
import java.util.List;

public final class MainCommand extends Command {

    @Override
    public boolean run(final @NotNull CommandSender sender, final @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) switch (args[0]) {
            case "reload", "rl" -> {
                return reload(sender);
            }
        }
        return info(sender);
    }

    private boolean info(final @NotNull CommandSender sender) {
        final @NotNull PluginMeta meta = CloudnodeMSG.getInstance().getPluginMeta();
        return sendMessage(sender, MiniMessage.miniMessage()
                .deserialize("<green><name> by <author></green><newline><green>Version: <white><version></white></green>", Placeholder.unparsed("name", meta.getName()), Placeholder.unparsed("author", String.join(", ", meta.getAuthors())), Placeholder.unparsed("version", meta.getVersion())));
    }

    private boolean reload(final @NotNull CommandSender sender) {
        if (!sender.hasPermission(Permission.RELOAD)) return new NoPermissionError().send(sender);
        CloudnodeMSG.getInstance().reload();
        return sendMessage(sender, CloudnodeMSG.getInstance().config().reloaded());
    }

    @Override
    public @NotNull List<@NotNull String> onTabComplete(final @NotNull CommandSender sender, final @NotNull org.bukkit.command.Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        final @NotNull List<@NotNull String> completions = new ArrayList<>();
        if (args.length == 1) if (sender.hasPermission(Permission.RELOAD) && "reload".startsWith(args[0].toLowerCase()))
            completions.add("reload");
        return completions;
    }
}
