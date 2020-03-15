// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.combat;

import me.nucleus.atom.util.Friends;
import me.nucleus.atom.util.EntityUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.world.GameType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import me.nucleus.atom.util.BlockInteractionHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.util.math.Vec3d;
import java.util.ArrayList;
import me.nucleus.atom.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.nucleus.atom.module.ModuleManager;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import me.nucleus.atom.setting.Settings;
import net.minecraft.entity.player.EntityPlayer;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "AutoTrap", category = Category.COMBAT)
public class AutoTrap extends Module
{
    private Setting<Double> range;
    private Setting<Integer> blocksPerTick;
    private Setting<Integer> tickDelay;
    private Setting<Cage> cage;
    private Setting<Boolean> rotate;
    private Setting<Boolean> noGlitchBlocks;
    private Setting<Boolean> activeInFreecam;
    private Setting<Boolean> infoMessage;
    private EntityPlayer closestTarget;
    private String lastTargetName;
    private int playerHotbarSlot;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int delayStep;
    private int offsetStep;
    private boolean firstRun;
    private boolean missingObiDisable;
    
    public AutoTrap() {
        this.range = this.register((Setting<Double>)Settings.doubleBuilder("Range").withMinimum(3.5).withValue(5.5).withMaximum(10.0).build());
        this.blocksPerTick = this.register((Setting<Integer>)Settings.integerBuilder("BlocksPerTick").withMinimum(1).withValue(2).withMaximum(23).build());
        this.tickDelay = this.register((Setting<Integer>)Settings.integerBuilder("TickDelay").withMinimum(0).withValue(2).withMaximum(10).build());
        this.cage = this.register(Settings.e("Cage", Cage.TRAP));
        this.rotate = this.register(Settings.b("Rotate", false));
        this.noGlitchBlocks = this.register(Settings.b("NoGlitchBlocks", true));
        this.activeInFreecam = this.register(Settings.b("Active In Freecam", true));
        this.infoMessage = this.register(Settings.b("Debug", false));
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.isSneaking = false;
        this.delayStep = 0;
        this.offsetStep = 0;
        this.missingObiDisable = false;
    }
    
