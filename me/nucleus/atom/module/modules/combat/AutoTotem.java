// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.combat;

import java.util.function.ToIntFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.gui.inventory.GuiContainer;
import me.nucleus.atom.setting.Settings;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "AutoTotem", category = Category.COMBAT)
public class AutoTotem extends Module
{
    private Setting<Mode> modeSetting;
    int totems;
    boolean moving;
    boolean returnI;
    
    public AutoTotem() {
        this.modeSetting = this.register(Settings.e("Mode", Mode.REPLACE_OFFHAND));
        this.moving = false;
        this.returnI = false;
    }
    
    @Override
    public void onUpdate() {
        if (!this.modeSetting.getValue().equals(Mode.INVENTORY) && AutoTotem.mc.field_71462_r instanceof GuiContainer) {
            return;
        }
        if (this.returnI) {
            int t = -1;
            for (int i = 0; i < 45; ++i) {
                if (AutoTotem.mc.field_71439_g.field_71071_by.func_70301_a(i).field_190928_g) {
                    t = i;
                    break;
                }
            }
            if (t == -1) {
                return;
            }
            AutoTotem.mc.field_71442_b.func_187098_a(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.field_71439_g);
            this.returnI = false;
        }
        this.totems = AutoTotem.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum();
        if (AutoTotem.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY) {
            ++this.totems;
        }
        else {
            if (!this.modeSetting.getValue().equals(Mode.REPLACE_OFFHAND) && !AutoTotem.mc.field_71439_g.func_184592_cb().field_190928_g) {
                return;
            }
            if (this.moving) {
                AutoTotem.mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.field_71439_g);
                this.moving = false;
                if (!AutoTotem.mc.field_71439_g.field_71071_by.field_70457_g.func_190926_b()) {
                    this.returnI = true;
                }
                return;
            }
            if (AutoTotem.mc.field_71439_g.field_71071_by.field_70457_g.func_190926_b()) {
                if (this.totems == 0) {
                    return;
                }
                int t = -1;
                for (int i = 0; i < 45; ++i) {
                    if (AutoTotem.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == Items.field_190929_cY) {
                        t = i;
                        break;
                    }
                }
                if (t == -1) {
                    return;
                }
                AutoTotem.mc.field_71442_b.func_187098_a(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.field_71439_g);
                this.moving = true;
            }
            else if (this.modeSetting.getValue().equals(Mode.REPLACE_OFFHAND)) {
                int t = -1;
                for (int i = 0; i < 45; ++i) {
                    if (AutoTotem.mc.field_71439_g.field_71071_by.func_70301_a(i).field_190928_g) {
                        t = i;
                        break;
                    }
                }
                if (t == -1) {
                    return;
                }
                AutoTotem.mc.field_71442_b.func_187098_a(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.field_71439_g);
            }
        }
    }
    
    @Override
    public String getHudInfo() {
        return String.valueOf(this.totems);
    }
    
    private enum Mode
    {
        NEITHER, 
        REPLACE_OFFHAND, 
        INVENTORY;
    }
}
