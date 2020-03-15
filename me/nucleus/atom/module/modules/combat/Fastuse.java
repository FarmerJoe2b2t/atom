// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.combat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;
import me.nucleus.atom.setting.Settings;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(category = Category.COMBAT, description = "Changes delay when holding right click", name = "FastUse")
public class Fastuse extends Module
{
    private Setting<Integer> delay;
    private Setting<Mode> mode;
    private static long time;
    
    public Fastuse() {
        this.delay = this.register((Setting<Integer>)Settings.integerBuilder("Delay").withMinimum(0).withMaximum(20).withValue(0).build());
        this.mode = this.register(Settings.e("Mode", Mode.BOTH));
    }
    
    public void onDisable() {
        Fastuse.mc.field_71467_ac = 4;
    }
    
    @Override
    public void onUpdate() {
        if (this.delay.getValue() > 0) {
            if (Fastuse.time > 0L) {
                --Fastuse.time;
                Fastuse.mc.field_71467_ac = 1;
                return;
            }
            Fastuse.time = Math.round((float)(2 * Math.round(this.delay.getValue() / 2.0f)));
        }
        if (Fastuse.mc.field_71439_g == null) {
            return;
        }
        final Item itemMain = Fastuse.mc.field_71439_g.func_184614_ca().func_77973_b();
        final Item itemOff = Fastuse.mc.field_71439_g.func_184592_cb().func_77973_b();
        final boolean doExpMain = itemMain instanceof ItemExpBottle;
        final boolean doExpOff = itemOff instanceof ItemExpBottle;
        final boolean doCrystalMain = itemMain instanceof ItemEndCrystal;
        final boolean doCrystalOff = itemOff instanceof ItemEndCrystal;
        switch (this.mode.getValue()) {
            case ALL: {
                Fastuse.mc.field_71467_ac = 0;
            }
            case BOTH: {
                if (doExpMain || doExpOff || doCrystalMain || doCrystalOff) {
                    Fastuse.mc.field_71467_ac = 0;
                }
            }
            case EXP: {
                if (doExpMain || doExpOff) {
                    Fastuse.mc.field_71467_ac = 0;
                }
            }
            case CRYSTAL: {
                if (doCrystalMain || doCrystalOff) {
                    Fastuse.mc.field_71467_ac = 0;
                    break;
                }
                break;
            }
        }
    }
    
    static {
        Fastuse.time = 0L;
    }
    
    private enum Mode
    {
        ALL, 
        BOTH, 
        EXP, 
        CRYSTAL;
    }
}
