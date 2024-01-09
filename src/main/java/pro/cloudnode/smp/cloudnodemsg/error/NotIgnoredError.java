package pro.cloudnode.smp.cloudnodemsg.error;

import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

/**
 * That player is not ignored
 */
public final class NotIgnoredError extends Error {
    public NotIgnoredError(final @NotNull String player) {
        super(CloudnodeMSG.getInstance().config().notIgnored(player));
    }
}
