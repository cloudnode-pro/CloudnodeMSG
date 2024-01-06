package pro.cloudnode.smp.cloudnodemsg.error;

import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

/**
 * Player has no username (somehow)
 */
public final class NoPermissionError extends Error {
    public NoPermissionError() {
        super(CloudnodeMSG.getInstance().config().noPermission());
    }
}
