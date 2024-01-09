package pro.cloudnode.smp.cloudnodemsg.error;

import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

public final class MessageYourselfError extends Error {
    public MessageYourselfError() {
        super(CloudnodeMSG.getInstance().config().messageYourself());
    }
}
