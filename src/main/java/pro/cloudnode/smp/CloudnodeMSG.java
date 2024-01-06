package pro.cloudnode.smp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.HashMap;

public final class CloudnodeMSG extends JavaPlugin {
    public static @NotNull FileConfiguration configFile;
    public static @NotNull HikariDataSource database;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        initDbSource();
        configFile = this.getConfig();
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initDbSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite:" + getDataFolder().getAbsolutePath() + "/messages.db");
        database = new HikariDataSource(config);
    }
}
