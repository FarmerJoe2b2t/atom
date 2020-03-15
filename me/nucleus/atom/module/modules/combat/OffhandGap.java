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
import me.nucleus.atom.module.ModuleManager;
import me.nucleus.atom.command.Command;
import me.nucleus.atom.setting.Settings;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "OffhandGap", category = Category.COMBAT)
public class OffhandGap extends Module
{
    int totems;
    boolean moving;
    boolean returnI;
    private Setting<Boolean> force;
    private Setting<Boolean> inv;
    private Setting<Double> minHealth;
    private boolean disabledAutoTotem;
    
    public OffhandGap() {
        this.moving = false;
        this.returnI = false;
        this.force = this.register(Settings.b("Replace Offhand", true));
        this.inv = this.register(Settings.b("Inventory", true));
        this.minHealth = this.register(Settings.d("MinHealth", 8.0));
    }
    
    public void onEnable() {
        this.disabledAutoTotem = false;
        if (OffhandGap.mc.field_71439_g.func_110143_aJ() < this.minHealth.getValue()) {
            Command.sendChatMessage("Not enabling OffhandGap due to insufficient health");
            this.disable();
            return;
        }
        final Module m = ModuleManager.getModuleByName("AutoTotem");
        if (m.isEnabled()) {
            this.disabledAutoTotem = true;
            m.disable();
        }
    }
    
    public void onDisable() {
        if (this.disabledAutoTotem) {
            final Module m = ModuleManager.getModuleByName("AutoTotem");
            m.disable();
            if (m.isDisabled()) {
                m.enable();
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (!this.inv.getValue() && OffhandGap.mc.field_71462_r instanceof GuiContainer) {
            return;
        }
        if (OffhandGap.mc.field_71439_g.func_110143_aJ() < this.minHealth.getValue()) {
            Command.sendChatMessage("OffhandGap disabled due to insufficient health");
            this.disable();
        }
        if (this.returnI) {
            int t = -1;
            for (int i = 0; i < 45; ++i) {
                if (OffhandGap.mc.field_71439_g.field_71071_by.func_70301_a(i).field_190928_g) {
                    t = i;
                    break;
                }
            }
            if (t == -1) {
                return;
            }
            OffhandGap.mc.field_71442_b.func_187098_a(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.field_71439_g);
            this.returnI = false;
        }
        this.totems = OffhandGap.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_151153_ao).mapToInt(ItemStack::func_190916_E).sum();
        if (OffhandGap.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao) {
            ++this.totems;
        }
        else {
            if (!this.force.getValue() && !OffhandGap.mc.field_71439_g.func_184592_cb().field_190928_g) {
                return;
            }
            if (this.moving) {
                OffhandGap.mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.field_71439_g);
                this.moving = false;
                if (!OffhandGap.mc.field_71439_g.field_71071_by.field_70457_g.func_190926_b()) {
                    this.returnI = true;
                }
                return;
            }
            if (OffhandGap.mc.field_71439_g.field_71071_by.field_70457_g.func_190926_b()) {
                if (this.totems == 0) {
                    return;
                }
                int t = -1;
                for (int i = 0; i < 45; ++i) {
                    if (OffhandGap.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == Items.field_151153_ao) {
                        t = i;
                        break;
                    }
                }
                if (t == -1) {
                    return;
                }
                OffhandGap.mc.field_71442_b.func_187098_a(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.field_71439_g);
                this.moving = true;
            }
            else if (this.force.getValue()) {
                int t = -1;
                for (int i = 0; i < 45; ++i) {
                    if (OffhandGap.mc.field_71439_g.field_71071_by.func_70301_a(i).field_190928_g) {
                        t = i;
                        break;
                    }
                }
                if (t == -1) {
                    return;
                }
                OffhandGap.mc.field_71442_b.func_187098_a(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.field_71439_g);
            }
        }
    }
    
    @Override
    public String getHudInfo() {
        return String.valueOf(this.totems);
    }
}
