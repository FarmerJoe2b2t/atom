// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.player;

import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import me.zero.alpine.listener.EventHandler;
import me.nucleus.atom.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.nucleus.atom.module.Module;

@Info(name = "PortalGodMode", category = Category.PLAYER, description = "Don't take damage in portals")
public class PortalGodMode extends Module
{
    @EventHandler
    public Listener<PacketEvent.Send> listener;
    
    public PortalGodMode() {
        this.listener = new Listener<PacketEvent.Send>(event -> {
            if (this.isEnabled() && event.getPacket() instanceof CPacketConfirmTeleport) {
                event.cancel();
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
}
