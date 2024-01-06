package pro.cloudnode.smp.cloudnodemsg.error;

import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

/**
 * Player has no username (somehow)
 */
public final class PlayerNotFoundError extends Error {
    public PlayerNotFoundError(final @NotNull String player) {
        super(CloudnodeMSG.getInstance().config().playerNotFound(player));
    }
}
