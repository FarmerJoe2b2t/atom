// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.combat;

import java.util.function.Predicate;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiGameOver;
import me.zero.alpine.listener.EventHandler;
import me.nucleus.atom.event.events.GuiScreenEvent;
import me.zero.alpine.listener.Listener;
import me.nucleus.atom.module.Module;

@Info(name = "AntiDeathScreen", description = "Fixes random death screen glitches", category = Category.COMBAT)
public class AntiDeathScreen extends Module
{
    @EventHandler
    public Listener<GuiScreenEvent.Displayed> listener;
    
    public AntiDeathScreen() {
        this.listener = new Listener<GuiScreenEvent.Displayed>(event -> {
            if (!(!(event.getScreen() instanceof GuiGameOver))) {
                if (AntiDeathScreen.mc.field_71439_g.func_110143_aJ() > 0.0f) {
                    AntiDeathScreen.mc.field_71439_g.func_71004_bE();
                    AntiDeathScreen.mc.func_147108_a((GuiScreen)null);
                }
            }
        }, (Predicate<GuiScreenEvent.Displayed>[])new Predicate[0]);
    }
}
