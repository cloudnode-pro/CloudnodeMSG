package pro.cloudnode.smp.cloudnodemsg.error;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

/**
 * Channel recipient not found (the channel should be closed after this error)
 */
public final class ChannelOfflineError extends Error {
    /**
     * Channel recipient not found (the channel should be closed after this error)
     *
     * @param sender    The username of the message sender
     * @param recipient The username of the message recipient
     */
    public ChannelOfflineError(final @NotNull OfflinePlayer sender, final @NotNull OfflinePlayer recipient) {
        super(CloudnodeMSG.getInstance().config().channelOffline(sender, recipient));
    }
}
