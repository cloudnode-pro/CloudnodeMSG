package pro.cloudnode.smp.cloudnodemsg;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.command.MainCommand;
import pro.cloudnode.smp.cloudnodemsg.command.MessageCommand;

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private final @NotNull PluginConfig config = new PluginConfig(getConfig());

    public @NotNull PluginConfig config() {
        return config;
    }
}
