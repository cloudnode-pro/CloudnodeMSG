package pro.cloudnode.smp.cloudnodemsg.error;

import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

/**
 * Player has no username (somehow)
 */
public final class InvalidPlayerError extends Error {
    /**
     * Player has no username (somehow)
     */
    public InvalidPlayerError() {
        super(CloudnodeMSG.getInstance().config().invalidPlayer());
    }
}
