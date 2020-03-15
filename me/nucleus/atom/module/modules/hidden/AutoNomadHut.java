// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.hidden;

import net.minecraft.util.math.MathHelper;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3i;
import me.nucleus.atom.util.LagCompensator;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import me.nucleus.atom.module.Module;

@Info(name = "AutoNomadHut", category = Category.HIDDEN, description = "Clown down.")
public class AutoNomadHut extends Module
{
    private int waitCounter;
    private int i;
    private BlockPos basePos;
    private int offsetStep;
    private int playerHotbarSlot;
    private int lastHotbarSlot;
    Vec3d[] surroundTargets;
    
    public AutoNomadHut() {
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.surroundTargets = new Vec3d[] { new Vec3d(0.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 1.0), new Vec3d(1.0, 0.0, -1.0), new Vec3d(-1.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, -1.0), new Vec3d(2.0, 0.0, 0.0), new Vec3d(2.0, 0.0, 1.0), new Vec3d(2.0, 0.0, -1.0), new Vec3d(-2.0, 0.0, 0.0), new Vec3d(-2.0, 0.0, 1.0), new Vec3d(-2.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 2.0), new Vec3d(1.0, 0.0, 2.0), new Vec3d(-1.0, 0.0, 2.0), new Vec3d(0.0, 0.0, -2.0), new Vec3d(-1.0, 0.0, -2.0), new Vec3d(1.0, 0.0, -2.0), new Vec3d(2.0, 1.0, -1.0), new Vec3d(2.0, 1.0, 1.0), new Vec3d(-2.0, 1.0, 0.0), new Vec3d(-2.0, 1.0, 1.0), new Vec3d(-2.0, 1.0, -1.0), new Vec3d(0.0, 1.0, 2.0), new Vec3d(1.0, 1.0, 2.0), new Vec3d(-1.0, 1.0, 2.0), new Vec3d(0.0, 1.0, -2.0), new Vec3d(1.0, 1.0, -2.0), new Vec3d(-1.0, 1.0, -2.0), new Vec3d(2.0, 2.0, -1.0), new Vec3d(2.0, 2.0, 1.0), new Vec3d(-2.0, 2.0, 1.0), new Vec3d(-2.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 2.0), new Vec3d(-1.0, 2.0, 2.0), new Vec3d(1.0, 2.0, -2.0), new Vec3d(-1.0, 2.0, -2.0), new Vec3d(2.0, 3.0, 0.0), new Vec3d(2.0, 3.0, -1.0), new Vec3d(2.0, 3.0, 1.0), new Vec3d(-2.0, 3.0, 0.0), new Vec3d(-2.0, 3.0, 1.0), new Vec3d(-2.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 2.0), new Vec3d(1.0, 3.0, 2.0), new Vec3d(-1.0, 3.0, 2.0), new Vec3d(0.0, 3.0, -2.0), new Vec3d(1.0, 3.0, -2.0), new Vec3d(-1.0, 3.0, -2.0), new Vec3d(0.0, 4.0, 0.0), new Vec3d(1.0, 4.0, 0.0), new Vec3d(-1.0, 4.0, 0.0), new Vec3d(0.0, 4.0, 1.0), new Vec3d(0.0, 4.0, -1.0), new Vec3d(1.0, 4.0, 1.0), new Vec3d(-1.0, 4.0, 1.0), new Vec3d(-1.0, 4.0, -1.0), new Vec3d(1.0, 4.0, -1.0), new Vec3d(2.0, 4.0, 0.0), new Vec3d(2.0, 4.0, 1.0), new Vec3d(2.0, 4.0, -1.0) };
    }
    
    @Override
    public void onUpdate() {
        if (!this.isEnabled() || AutoNomadHut.mc.field_71439_g == null) {
            return;
        }
        if (this.offsetStep == 0) {
            this.basePos = new BlockPos(AutoNomadHut.mc.field_71439_g.func_174791_d()).func_177977_b();
            this.playerHotbarSlot = AutoNomadHut.mc.field_71439_g.field_71071_by.field_70461_c;
        }
        final double autoWaitTick = 20.0 - Math.round(LagCompensator.INSTANCE.getTickRate() * 10.0f) / 10.0;
        if (this.waitCounter < autoWaitTick) {
            ++this.waitCounter;
            return;
        }
        this.waitCounter = 0;
        if (this.offsetStep >= this.surroundTargets.length) {
            this.endLoop();
            return;
        }
        final Vec3d offset = this.surroundTargets[this.offsetStep];
        this.placeBlock(new BlockPos((Vec3i)this.basePos.func_177963_a(offset.field_72450_a, offset.field_72448_b, offset.field_72449_c)));
        ++this.offsetStep;
    }
    
