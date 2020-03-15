// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.misc;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.nucleus.atom.setting.Settings;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "ElytraCount", category = Category.MISC, description = "Counts the amount of usable Elytras in your inventory")
public class ElytraCount extends Module
{
    private int elytras;
    private Setting<Integer> minDurability;
    
    public ElytraCount() {
        this.elytras = 0;
        this.minDurability = this.register((Setting<Integer>)Settings.integerBuilder("Minimum Durability").withMinimum(0).withValue(10).withMaximum(432).build());
    }
    
    @Override
    public void onUpdate() {
        this.elytras = 0;
        for (int i = 0; i < 45; ++i) {
            final ItemStack stack = ElytraCount.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack != ItemStack.field_190927_a) {
                if (stack.func_77973_b() == Items.field_185160_cR && stack.func_77952_i() < 432 - this.minDurability.getValue()) {
                    ++this.elytras;
                }
            }
        }
    }
    
    @Override
    public String getHudInfo() {
        return String.valueOf(this.elytras);
    }
}
