package pro.cloudnode.smp.cloudnodemsg.error;

import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

public final class IgnoreYourselfError extends Error {
    public IgnoreYourselfError() {
        super(CloudnodeMSG.getInstance().config().ignoreYourself());
    }
}
