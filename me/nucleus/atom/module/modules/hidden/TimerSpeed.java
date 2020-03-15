// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.hidden;

import me.nucleus.atom.util.InfoCalculator;
import me.nucleus.atom.setting.builder.SettingBuilder;
import me.nucleus.atom.setting.Settings;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "TimerSpeed", description = "Automatically change Timer Speed", category = Category.HIDDEN)
public class TimerSpeed extends Module
{
    private float tickDelay;
    private static float curSpeed;
    private Setting<Float> minimumSpeed;
    private Setting<Float> maxSpeed;
    private Setting<Float> attemptSpeed;
    private Setting<Float> fastSpeed;
    
    public TimerSpeed() {
        this.tickDelay = 0.0f;
        this.minimumSpeed = this.register(Settings.floatBuilder("Minimum Speed").withMinimum(0.0f).withMaximum(10.0f).withValue(4.0f));
        this.maxSpeed = this.register(Settings.floatBuilder("Max Speed").withMinimum(0.0f).withMaximum(10.0f).withValue(7.0f));
        this.attemptSpeed = this.register(Settings.floatBuilder("Attempt Speed").withMinimum(1.0f).withMaximum(10.0f).withValue(4.2f));
        this.fastSpeed = this.register(Settings.floatBuilder("Fast Speed").withMinimum(1.0f).withMaximum(10.0f).withValue(5.0f));
    }
    
    public static String returnGui() {
        return "" + InfoCalculator.round(TimerSpeed.curSpeed, 2);
    }
    
    @Override
    public void onUpdate() {
        if (this.tickDelay == this.minimumSpeed.getValue()) {
            TimerSpeed.curSpeed = this.fastSpeed.getValue();
            TimerSpeed.mc.field_71428_T.field_194149_e = 50.0f / this.fastSpeed.getValue();
        }
        if (this.tickDelay >= this.maxSpeed.getValue()) {
            this.tickDelay = 0.0f;
            TimerSpeed.curSpeed = this.attemptSpeed.getValue();
            TimerSpeed.mc.field_71428_T.field_194149_e = 50.0f / this.attemptSpeed.getValue();
        }
        ++this.tickDelay;
    }
    
    public void onDisable() {
        TimerSpeed.mc.field_71428_T.field_194149_e = 50.0f;
    }
    
    static {
        TimerSpeed.curSpeed = 0.0f;
    }
}
