package pro.cloudnode.smp.cloudnodemsg.error;

import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

/**
 * Only players can use this command
 */
public final class NotInTeamError extends Error {
    /**
     * Only players can use this command
     */
    public NotInTeamError() {
        super(CloudnodeMSG.getInstance().config().notInTeam());
    }
}
