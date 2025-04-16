package pro.cloudnode.smp.cloudnodemsg.error;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

/**
 * Player cannot be ignored
 */
public final class CannotIgnoreError extends Error {
    public CannotIgnoreError(final @NotNull OfflinePlayer player) {
        super(CloudnodeMSG.getInstance().config().cannotIgnore(player));
    }
}
