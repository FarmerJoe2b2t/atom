// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.combat;

import java.util.function.Predicate;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.nucleus.atom.command.Command;
import java.util.Random;
import net.minecraft.client.gui.GuiGameOver;
import me.nucleus.atom.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.nucleus.atom.event.events.GuiScreenEvent;
import me.zero.alpine.listener.Listener;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "AutoExcuse", category = Category.COMBAT)
public class AutoExcuse extends Module
{
    private Setting<Boolean> clientSideOnly;
    private boolean died;
    @EventHandler
    public Listener<GuiScreenEvent.Displayed> listener;
    
    public AutoExcuse() {
        this.clientSideOnly = this.register(Settings.b("ClientSideOnly", true));
        this.died = false;
        String[] excuses;
        String excuse;
        this.listener = new Listener<GuiScreenEvent.Displayed>(event -> {
            if (AutoExcuse.mc.field_71439_g != null) {
                if (!(!(event.getScreen() instanceof GuiGameOver))) {
                    if (!this.died) {
                        excuses = new String[] { "lag", "shit anticheat", "fps drop", "i lagged", "ghostblock", "i tabbed out", "was configuring my settings" };
                        excuse = excuses[new Random().nextInt(excuses.length)];
                        if (this.clientSideOnly.getValue()) {
                            Command.sendChatMessage("[AutoExcuse] " + excuse);
                        }
                        else {
                            AutoExcuse.mc.field_71442_b.field_78774_b.func_147297_a((Packet)new CPacketChatMessage(excuse));
                        }
                        this.died = true;
                    }
                }
            }
        }, (Predicate<GuiScreenEvent.Displayed>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (AutoExcuse.mc.field_71439_g == null) {
            return;
        }
        if (AutoExcuse.mc.field_71439_g.func_110143_aJ() > 0.0f) {
            this.died = false;
        }
    }
}
