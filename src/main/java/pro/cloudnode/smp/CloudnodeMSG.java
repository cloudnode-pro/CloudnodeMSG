package pro.cloudnode.smp;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public final class CloudnodeMSG extends JavaPlugin {
    public static @NotNull FileConfiguration configFile;
    public static @NotNull HikariDataSource database;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        configFile = this.getConfig();
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