    public void onEnable() {
        if (AutoNomadHut.mc.field_71439_g == null) {
            this.disable();
            return;
        }
        this.playerHotbarSlot = AutoNomadHut.mc.field_71439_g.field_71071_by.field_70461_c;
        this.lastHotbarSlot = -1;
        this.i = 0;
        this.waitCounter = 0;
    }
    
    public void onDisable() {
        if (AutoNomadHut.mc.field_71439_g == null) {
            return;
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            AutoNomadHut.mc.field_71439_g.field_71071_by.field_70461_c = this.playerHotbarSlot;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
    }
    
    private void endLoop() {
        this.offsetStep = 0;
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            AutoNomadHut.mc.field_71439_g.field_71071_by.field_70461_c = this.playerHotbarSlot;
            this.lastHotbarSlot = this.playerHotbarSlot;
        }
    }
    
    private void placeBlock(final BlockPos blockPos) {
        if (!AutoNomadHut.mc.field_71441_e.func_180495_p(blockPos).func_185904_a().func_76222_j()) {
            return;
        }
        if (!hasNeighbour(blockPos)) {
            return;
        }
        this.placeBlockExecute(blockPos);
    }
    
    private int findObiInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoNomadHut.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack != ItemStack.field_190927_a && stack.func_77973_b() instanceof ItemBlock) {
                final Block block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
                if (block instanceof BlockObsidian) {
                    slot = i;
                    break;
                }
            }
        }
        return slot;
    }
    
    public void placeBlockExecute(final BlockPos pos) {
        final Vec3d eyesPos = new Vec3d(AutoNomadHut.mc.field_71439_g.field_70165_t, AutoNomadHut.mc.field_71439_g.field_70163_u + AutoNomadHut.mc.field_71439_g.func_70047_e(), AutoNomadHut.mc.field_71439_g.field_70161_v);
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = pos.func_177972_a(side);
            final EnumFacing side2 = side.func_176734_d();
            if (canBeClicked(neighbor)) {
                final Vec3d hitVec = new Vec3d((Vec3i)neighbor).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(side2.func_176730_m()).func_186678_a(0.5));
                if (eyesPos.func_72436_e(hitVec) <= 18.0625) {
                    boolean needSneak = false;
                    final Block blockBelow = AutoNomadHut.mc.field_71441_e.func_180495_p(neighbor).func_177230_c();
                    needSneak = true;
                    if (needSneak) {
                        AutoNomadHut.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)AutoNomadHut.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
                    }
                    final int obiSlot = this.findObiInHotbar();
                    if (obiSlot == -1) {
                        this.disable();
                        return;
                    }
                    if (this.lastHotbarSlot != obiSlot) {
                        AutoNomadHut.mc.field_71439_g.field_71071_by.field_70461_c = obiSlot;
                        this.lastHotbarSlot = obiSlot;
                    }
                    AutoNomadHut.mc.field_71442_b.func_187099_a(AutoNomadHut.mc.field_71439_g, AutoNomadHut.mc.field_71441_e, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
                    AutoNomadHut.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
                    if (needSneak) {
                        AutoNomadHut.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)AutoNomadHut.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
                    }
                    return;
                }
            }
        }
    }
    
    private static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).func_176209_a(getState(pos), false);
    }
    
    private static Block getBlock(final BlockPos pos) {
        return getState(pos).func_177230_c();
    }
    
    private static IBlockState getState(final BlockPos pos) {
        return AutoNomadHut.mc.field_71441_e.func_180495_p(pos);
    }
    
    private static void faceVectorPacketInstant(final Vec3d vec) {
        final float[] rotations = getLegitRotations(vec);
        AutoNomadHut.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], AutoNomadHut.mc.field_71439_g.field_70122_E));
    }
    
    private static float[] getLegitRotations(final Vec3d vec) {
        final Vec3d eyesPos = getEyesPos();
        final double diffX = vec.field_72450_a - eyesPos.field_72450_a;
        final double diffY = vec.field_72448_b - eyesPos.field_72448_b;
        final double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { AutoNomadHut.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g(yaw - AutoNomadHut.mc.field_71439_g.field_70177_z), AutoNomadHut.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g(pitch - AutoNomadHut.mc.field_71439_g.field_70125_A) };
    }
    
    private static Vec3d getEyesPos() {
        return new Vec3d(AutoNomadHut.mc.field_71439_g.field_70165_t, AutoNomadHut.mc.field_71439_g.field_70163_u + AutoNomadHut.mc.field_71439_g.func_70047_e(), AutoNomadHut.mc.field_71439_g.field_70161_v);
    }
    
    private static boolean hasNeighbour(final BlockPos blockPos) {
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = blockPos.func_177972_a(side);
            if (!AutoNomadHut.mc.field_71441_e.func_180495_p(neighbour).func_185904_a().func_76222_j()) {
                return true;
            }
        }
        return false;
    }
}
