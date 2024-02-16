package pro.cloudnode.smp.cloudnodemsg.error;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

public class PlayerHasIncomingDisabledError extends Error {
    public PlayerHasIncomingDisabledError(final @NotNull OfflinePlayer player) {
        super(CloudnodeMSG.getInstance().config().incomingDisabled(player));
    }
}
