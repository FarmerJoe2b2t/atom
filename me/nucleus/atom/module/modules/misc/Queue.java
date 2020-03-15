// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.misc;

import net.minecraft.client.Minecraft;
import java.util.function.Predicate;
import me.nucleus.atom.module.ModuleManager;
import net.minecraft.network.play.server.SPacketChat;
import me.zero.alpine.listener.EventHandler;
import me.nucleus.atom.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.nucleus.atom.module.Module;

@Info(name = "Queue", category = Category.MISC)
public class Queue extends Module
{
    private boolean queue;
    private int queuePosition;
    private int lastQueuePosition;
    @EventHandler
    private Listener<PacketEvent.Receive> listener;
    
    public Queue() {
        this.lastQueuePosition = -1;
        SPacketChat sPacketChat;
        String msg;
        String prefix;
        String[] s;
        TelegramNotifications telegram;
        int step;
        this.listener = new Listener<PacketEvent.Receive>(event -> {
            if (Queue.mc.field_71439_g != null && this.queue) {
                if (!(!(event.getPacket() instanceof SPacketChat))) {
                    sPacketChat = (SPacketChat)event.getPacket();
                    msg = sPacketChat.func_148915_c().func_150260_c();
                    prefix = "Position in queue: ";
                    if (Chat.INSTANCE.addTimestamp.getValue() && msg.startsWith("[")) {
                        s = msg.split(" ");
                        msg = msg.substring(s[0].length());
                    }
                    if (!(!msg.startsWith(prefix))) {
                        this.queuePosition = Integer.parseInt(msg.split(" ")[3]);
                        if (this.lastQueuePosition == -1) {
                            this.lastQueuePosition = this.queuePosition;
                        }
                        telegram = (TelegramNotifications)ModuleManager.getModuleByNameUnsafe("TelegramNotifications");
                        if (telegram != null && telegram.isEnabled() && telegram.queue.getValue()) {
                            step = telegram.queueStep.getValue();
                            if (this.lastQueuePosition - this.queuePosition >= step) {
                                telegram.notify("Position in queue: " + this.queuePosition);
                                this.lastQueuePosition = this.queuePosition;
                            }
                        }
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    public boolean inQueue() {
        return this.queue;
    }
    
    public int getQueuePosition() {
        return this.queuePosition;
    }
    
    @Override
    public void onUpdate() {
        if (Minecraft.func_71410_x().func_147104_D() == null) {
            this.queue = false;
            return;
        }
        if (!Minecraft.func_71410_x().func_147104_D().field_78845_b.equalsIgnoreCase("2b2t.org")) {
            this.queue = false;
            return;
        }
        if (Queue.mc.field_71439_g.field_71093_bK != 1) {
            this.queue = false;
            return;
        }
        if (!this.queue) {
            this.queue = true;
            this.queuePosition = -1;
            this.lastQueuePosition = -1;
        }
    }
}
