package pro.cloudnode.smp.cloudnodemsg;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.command.IgnoreCommand;
import pro.cloudnode.smp.cloudnodemsg.command.MainCommand;
import pro.cloudnode.smp.cloudnodemsg.command.MessageCommand;
import pro.cloudnode.smp.cloudnodemsg.command.ReplyCommand;
import pro.cloudnode.smp.cloudnodemsg.command.ToggleMessageCommand;
import pro.cloudnode.smp.cloudnodemsg.command.UnIgnoreCommand;
import pro.cloudnode.smp.cloudnodemsg.listener.AsyncChatListener;

import java.util.Map;
import java.util.Objects;

public final class CloudnodeMSG extends JavaPlugin {
    public static @NotNull CloudnodeMSG getInstance() {
        return getPlugin(CloudnodeMSG.class);
    }

    public void reload() {
        getInstance().reloadConfig();
        getInstance().config.config = getInstance().getConfig();
        setupDbSource();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reload();

        Objects.requireNonNull(getCommand("cloudnodemsg")).setExecutor(new MainCommand());
        Objects.requireNonNull(getCommand("message")).setExecutor(new MessageCommand());
        Objects.requireNonNull(getCommand("reply")).setExecutor(new ReplyCommand());
        Objects.requireNonNull(getCommand("ignore")).setExecutor(new IgnoreCommand());
        Objects.requireNonNull(getCommand("unignore")).setExecutor(new UnIgnoreCommand());
        Objects.requireNonNull(getCommand("togglemsg")).setExecutor(new ToggleMessageCommand());

        getServer().getPluginManager().registerEvents(new AsyncChatListener(), this);
    }

    @Override
    public void onDisable() {
        dbSource.close();
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

    public final @NotNull HikariConfig hikariConfig = new HikariConfig();
    private HikariDataSource dbSource;

    public @NotNull HikariDataSource db() {
        return dbSource;
    }

    private void setupDbSource() {
        if (dbSource != null) dbSource.close();
        hikariConfig.setDriverClassName("org.sqlite.JDBC");
        hikariConfig.setJdbcUrl("jdbc:sqlite:" + getDataFolder().getAbsolutePath() + "/" + config().dbSqliteFile());

        for (final @NotNull Map.Entry<@NotNull String, @NotNull String> entry : config().dbHikariProperties().entrySet())
            hikariConfig.addDataSourceProperty(entry.getKey(), entry.getValue());

        dbSource = new HikariDataSource(hikariConfig);
    }
}
