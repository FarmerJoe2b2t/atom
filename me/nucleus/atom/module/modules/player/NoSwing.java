// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.player;

import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketAnimation;
import me.zero.alpine.listener.EventHandler;
import me.nucleus.atom.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.nucleus.atom.module.Module;

@Info(name = "NoSwing", category = Category.PLAYER, description = "Cancels server and client swinging packets")
public class NoSwing extends Module
{
    @EventHandler
    private Listener<PacketEvent.Send> listener;
    
    public NoSwing() {
        this.listener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketAnimation) {
                event.cancel();
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
}
