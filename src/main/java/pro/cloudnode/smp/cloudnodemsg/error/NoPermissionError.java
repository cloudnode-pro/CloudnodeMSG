package pro.cloudnode.smp.cloudnodemsg.error;

import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

/**
 * No permission
 */
public final class NoPermissionError extends Error {
    /**
     * No permission
     */
    public NoPermissionError() {
        super(CloudnodeMSG.getInstance().config().noPermission());
    }
}