    private static EnumFacing getPlaceableSide(final BlockPos pos) {
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = pos.func_177972_a(side);
            if (AutoTrap.mc.field_71441_e.func_180495_p(neighbour).func_177230_c().func_176209_a(AutoTrap.mc.field_71441_e.func_180495_p(neighbour), false)) {
                final IBlockState blockState = AutoTrap.mc.field_71441_e.func_180495_p(neighbour);
                if (!blockState.func_185904_a().func_76222_j()) {
                    return side;
                }
            }
        }
        return null;
    }
    
    @Override
    protected void onEnable() {
        if (AutoTrap.mc.field_71439_g == null) {
            this.disable();
            return;
        }
        this.firstRun = true;
        this.playerHotbarSlot = AutoTrap.mc.field_71439_g.field_71071_by.field_70461_c;
        this.lastHotbarSlot = -1;
    }
    
    @Override
    protected void onDisable() {
        if (AutoTrap.mc.field_71439_g == null) {
            return;
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            AutoTrap.mc.field_71439_g.field_71071_by.field_70461_c = this.playerHotbarSlot;
        }
        if (this.isSneaking) {
            AutoTrap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)AutoTrap.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.missingObiDisable = false;
    }
    
    @Override
    public void onUpdate() {
        if (AutoTrap.mc.field_71439_g == null) {
            return;
        }
        if (!this.activeInFreecam.getValue() && ModuleManager.isModuleEnabled("Freecam")) {
            return;
        }
        if (this.firstRun) {
            if (this.findObiInHotbar() == -1) {
                if (this.infoMessage.getValue()) {
                    Command.sendChatMessage("[AutoTrap] " + ChatFormatting.RED + "Disabled" + ChatFormatting.RESET + ", Obsidian missing!");
                }
                this.disable();
                return;
            }
        }
        else {
            if (this.delayStep < this.tickDelay.getValue()) {
                ++this.delayStep;
                return;
            }
            this.delayStep = 0;
        }
        this.findClosestTarget();
        if (this.closestTarget == null) {
            return;
        }
        if (this.firstRun) {
            this.firstRun = false;
            this.lastTargetName = this.closestTarget.func_70005_c_();
        }
        else if (!this.lastTargetName.equals(this.closestTarget.func_70005_c_())) {
            this.offsetStep = 0;
            this.lastTargetName = this.closestTarget.func_70005_c_();
        }
        final List<Vec3d> placeTargets = new ArrayList<Vec3d>();
        if (this.cage.getValue().equals(Cage.TRAP)) {
            Collections.addAll(placeTargets, Offsets.TRAP);
        }
        if (this.cage.getValue().equals(Cage.CRYSTALEXA)) {
            Collections.addAll(placeTargets, Offsets.CRYSTALEXA);
        }
        if (this.cage.getValue().equals(Cage.CRYSTALFULL)) {
            Collections.addAll(placeTargets, Offsets.CRYSTALFULL);
        }
        int blocksPlaced = 0;
        while (blocksPlaced < this.blocksPerTick.getValue()) {
            if (this.offsetStep >= placeTargets.size()) {
                this.offsetStep = 0;
                break;
            }
            final BlockPos offsetPos = new BlockPos((Vec3d)placeTargets.get(this.offsetStep));
            final BlockPos targetPos = new BlockPos(this.closestTarget.func_174791_d()).func_177977_b().func_177982_a(offsetPos.field_177962_a, offsetPos.field_177960_b, offsetPos.field_177961_c);
            if (this.placeBlockInRange(targetPos, this.range.getValue())) {
                ++blocksPlaced;
            }
            ++this.offsetStep;
        }
        if (blocksPlaced > 0) {
            if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
                AutoTrap.mc.field_71439_g.field_71071_by.field_70461_c = this.playerHotbarSlot;
                this.lastHotbarSlot = this.playerHotbarSlot;
            }
            if (this.isSneaking) {
                AutoTrap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)AutoTrap.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
                this.isSneaking = false;
            }
        }
        if (this.missingObiDisable) {
            this.missingObiDisable = false;
            if (this.infoMessage.getValue()) {
                Command.sendChatMessage("[AutoTrap] " + ChatFormatting.RED + "Disabled" + ChatFormatting.RESET + ", Obsidian missing!");
            }
            this.disable();
        }
    }
    
    private boolean placeBlockInRange(final BlockPos pos, final double range) {
        final Block block = AutoTrap.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        for (final Entity entity : AutoTrap.mc.field_71441_e.func_72839_b((Entity)null, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
                return false;
            }
        }
        final EnumFacing side = getPlaceableSide(pos);
        if (side == null) {
            return false;
        }
        final BlockPos neighbour = pos.func_177972_a(side);
        final EnumFacing opposite = side.func_176734_d();
        if (!BlockInteractionHelper.canBeClicked(neighbour)) {
            return false;
        }
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        final Block neighbourBlock = AutoTrap.mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
        if (AutoTrap.mc.field_71439_g.func_174791_d().func_72438_d(hitVec) > range) {
            return false;
        }
        final int obiSlot = this.findObiInHotbar();
        if (obiSlot == -1) {
            this.missingObiDisable = true;
            return false;
        }
        if (this.lastHotbarSlot != obiSlot) {
            AutoTrap.mc.field_71439_g.field_71071_by.field_70461_c = obiSlot;
            this.lastHotbarSlot = obiSlot;
        }
        if ((!this.isSneaking && BlockInteractionHelper.blackList.contains(neighbourBlock)) || BlockInteractionHelper.shulkerList.contains(neighbourBlock)) {
            AutoTrap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)AutoTrap.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            this.isSneaking = true;
        }
        if (this.rotate.getValue()) {
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
        }
        AutoTrap.mc.field_71442_b.func_187099_a(AutoTrap.mc.field_71439_g, AutoTrap.mc.field_71441_e, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        AutoTrap.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        AutoTrap.mc.field_71467_ac = 4;
        if (this.noGlitchBlocks.getValue() && !AutoTrap.mc.field_71442_b.func_178889_l().equals((Object)GameType.CREATIVE)) {
            AutoTrap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, neighbour, opposite));
        }
        return true;
    }
    
    private int findObiInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoTrap.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack != ItemStack.field_190927_a) {
                if (stack.func_77973_b() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
                    if (block instanceof BlockObsidian) {
                        slot = i;
                        break;
                    }
                }
            }
        }
        return slot;
    }
    
    private void findClosestTarget() {
        final List<EntityPlayer> playerList = (List<EntityPlayer>)AutoTrap.mc.field_71441_e.field_73010_i;
        this.closestTarget = null;
        for (final EntityPlayer target : playerList) {
            if (target == AutoTrap.mc.field_71439_g) {
                continue;
            }
            if (AutoTrap.mc.field_71439_g.func_70032_d((Entity)target) > this.range.getValue() + 3.0) {
                continue;
            }
            if (!EntityUtil.isLiving((Entity)target)) {
                continue;
            }
            if (target.func_110143_aJ() <= 0.0f) {
                continue;
            }
            if (Friends.isFriend(target.func_70005_c_())) {
                continue;
            }
            if (this.closestTarget == null) {
                this.closestTarget = target;
            }
            else {
                if (AutoTrap.mc.field_71439_g.func_70032_d((Entity)target) >= AutoTrap.mc.field_71439_g.func_70032_d((Entity)this.closestTarget)) {
                    continue;
                }
                this.closestTarget = target;
            }
        }
    }
    
    @Override
    public String getHudInfo() {
        if (this.closestTarget != null) {
            return this.closestTarget.func_70005_c_().toUpperCase();
        }
        return "NO TARGET";
    }
    
    private enum Cage
    {
        TRAP, 
        CRYSTALEXA, 
        CRYSTALFULL;
    }
    
    private static class Offsets
    {
        private static final Vec3d[] TRAP;
        private static final Vec3d[] CRYSTALEXA;
        private static final Vec3d[] CRYSTALFULL;
        
        static {
            TRAP = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0) };
            CRYSTALEXA = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(-1.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 1.0), new Vec3d(1.0, 2.0, -1.0), new Vec3d(-1.0, 2.0, 1.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0) };
            CRYSTALFULL = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(-1.0, 0.0, 1.0), new Vec3d(1.0, 0.0, -1.0), new Vec3d(-1.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 1.0), new Vec3d(-1.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 1.0), new Vec3d(1.0, 1.0, -1.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(-1.0, 2.0, 1.0), new Vec3d(1.0, 2.0, -1.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0) };
        }
    }
}
