package pro.cloudnode.smp.cloudnodemsg.error;

import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

/**
 * Player cannot be ignored
 */
public final class CannotIgnoredError extends Error {
    public CannotIgnoredError(final @NotNull String player) {
        super(CloudnodeMSG.getInstance().config().cannotIgnore(player));
    }
}
