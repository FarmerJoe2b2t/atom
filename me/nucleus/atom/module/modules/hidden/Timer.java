// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.hidden;

import me.nucleus.atom.setting.builder.SettingBuilder;
import me.nucleus.atom.setting.Settings;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "Timer", category = Category.HIDDEN, description = "Changes your client tick speed")
public class Timer extends Module
{
    private Setting<Float> speed;
    
    public Timer() {
        this.speed = this.register(Settings.floatBuilder("Speed").withMinimum(0.0f).withMaximum(10.0f).withValue(2.0f));
    }
    
    public void onDisable() {
        Timer.mc.field_71428_T.field_194149_e = 50.0f;
    }
    
    @Override
    public void onUpdate() {
        Timer.mc.field_71428_T.field_194149_e = 50.0f / this.speed.getValue();
    }
}
