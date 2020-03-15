// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.combat;

import net.minecraft.entity.Entity;
import java.util.function.Predicate;
import me.nucleus.atom.module.ModuleManager;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketEntityStatus;
import me.zero.alpine.listener.EventHandler;
import me.nucleus.atom.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.nucleus.atom.module.Module;

@Info(name = "AntiChainPop", category = Category.COMBAT)
public class AntiChainPop extends Module
{
    @EventHandler
    private Listener<PacketEvent.Receive> damageListener;
    
    public AntiChainPop() {
        SPacketEntityStatus packet;
        Entity entity;
        Surround surround;
        this.damageListener = new Listener<PacketEvent.Receive>(event -> {
            if (AntiChainPop.mc.field_71439_g != null && AntiChainPop.mc.field_71441_e != null) {
                if (event.getPacket() instanceof SPacketEntityStatus) {
                    packet = (SPacketEntityStatus)event.getPacket();
                    if (packet.func_149160_c() == 35) {
                        entity = packet.func_149161_a((World)AntiChainPop.mc.field_71441_e);
                        if (entity.func_70005_c_().equals(AntiChainPop.mc.field_71439_g.func_70005_c_())) {
                            surround = (Surround)ModuleManager.getModuleByName("Surround");
                            if (!surround.isEnabled()) {
                                surround.enable();
                            }
                        }
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
}
