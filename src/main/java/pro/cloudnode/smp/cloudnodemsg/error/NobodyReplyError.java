package pro.cloudnode.smp.cloudnodemsg.error;

import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

public final class NobodyReplyError extends Error {
    public NobodyReplyError() {
        super(CloudnodeMSG.getInstance().config().nobodyReply());
    }
}
