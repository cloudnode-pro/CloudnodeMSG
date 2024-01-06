package pro.cloudnode.smp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.Commands.Message;
import pro.cloudnode.smp.Commands.Reply;

import java.util.HashMap;

public final class CloudnodeMSG extends JavaPlugin {
    public static @NotNull HashMap<String, String> replyHashMap;
    public static @NotNull FileConfiguration configFile;
    public static @NotNull HikariDataSource database;
    public static @NotNull Server server;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        initDbSource();

        configFile = this.getConfig();
        server = getServer();
        replyHashMap = new HashMap<>();

        getCommand("message").setExecutor(new @NotNull Message());
        getCommand("reply").setExecutor(new @NotNull Reply());
    }

    @Override
    public void onDisable() {
        database.close();
    }

    private void initDbSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite:" + getDataFolder().getAbsolutePath() + "/messages.db");
        database = new HikariDataSource(config);

    }
}
