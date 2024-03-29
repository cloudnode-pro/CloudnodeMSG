package pro.cloudnode.smp.cloudnodemsg.error;

import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

/**
 * Only players can use this command
 */
public final class NotPlayerError extends Error {
    /**
     * Only players can use this command
     */
    public NotPlayerError() {
        super(CloudnodeMSG.getInstance().config().notPlayer());
    }
}
