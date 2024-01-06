package pro.cloudnode.smp.cloudnodemsg.error;

import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

/**
 * Player has no username (somehow)
 */
public final class MessageYourselfError extends Error {
    public MessageYourselfError() {
        super(CloudnodeMSG.getInstance().config().messageYourself());
    }
}
