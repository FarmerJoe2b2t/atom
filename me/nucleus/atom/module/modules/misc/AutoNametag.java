// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.misc;

import java.util.Iterator;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.Entity;
import me.nucleus.atom.command.Command;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import me.nucleus.atom.setting.Settings;
import net.minecraft.entity.boss.EntityWither;
import java.util.ArrayList;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "AutoNametag", category = Category.MISC)
public class AutoNametag extends Module
{
    private Setting<Double> range;
    private ArrayList<EntityWither> namedWithers;
    
    public AutoNametag() {
        this.range = this.register(Settings.d("Range", 4.0));
        this.namedWithers = new ArrayList<EntityWither>();
    }
    
    public void onEnable() {
        this.namedWithers.clear();
    }
    
    @Override
    public void onUpdate() {
        int tagslot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoNametag.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack != ItemStack.field_190927_a) {
                if (!(stack.func_77973_b() instanceof ItemBlock)) {
                    final Item tag = stack.func_77973_b();
                    if (tag instanceof ItemNameTag) {
                        tagslot = i;
                    }
                }
            }
        }
        if (tagslot == -1) {
            Command.sendChatMessage("[AutoNametag] Error: No nametags in inventory, disabling module");
            this.disable();
            return;
        }
        for (final Entity w : AutoNametag.mc.field_71441_e.func_72910_y()) {
            if (w instanceof EntityWither) {
                final EntityWither wither = (EntityWither)w;
                if (this.namedWithers.contains(wither)) {
                    continue;
                }
                if (AutoNametag.mc.field_71439_g.func_70032_d((Entity)wither) > this.range.getValue() || tagslot == -1) {
                    continue;
                }
                AutoNametag.mc.field_71439_g.field_71071_by.field_70461_c = tagslot;
                AutoNametag.mc.field_71442_b.func_187097_a((EntityPlayer)AutoNametag.mc.field_71439_g, (Entity)wither, EnumHand.MAIN_HAND);
                this.namedWithers.add(wither);
            }
        }
    }
}
