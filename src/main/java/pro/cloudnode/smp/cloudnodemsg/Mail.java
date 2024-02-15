package pro.cloudnode.smp.cloudnodemsg;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

public final class Mail {
    public final @NotNull UUID id;
    public final @NotNull OfflinePlayer sender;
    public final @NotNull OfflinePlayer recipient;
    public final @NotNull String message;
    public boolean seen;

    public boolean starred;

    public Mail(final @NotNull OfflinePlayer sender, final @NotNull OfflinePlayer recipient, final @NotNull String message) {
        this.id = UUID.randomUUID();
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.seen = false;
        this.starred = false;
    }

    public Mail(final @NotNull ResultSet rs) throws SQLException {
        final @NotNull Server server = CloudnodeMSG.getInstance().getServer();

        this.id = UUID.fromString(rs.getString("id"));
        this.sender = server.getOfflinePlayer(UUID.fromString(rs.getString("sender")));
        this.recipient = server.getOfflinePlayer(UUID.fromString(rs.getString("recipient")));
        this.message = rs.getString("message");
        this.seen = rs.getBoolean("seen");
        this.starred = rs.getBoolean("starred");
    }

    public void update() {
        try (final @NotNull PreparedStatement stmt = CloudnodeMSG.getInstance().db().getConnection().prepareStatement("UPDATE `cloudnodemsg_mail` SET `seen` = ?, `starred` = ? WHERE `id` = ?")) {
            stmt.setBoolean(1, seen);
            stmt.setBoolean(2, starred);
            stmt.setString(3, id.toString());
            stmt.executeUpdate();
        }
        catch (final @NotNull SQLException exception) {
            CloudnodeMSG.getInstance().getLogger().log(Level.SEVERE, "Could not update mail: " + id, exception);
        }
    }

    public void delete() {
        try (final @NotNull PreparedStatement stmt = CloudnodeMSG.getInstance().db().getConnection().prepareStatement("DELETE FROM `cloudnodemsg_mail` WHERE `id` = ?")) {
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        }
        catch (final @NotNull SQLException exception) {
            CloudnodeMSG.getInstance().getLogger().log(Level.SEVERE, "Could not delete mail: " + id, exception);
        }
    }

    public void insert() {
        try (final @NotNull PreparedStatement stmt = CloudnodeMSG.getInstance().db().getConnection().prepareStatement("INSERT INTO `cloudnodemsg_mail` (`id`, `sender`, `recipient`, `message`, `seen`, `starred`) VALUES (?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, id.toString());
            stmt.setString(2, sender.getUniqueId().toString());
            stmt.setString(3, recipient.getUniqueId().toString());
            stmt.setString(4, message);
            stmt.setBoolean(5, seen);
            stmt.setBoolean(6, starred);
            stmt.executeUpdate();
        }
        catch (final @NotNull SQLException exception) {
            CloudnodeMSG.getInstance().getLogger().log(Level.SEVERE, "Could not insert mail: " + id, exception);
        }
    }

    public static @NotNull Optional<@NotNull Mail> get(final @NotNull UUID id) {
        try (final @NotNull PreparedStatement stmt = CloudnodeMSG.getInstance().db().getConnection().prepareStatement("SELECT * FROM `cloudnodemsg_mail` WHERE `id` = ? LIMIT 1")) {
            stmt.setString(1, id.toString());
            final @NotNull ResultSet rs = stmt.executeQuery();
            if (!rs.next()) return Optional.empty();
            return Optional.of(new Mail(rs));
        }
        catch (final @NotNull SQLException exception) {
            CloudnodeMSG.getInstance().getLogger().log(Level.SEVERE, "Could not get mail: " + id, exception);
            return Optional.empty();
        }
    }

    public static int unread(final @NotNull OfflinePlayer player) {
        try (final @NotNull PreparedStatement stmt = CloudnodeMSG.getInstance().db().getConnection().prepareStatement("SELECT COUNT(`id`) as `n` FROM `cloudnodemsg_mail` WHERE `recipient` = ? AND `seen` = 0")) {
            stmt.setString(1, player.getUniqueId().toString());
            final @NotNull ResultSet rs = stmt.executeQuery();
            if (!rs.next()) return 0;
            return rs.getInt("n");
        }
        catch (final @NotNull SQLException exception) {
            CloudnodeMSG.getInstance().getLogger().log(Level.SEVERE, "Could not get unread mails: " + player.getName(), exception);
            return 0;
        }
    }
}
