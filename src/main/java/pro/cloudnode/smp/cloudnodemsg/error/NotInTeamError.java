package pro.cloudnode.smp.cloudnodemsg.error;

import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

/**
 * You are not in a team
 */
public final class NotInTeamError extends Error {
    /**
     * You are not in a team
     */
    public NotInTeamError() {
        super(CloudnodeMSG.getInstance().config().notInTeam());
    }
}
