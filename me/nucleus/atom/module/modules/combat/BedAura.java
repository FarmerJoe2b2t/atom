// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.combat;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumFacing;
import java.util.Comparator;
import net.minecraft.tileentity.TileEntityBed;
import me.nucleus.atom.setting.Settings;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "BedAura", category = Category.COMBAT)
public class BedAura extends Module
{
    private Setting<Double> range;
    private Setting<Double> waitTick;
    private int waitCounter;
    
    public BedAura() {
        this.range = this.register(Settings.d("Hit Range", 5.0));
        this.waitTick = this.register(Settings.d("Tick Delay", 1.0));
    }
    
    @Override
    public void onUpdate() {
        if (BedAura.mc.field_71439_g == null) {
            return;
        }
        if (BedAura.mc.field_71439_g.field_71093_bK == 0) {
            return;
        }
        if (this.waitTick.getValue() > 0.0) {
            if (this.waitCounter < this.waitTick.getValue()) {
                ++this.waitCounter;
                return;
            }
            this.waitCounter = 0;
        }
        final TileEntityBed bed = (TileEntityBed)BedAura.mc.field_71441_e.field_147482_g.stream().filter(e -> e instanceof TileEntityBed).filter(e -> BedAura.mc.field_71439_g.func_180425_c().func_185332_f(e.func_174877_v().field_177962_a, e.func_174877_v().field_177960_b, e.func_174877_v().field_177961_c) <= this.range.getValue()).map(entity -> entity).min(Comparator.comparing(e -> BedAura.mc.field_71439_g.func_180425_c().func_185332_f(e.func_174877_v().field_177962_a, e.func_174877_v().field_177960_b, e.func_174877_v().field_177961_c))).orElse(null);
        if (bed != null) {
            BedAura.mc.field_71442_b.func_187099_a(BedAura.mc.field_71439_g, BedAura.mc.field_71441_e, bed.func_174877_v(), EnumFacing.UP, new Vec3d((double)bed.func_174877_v().func_177958_n(), (double)bed.func_174877_v().func_177956_o(), (double)bed.func_174877_v().func_177952_p()), EnumHand.MAIN_HAND);
        }
    }
}
