// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import me.nucleus.atom.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Iterator;
import java.util.function.Consumer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.block.material.Material;
import java.util.Arrays;
import net.minecraft.init.Blocks;
import me.nucleus.atom.setting.Settings;
import net.minecraft.block.Block;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "HoleFill", category = Category.COMBAT)
public class HoleFill extends Module
{
    private Setting<Double> range;
    private Setting<Double> yRange;
    private Setting<Boolean> rotate;
    private Setting<Double> waitTick;
    private Setting<Boolean> useEC;
    private Setting<Boolean> chat;
    private ArrayList<BlockPos> holes;
    private List<Block> whiteList;
    BlockPos pos;
    private int waitCounter;
    
    public HoleFill() {
        this.range = this.register(Settings.d("Range", 5.0));
        this.yRange = this.register(Settings.d("YRange", 2.0));
        this.rotate = this.register(Settings.b("Rotate", true));
        this.waitTick = this.register(Settings.d("TickDelay", 1.0));
        this.useEC = this.register(Settings.b("UseEnderchests", false));
        this.chat = this.register(Settings.b("Chat", false));
        this.holes = new ArrayList<BlockPos>();
        this.whiteList = Arrays.asList(Blocks.field_150343_Z);
    }
    
    @Override
    public void onUpdate() {
        this.holes = new ArrayList<BlockPos>();
        if (this.useEC.getValue()) {
            if (!this.whiteList.contains(Blocks.field_150477_bB)) {
                this.whiteList.add(Blocks.field_150477_bB);
            }
        }
        else if (this.whiteList.contains(Blocks.field_150477_bB)) {
            this.whiteList.remove(Blocks.field_150477_bB);
        }
        final Iterable<BlockPos> blocks = (Iterable<BlockPos>)BlockPos.func_177980_a(HoleFill.mc.field_71439_g.func_180425_c().func_177963_a(-this.range.getValue(), -this.yRange.getValue(), -this.range.getValue()), HoleFill.mc.field_71439_g.func_180425_c().func_177963_a((double)this.range.getValue(), (double)this.yRange.getValue(), (double)this.range.getValue()));
        for (final BlockPos pos : blocks) {
            if (!HoleFill.mc.field_71441_e.func_180495_p(pos).func_185904_a().func_76230_c() && !HoleFill.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_185904_a().func_76230_c()) {
                final boolean solidNeighbours = (HoleFill.mc.field_71441_e.func_180495_p(pos.func_177982_a(1, 0, 0)).func_177230_c() == Blocks.field_150357_h | HoleFill.mc.field_71441_e.func_180495_p(pos.func_177982_a(1, 0, 0)).func_177230_c() == Blocks.field_150343_Z) && (HoleFill.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 0, 1)).func_177230_c() == Blocks.field_150357_h | HoleFill.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 0, 1)).func_177230_c() == Blocks.field_150343_Z) && (HoleFill.mc.field_71441_e.func_180495_p(pos.func_177982_a(-1, 0, 0)).func_177230_c() == Blocks.field_150357_h | HoleFill.mc.field_71441_e.func_180495_p(pos.func_177982_a(-1, 0, 0)).func_177230_c() == Blocks.field_150343_Z) && (HoleFill.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 0, -1)).func_177230_c() == Blocks.field_150357_h | HoleFill.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 0, -1)).func_177230_c() == Blocks.field_150343_Z) && HoleFill.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 0, 0)).func_185904_a() == Material.field_151579_a && HoleFill.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_185904_a() == Material.field_151579_a && HoleFill.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 2, 0)).func_185904_a() == Material.field_151579_a;
                if (!solidNeighbours) {
                    continue;
                }
                this.holes.add(pos);
            }
        }
        int newSlot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = HoleFill.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack != ItemStack.field_190927_a) {
                if (stack.func_77973_b() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
                    if (this.whiteList.contains(block)) {
                        newSlot = i;
                        break;
                    }
                }
            }
        }
        if (newSlot == -1) {
            return;
        }
        final int oldSlot = HoleFill.mc.field_71439_g.field_71071_by.field_70461_c;
        if (this.waitTick.getValue() > 0.0) {
            if (this.waitCounter < this.waitTick.getValue()) {
                HoleFill.mc.field_71439_g.field_71071_by.field_70461_c = newSlot;
                this.holes.forEach(this::place);
                HoleFill.mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
                return;
            }
            this.waitCounter = 0;
        }
    }
    
    public void onEnable() {
        if (HoleFill.mc.field_71439_g != null && this.chat.getValue()) {
            Command.sendChatMessage("HoleFill " + ChatFormatting.GREEN + "Enabled!");
        }
    }
    
    public void onDisable() {
        if (HoleFill.mc.field_71439_g != null && this.chat.getValue()) {
            Command.sendChatMessage("HoleFill " + ChatFormatting.RED + "Disabled!");
        }
    }
    
    private void place(final BlockPos blockPos) {
        for (final Entity entity : HoleFill.mc.field_71441_e.func_72839_b((Entity)null, new AxisAlignedBB(blockPos))) {
            if (entity instanceof EntityLivingBase) {
                return;
            }
        }
        Surround.placeBlockScaffold(blockPos, this.rotate.getValue());
        ++this.waitCounter;
    }
}
