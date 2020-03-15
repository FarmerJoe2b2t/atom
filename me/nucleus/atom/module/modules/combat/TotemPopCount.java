// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.combat;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import me.nucleus.atom.command.Command;
import net.minecraft.entity.Entity;
import java.util.function.Predicate;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketEntityStatus;
import me.nucleus.atom.setting.Settings;
import java.util.HashMap;
import me.zero.alpine.listener.EventHandler;
import me.nucleus.atom.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.nucleus.atom.setting.Setting;
import java.util.Map;
import me.nucleus.atom.module.Module;

@Info(name = "TotemPopCount", category = Category.COMBAT)
public class TotemPopCount extends Module
{
    public Map<String, Integer> popList;
    private Setting<Boolean> chatMessages;
    private Setting<Boolean> ignoreSelf;
    @EventHandler
    private Listener<PacketEvent.Receive> damageListener;
    
    public TotemPopCount() {
        this.popList = new HashMap<String, Integer>();
        this.chatMessages = this.register(Settings.b("Chat Messages", true));
        this.ignoreSelf = this.register(Settings.b("Ignore Self", true));
        SPacketEntityStatus packet;
        Entity entity;
        this.damageListener = new Listener<PacketEvent.Receive>(event -> {
            if (TotemPopCount.mc.field_71439_g != null && TotemPopCount.mc.field_71441_e != null) {
                if (event.getPacket() instanceof SPacketEntityStatus) {
                    packet = (SPacketEntityStatus)event.getPacket();
                    if (packet.func_149160_c() == 35) {
                        entity = packet.func_149161_a((World)TotemPopCount.mc.field_71441_e);
                        this.popTotem(entity);
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    private void popTotem(final Entity entity) {
        final String name = entity.func_70005_c_();
        int count = 1;
        if (this.popList.get(name) == null) {
            this.popList.put(name, 1);
        }
        else {
            count = this.popList.get(name) + 1;
            this.popList.put(name, count);
        }
        if (entity.func_70005_c_().equals(TotemPopCount.mc.field_71439_g.func_70005_c_()) && this.ignoreSelf.getValue()) {
            return;
        }
        if (this.chatMessages.getValue()) {
            Command.sendChatMessagePermanent("&d" + name + " popped " + count + " totems!");
        }
    }
    
    @Override
    public void onUpdate() {
        if (TotemPopCount.mc.field_71441_e == null) {
            return;
        }
        for (final EntityPlayer player : TotemPopCount.mc.field_71441_e.field_73010_i) {
            if (player.func_110143_aJ() <= 0.0f) {
                if (!this.popList.containsKey(player.func_70005_c_())) {
                    continue;
                }
                if ((!player.func_70005_c_().equals(TotemPopCount.mc.field_71439_g.func_70005_c_()) || !this.ignoreSelf.getValue()) && this.chatMessages.getValue()) {
                    Command.sendChatMessagePermanent("&d" + player.func_70005_c_() + " died after popping " + this.popList.get(player.func_70005_c_()) + " totems!");
                }
                this.popList.remove(player.func_70005_c_());
            }
        }
    }
}
