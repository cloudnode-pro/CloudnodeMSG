package pro.cloudnode.smp.cloudnodemsg.error;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import pro.cloudnode.smp.cloudnodemsg.CloudnodeMSG;

import java.util.logging.Level;

public abstract class Error extends Throwable {
    public final @NotNull Component component;

    protected Error(@NotNull Component component) {
        this.component = component;
    }

    public boolean send(final @NotNull Audience recipient) {
        recipient.sendMessage(component);
        return true;
    }

    public @NotNull Error log() {
        CloudnodeMSG.getInstance().getLogger().log(Level.SEVERE, MiniMessage.miniMessage().serialize(component), this);
        return this;
    }
}
