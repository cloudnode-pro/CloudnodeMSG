package pro.cloudnode.smp.cloudnodemsg.error;

import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

/**
 * Player has no username (somehow)
 */
public final class NobodyReplyError extends Error {
    public NobodyReplyError() {
        super(CloudnodeMSG.getInstance().config().nobodyReply());
    }
}
