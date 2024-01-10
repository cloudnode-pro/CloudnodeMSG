package pro.cloudnode.smp.cloudnodemsg;

import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.command.IgnoreCommand;
import pro.cloudnode.smp.cloudnodemsg.command.MainCommand;
import pro.cloudnode.smp.cloudnodemsg.command.MessageCommand;
import pro.cloudnode.smp.cloudnodemsg.command.ReplyCommand;
import pro.cloudnode.smp.cloudnodemsg.command.UnIgnoreCommand;
import pro.cloudnode.smp.cloudnodemsg.listener.AsyncChatListener;
import pro.cloudnode.smp.cloudnodemsg.command.ToggleMessageCommand;

import java.util.Objects;

public final class CloudnodeMSG extends JavaPlugin {
    public static @NotNull CloudnodeMSG getInstance() {
        return getPlugin(CloudnodeMSG.class);
    }

    public void reload() {
        getInstance().reloadConfig();
        getInstance().config.config = getInstance().getConfig();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reload();

        Objects.requireNonNull(getCommand("cloudnodemsg")).setExecutor(new MainCommand());
        Objects.requireNonNull(getCommand("message")).setExecutor(new MessageCommand());
        Objects.requireNonNull(getCommand("reply")).setExecutor(new ReplyCommand());Objects.requireNonNull(getCommand("ignore")).setExecutor(new IgnoreCommand());
        Objects.requireNonNull(getCommand("unignore")).setExecutor(new UnIgnoreCommand());
        Objects.requireNonNull(getCommand("togglemsg")).setExecutor(new ToggleMessageCommand());

        getServer().getPluginManager().registerEvents(new AsyncChatListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static boolean isVanished(final @NotNull Player player) {
        for (final @NotNull MetadataValue meta : player.getMetadata("vanished"))
            if (meta.asBoolean()) return true;
        return false;
    }

    private final @NotNull PluginConfig config = new PluginConfig(getConfig());

    public @NotNull PluginConfig config() {
        return config;
    }
}
