// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.hidden;

import me.nucleus.atom.command.Command;
import net.minecraft.client.Minecraft;
import java.util.function.Predicate;
import net.minecraft.network.Packet;
import me.nucleus.atom.util.Wrapper;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.zero.alpine.listener.EventHandler;
import me.nucleus.atom.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.nucleus.atom.module.Module;

@Info(name = "FormatChat", description = "Add colour and linebreak support to upstream chat packets", category = Category.HIDDEN)
public class FormatChat extends Module
{
    @EventHandler
    public Listener<PacketEvent.Send> sendListener;
    
    public FormatChat() {
        String message;
        String message2;
        String message3;
        this.sendListener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketChatMessage) {
                message = ((CPacketChatMessage)event.getPacket()).field_149440_a;
                if (message.contains("&") || message.contains("#n")) {
                    message2 = message.replaceAll("&", "§");
                    message3 = message2.replaceAll("#n", "\n");
                    Wrapper.getPlayer().field_71174_a.func_147297_a((Packet)new CPacketChatMessage(message3));
                    event.cancel();
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    public void onEnable() {
        if (Minecraft.func_71410_x().func_147104_D() == null) {
            Command.sendWarningMessage("[FormatChat] &6&lWarning: &r&6This does not work in singleplayer");
            this.disable();
        }
        else {
            Command.sendWarningMessage("[FormatChat] &6&lWarning: &r&6This will kick you on most servers!");
        }
    }
}
