package pro.cloudnode.smp.cloudnodemsg.error;

import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

/**
 * Target player has never joined the server
 */
public final class NeverJoinedError extends Error {
    public NeverJoinedError(final @NotNull String player) {
        super(CloudnodeMSG.getInstance().config().neverJoined(player));
    }
}
