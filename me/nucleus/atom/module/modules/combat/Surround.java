// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.combat;

import net.minecraft.util.math.MathHelper;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.Vec3i;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import me.nucleus.atom.setting.Settings;
import java.util.Arrays;
import net.minecraft.init.Blocks;
import me.nucleus.atom.setting.Setting;
import net.minecraft.block.Block;
import java.util.List;
import me.nucleus.atom.module.Module;

@Info(name = "Surround", category = Category.COMBAT, description = "Surrounds you with obsidian to take less damage")
public class Surround extends Module
{
    private List<Block> whiteList;
    private Setting<Boolean> sneak;
    private Setting<Boolean> rotate;
    private Setting<Boolean> disableAfterPlacing;
    
    public Surround() {
        this.whiteList = Arrays.asList(Blocks.field_150343_Z, Blocks.field_150477_bB);
        this.sneak = this.register(Settings.b("SneakOnly", false));
        this.rotate = this.register(Settings.b("Rotate", true));
        this.disableAfterPlacing = this.register(Settings.b("DisableAfterPlacing", true));
    }
    
    public static boolean hasNeighbour(final BlockPos blockPos) {
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = blockPos.func_177972_a(side);
            if (!Surround.mc.field_71441_e.func_180495_p(neighbour).func_185904_a().func_76222_j()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onUpdate() {
        if (this.sneak.getValue() && !Surround.mc.field_71474_y.field_74311_E.func_151470_d()) {
            return;
        }
        if (!this.isEnabled() || Surround.mc.field_71439_g == null) {
            return;
        }
        final Vec3d vec3d = getInterpolatedPos((Entity)Surround.mc.field_71439_g, 0.0f);
        BlockPos northBlockPos = new BlockPos(vec3d).func_177978_c();
        BlockPos southBlockPos = new BlockPos(vec3d).func_177968_d();
        BlockPos eastBlockPos = new BlockPos(vec3d).func_177974_f();
        BlockPos westBlockPos = new BlockPos(vec3d).func_177976_e();
        int newSlot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Surround.mc.field_71439_g.field_71071_by.func_70301_a(i);
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
        final int oldSlot = Surround.mc.field_71439_g.field_71071_by.field_70461_c;
        Surround.mc.field_71439_g.field_71071_by.field_70461_c = newSlot;
        Label_0302: {
            if (!hasNeighbour(northBlockPos)) {
                for (final EnumFacing side : EnumFacing.values()) {
                    final BlockPos neighbour = northBlockPos.func_177972_a(side);
                    if (hasNeighbour(neighbour)) {
                        northBlockPos = neighbour;
                        break Label_0302;
                    }
                }
                return;
            }
        }
        Label_0365: {
            if (!hasNeighbour(southBlockPos)) {
                for (final EnumFacing side : EnumFacing.values()) {
                    final BlockPos neighbour = southBlockPos.func_177972_a(side);
                    if (hasNeighbour(neighbour)) {
                        southBlockPos = neighbour;
                        break Label_0365;
                    }
                }
                return;
            }
        }
        Label_0431: {
            if (!hasNeighbour(eastBlockPos)) {
                for (final EnumFacing side : EnumFacing.values()) {
                    final BlockPos neighbour = eastBlockPos.func_177972_a(side);
                    if (hasNeighbour(neighbour)) {
                        eastBlockPos = neighbour;
                        break Label_0431;
                    }
                }
                return;
            }
        }
        Label_0497: {
            if (!hasNeighbour(westBlockPos)) {
                for (final EnumFacing side : EnumFacing.values()) {
                    final BlockPos neighbour = westBlockPos.func_177972_a(side);
                    if (hasNeighbour(neighbour)) {
                        westBlockPos = neighbour;
                        break Label_0497;
                    }
                }
                return;
            }
        }
        if (Surround.mc.field_71441_e.func_180495_p(northBlockPos).func_185904_a().func_76222_j() && this.isEntitiesEmpty(northBlockPos)) {
            placeBlockScaffold(northBlockPos, this.rotate.getValue());
        }
        if (Surround.mc.field_71441_e.func_180495_p(southBlockPos).func_185904_a().func_76222_j() && this.isEntitiesEmpty(southBlockPos)) {
            placeBlockScaffold(southBlockPos, this.rotate.getValue());
        }
        if (Surround.mc.field_71441_e.func_180495_p(eastBlockPos).func_185904_a().func_76222_j() && this.isEntitiesEmpty(eastBlockPos)) {
            placeBlockScaffold(eastBlockPos, this.rotate.getValue());
        }
        if (Surround.mc.field_71441_e.func_180495_p(westBlockPos).func_185904_a().func_76222_j() && this.isEntitiesEmpty(westBlockPos)) {
            placeBlockScaffold(westBlockPos, this.rotate.getValue());
        }
        Surround.mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
        if (this.disableAfterPlacing.getValue()) {
            this.disable();
        }
    }
    
    private boolean isEntitiesEmpty(final BlockPos pos) {
        final List<Entity> entities = (List<Entity>)Surround.mc.field_71441_e.func_72839_b((Entity)null, new AxisAlignedBB(pos)).stream().filter(e -> !(e instanceof EntityItem)).filter(e -> !(e instanceof EntityXPOrb)).collect(Collectors.toList());
        return entities.isEmpty();
    }
    
    public static boolean placeBlockScaffold(final BlockPos pos, final boolean rotate) {
        final Vec3d eyesPos = new Vec3d(Surround.mc.field_71439_g.field_70165_t, Surround.mc.field_71439_g.field_70163_u + Surround.mc.field_71439_g.func_70047_e(), Surround.mc.field_71439_g.field_70161_v);
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = pos.func_177972_a(side);
            final EnumFacing side2 = side.func_176734_d();
            if (canBeClicked(neighbor)) {
                final Vec3d hitVec = new Vec3d((Vec3i)neighbor).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(side2.func_176730_m()).func_186678_a(0.5));
                if (rotate) {
                    faceVectorPacketInstant(hitVec);
                }
                Surround.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Surround.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
                processRightClickBlock(neighbor, side2, hitVec);
                Surround.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                Surround.mc.field_71467_ac = 0;
                Surround.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Surround.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
                return true;
            }
        }
        return false;
    }
    
