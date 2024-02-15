package pro.cloudnode.smp.cloudnodemsg;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pro.cloudnode.smp.cloudnodemsg.command.IgnoreCommand;
import pro.cloudnode.smp.cloudnodemsg.command.MainCommand;
import pro.cloudnode.smp.cloudnodemsg.command.MessageCommand;
import pro.cloudnode.smp.cloudnodemsg.command.ReplyCommand;
import pro.cloudnode.smp.cloudnodemsg.command.ToggleMessageCommand;
import pro.cloudnode.smp.cloudnodemsg.command.UnIgnoreCommand;
import pro.cloudnode.smp.cloudnodemsg.listener.AsyncChatListener;
import pro.cloudnode.smp.cloudnodemsg.listener.PlayerJoinListener;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public final class CloudnodeMSG extends JavaPlugin {
    public static @NotNull CloudnodeMSG getInstance() {
        return getPlugin(CloudnodeMSG.class);
    }

    public void reload() {
        getInstance().reloadConfig();
        getInstance().config.config = getInstance().getConfig();
        setupDbSource();
        runDDL();
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
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        minuteLoop = minuteLoop();
    }

    @Override
    public void onDisable() {
        if (dbSource != null) dbSource.close();
        if (minuteLoop != null) minuteLoop.cancel();
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
    private @Nullable HikariDataSource dbSource;

    public @NotNull HikariDataSource db() {
        assert dbSource != null;
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

    /**
     * Run DDL script
     */
    public void runDDL() {
        final @NotNull String file = "ddl/sqlite.sql";
        final @NotNull String @NotNull [] queries;
        try (final @Nullable InputStream inputStream = getClassLoader().getResourceAsStream(file)) {
            queries = Arrays.stream(
                    new String(Objects.requireNonNull(inputStream).readAllBytes()).split(";")
            ).map(q -> q.stripTrailing().stripIndent().replaceAll("^\\s+(?:--.+)*", "")).toArray(String[]::new);
        }
        catch (final @NotNull Exception exception) {
            getLogger().log(Level.SEVERE, "Could not read DDL script: " + file, exception);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        for (final @NotNull String query : queries) {
            if (query.isBlank()) continue;
            try (final @NotNull PreparedStatement stmt = db().getConnection().prepareStatement(query)) {
                stmt.execute();
            }
            catch (final @NotNull SQLException exception) {
                getLogger().log(Level.SEVERE, "Could not execute DDL query: " + query, exception);
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }
        getLogger().info("Database successfully initialised with DDL");
    }

    /**
     * Run code asynchronously
     */
    public static void runAsync(final @NotNull Runnable runnable) {
        getInstance().getServer().getScheduler().runTaskAsynchronously(getInstance(), runnable);
    }

    private @NotNull BukkitTask minuteLoop() {
        return getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            Mail.notifyUnread();
        }, 0, 20 * 60);
    }
    private @Nullable BukkitTask minuteLoop;
}