    private static PlayerControllerMP getPlayerController() {
        return Surround.mc.field_71442_b;
    }
    
    public static void processRightClickBlock(final BlockPos pos, final EnumFacing side, final Vec3d hitVec) {
        getPlayerController().func_187099_a(Surround.mc.field_71439_g, Surround.mc.field_71441_e, pos, side, hitVec, EnumHand.MAIN_HAND);
    }
    
    public static IBlockState getState(final BlockPos pos) {
        return Surround.mc.field_71441_e.func_180495_p(pos);
    }
    
    public static Block getBlock(final BlockPos pos) {
        return getState(pos).func_177230_c();
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).func_176209_a(getState(pos), false);
    }
    
    public static void faceVectorPacketInstant(final Vec3d vec) {
        final float[] rotations = getNeededRotations2(vec);
        Surround.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], Surround.mc.field_71439_g.field_70122_E));
    }
    
    private static float[] getNeededRotations2(final Vec3d vec) {
        final Vec3d eyesPos = getEyesPos();
        final double diffX = vec.field_72450_a - eyesPos.field_72450_a;
        final double diffY = vec.field_72448_b - eyesPos.field_72448_b;
        final double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { Surround.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g(yaw - Surround.mc.field_71439_g.field_70177_z), Surround.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g(pitch - Surround.mc.field_71439_g.field_70125_A) };
    }
    
    public static Vec3d getEyesPos() {
        return new Vec3d(Surround.mc.field_71439_g.field_70165_t, Surround.mc.field_71439_g.field_70163_u + Surround.mc.field_71439_g.func_70047_e(), Surround.mc.field_71439_g.field_70161_v);
    }
    
    public static Vec3d getInterpolatedPos(final Entity entity, final float ticks) {
        return new Vec3d(entity.field_70142_S, entity.field_70137_T, entity.field_70136_U).func_178787_e(getInterpolatedAmount(entity, ticks));
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final double ticks) {
        return getInterpolatedAmount(entity, ticks, ticks, ticks);
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final double x, final double y, final double z) {
        return new Vec3d((entity.field_70165_t - entity.field_70142_S) * x, (entity.field_70163_u - entity.field_70137_T) * y, (entity.field_70161_v - entity.field_70136_U) * z);
    }
}
