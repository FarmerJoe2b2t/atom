// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.combat;

import me.nucleus.atom.command.Command;
import me.nucleus.atom.AtomMod;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.MathHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.CombatRules;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAppleGold;
import me.nucleus.atom.util.KamiTessellator;
import java.awt.Color;
import me.nucleus.atom.event.events.RenderEvent;
import net.minecraft.util.math.RayTraceResult;
import java.util.List;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import me.nucleus.atom.util.Friends;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraft.init.MobEffects;
import me.nucleus.atom.util.LagCompensator;
import java.util.Comparator;
import me.nucleus.atom.module.ModuleManager;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.network.play.server.SPacketSoundEffect;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketPlayer;
import me.nucleus.atom.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.nucleus.atom.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "AutoCrystal", category = Category.COMBAT)
public class AutoCrystal extends Module
{
    private Setting<Boolean> explode;
    private Setting<Boolean> autoTickDelay;
    private Setting<Double> waitTick;
    private Setting<Double> range;
    private Setting<Double> walls;
    private Setting<Boolean> antiWeakness;
    private Setting<Boolean> nodesync;
    private Setting<Boolean> place;
    private Setting<Boolean> autoSwitch;
    private Setting<Boolean> noGappleSwitch;
    private Setting<Double> placeRange;
    private Setting<Double> minDmg;
    private Setting<Double> facePlace;
    private Setting<Boolean> raytrace;
    private Setting<Boolean> rotate;
    private Setting<Boolean> spoofRotations;
    private Setting<Boolean> chat;
    private Setting<Double> maxSelfDmg;
    private Setting<Boolean> targetWithers;
    private BlockPos render;
    private Entity renderEnt;
    private boolean switchCooldown;
    private boolean isAttacking;
    private int oldSlot;
    private int newSlot;
    private int waitCounter;
    EnumFacing f;
    private boolean isActive;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    @EventHandler
    private Listener<PacketEvent.Send> packetSendListener;
    @EventHandler
    private Listener<PacketEvent.Receive> packetReceiveListener;
    
    public AutoCrystal() {
        this.explode = this.register(Settings.b("Explode"));
        this.autoTickDelay = this.register(Settings.b("Auto Tick Delay", false));
        this.waitTick = this.register(Settings.d("Tick Delay", 1.0));
        this.range = this.register(Settings.d("Hit Range", 5.0));
        this.walls = this.register(Settings.d("Walls Range", 3.5));
        this.antiWeakness = this.register(Settings.b("Anti Weakness", true));
        this.nodesync = this.register(Settings.b("No Desync", true));
        this.place = this.register(Settings.b("Place", true));
        this.autoSwitch = this.register(Settings.b("Auto Switch", true));
        this.noGappleSwitch = this.register(Settings.b("No Gap Switch", false));
        this.placeRange = this.register(Settings.d("Place Range", 5.0));
        this.minDmg = this.register(Settings.d("Min Damage", 5.0));
        this.facePlace = this.register(Settings.d("Faceplace HP", 6.0));
        this.raytrace = this.register(Settings.b("Ray Trace", false));
        this.rotate = this.register(Settings.b("Rotate", true));
        this.spoofRotations = this.register(Settings.b("Spoof Angles", true));
        this.chat = this.register(Settings.b("Toggle Messages", true));
        this.maxSelfDmg = this.register(Settings.d("Max Self Dmg", 10.0));
        this.targetWithers = this.register(Settings.b("Withers", false));
        this.switchCooldown = false;
        this.isAttacking = false;
        this.oldSlot = -1;
        final Packet packet;
        this.packetSendListener = new Listener<PacketEvent.Send>(event -> {
            packet = event.getPacket();
            if (packet instanceof CPacketPlayer && this.spoofRotations.getValue() && AutoCrystal.isSpoofingAngles) {
                ((CPacketPlayer)packet).field_149476_e = (float)AutoCrystal.yaw;
                ((CPacketPlayer)packet).field_149473_f = (float)AutoCrystal.pitch;
            }
            return;
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
        SPacketSoundEffect packet2;
        final Iterator<Entity> iterator;
        Entity e;
        this.packetReceiveListener = new Listener<PacketEvent.Receive>(event -> {
            if (event.getPacket() instanceof SPacketSoundEffect && this.nodesync.getValue()) {
                packet2 = (SPacketSoundEffect)event.getPacket();
                if (packet2.func_186977_b() == SoundCategory.BLOCKS && packet2.func_186978_a() == SoundEvents.field_187539_bB) {
                    Minecraft.func_71410_x().field_71441_e.field_72996_f.iterator();
                    while (iterator.hasNext()) {
                        e = iterator.next();
                        if (e instanceof EntityEnderCrystal && e.func_70011_f(packet2.func_149207_d(), packet2.func_149211_e(), packet2.func_149210_f()) <= 6.0) {
                            e.func_70106_y();
                        }
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        this.isActive = false;
        if (AutoCrystal.mc.field_71439_g == null || AutoCrystal.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (ModuleManager.getModuleByName("Surround").isEnabled()) {
            return;
        }
        final EntityEnderCrystal crystal = (EntityEnderCrystal)AutoCrystal.mc.field_71441_e.field_72996_f.stream().filter(entity -> entity instanceof EntityEnderCrystal).filter(e -> AutoCrystal.mc.field_71439_g.func_70032_d(e) <= this.range.getValue()).map(entity -> entity).min(Comparator.comparing(c -> AutoCrystal.mc.field_71439_g.func_70032_d(c))).orElse(null);
        if (this.explode.getValue() && crystal != null) {
            if (!AutoCrystal.mc.field_71439_g.func_70685_l((Entity)crystal) && AutoCrystal.mc.field_71439_g.func_70032_d((Entity)crystal) > this.walls.getValue()) {
                return;
            }
            if (this.waitTick.getValue() > 0.0) {
                final int waitValue = (int)(this.autoTickDelay.getValue() ? Math.ceil(20.0 - LagCompensator.INSTANCE.getTickRate()) : this.waitTick.getValue());
                if (this.waitCounter < waitValue) {
                    ++this.waitCounter;
                    return;
                }
                this.waitCounter = 0;
            }
            if (this.antiWeakness.getValue() && AutoCrystal.mc.field_71439_g.func_70644_a(MobEffects.field_76437_t)) {
                if (!this.isAttacking) {
                    this.oldSlot = AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c;
                    this.isAttacking = true;
                }
                this.newSlot = -1;
                for (int i = 0; i < 9; ++i) {
                    final ItemStack stack = AutoCrystal.mc.field_71439_g.field_71071_by.func_70301_a(i);
                    if (stack != ItemStack.field_190927_a) {
                        if (stack.func_77973_b() instanceof ItemSword) {
                            this.newSlot = i;
                            break;
                        }
                        if (stack.func_77973_b() instanceof ItemTool) {
                            this.newSlot = i;
                            break;
                        }
                    }
                }
                if (this.newSlot != -1) {
                    AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c = this.newSlot;
                    this.switchCooldown = true;
                }
            }
            this.isActive = true;
            if (this.rotate.getValue()) {
                this.lookAtPacket(crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v, (EntityPlayer)AutoCrystal.mc.field_71439_g);
            }
            AutoCrystal.mc.field_71442_b.func_78764_a((EntityPlayer)AutoCrystal.mc.field_71439_g, (Entity)crystal);
            AutoCrystal.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            this.isActive = false;
        }
        else {
            resetRotation();
            if (this.oldSlot != -1) {
                AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c = this.oldSlot;
                this.oldSlot = -1;
            }
            this.isAttacking = false;
            this.isActive = false;
            int crystalSlot = (AutoCrystal.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP) ? AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c : -1;
            if (crystalSlot == -1) {
                for (int l = 0; l < 9; ++l) {
                    if (AutoCrystal.mc.field_71439_g.field_71071_by.func_70301_a(l).func_77973_b() == Items.field_185158_cP) {
                        crystalSlot = l;
                        break;
                    }
                }
            }
            boolean offhand = false;
            if (AutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP) {
                offhand = true;
            }
            else if (crystalSlot == -1) {
                return;
            }
            final List<BlockPos> blocks = this.findCrystalBlocks();
            final List<Entity> entities = new ArrayList<Entity>();
            entities.addAll((Collection<? extends Entity>)AutoCrystal.mc.field_71441_e.field_73010_i.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.func_70005_c_())).sorted(Comparator.comparing(e -> AutoCrystal.mc.field_71439_g.func_70032_d(e))).collect(Collectors.toList()));
            if (this.targetWithers.getValue()) {
                entities.addAll((Collection<? extends Entity>)AutoCrystal.mc.field_71441_e.func_72910_y().stream().filter(e -> e instanceof EntityWither).sorted(Comparator.comparing(e -> AutoCrystal.mc.field_71439_g.func_70032_d(e))).collect(Collectors.toList()));
            }
            BlockPos q = null;
            double damage = 0.5;
            for (final Entity entity2 : entities) {
                if (entity2 == AutoCrystal.mc.field_71439_g) {
                    continue;
                }
                if (((EntityLivingBase)entity2).func_110143_aJ() <= 0.0f || entity2.field_70128_L) {
                    continue;
                }
                if (AutoCrystal.mc.field_71439_g == null) {
                    continue;
                }
                for (final BlockPos blockPos : blocks) {
                    final double b = entity2.func_174818_b(blockPos);
                    if (b >= 169.0) {
                        continue;
                    }
                    final double d = calculateDamage(blockPos.func_177958_n() + 0.5, blockPos.func_177956_o() + 1, blockPos.func_177952_p() + 0.5, entity2);
                    if (d < this.minDmg.getValue() && ((EntityLivingBase)entity2).func_110143_aJ() + ((EntityLivingBase)entity2).func_110139_bj() > this.facePlace.getValue()) {
                        continue;
                    }
                    if (d <= damage) {
                        continue;
                    }
                    final double self = calculateDamage(blockPos.func_177958_n() + 0.5, blockPos.func_177956_o() + 1, blockPos.func_177952_p() + 0.5, (Entity)AutoCrystal.mc.field_71439_g);
                    if (self > d && d >= ((EntityLivingBase)entity2).func_110143_aJ()) {
                        continue;
                    }
                    if (self - 0.5 > AutoCrystal.mc.field_71439_g.func_110143_aJ()) {
                        continue;
                    }
                    if (self > this.maxSelfDmg.getValue()) {
                        continue;
                    }
                    damage = d;
                    q = blockPos;
                    this.renderEnt = entity2;
                }
            }
            if (damage == 0.5) {
                this.render = null;
                this.renderEnt = null;
                resetRotation();
                return;
            }
            this.render = q;
            if (this.place.getValue()) {
                if (AutoCrystal.mc.field_71439_g == null) {
                    return;
                }
                this.isActive = true;
                if (this.rotate.getValue()) {
                    this.lookAtPacket(q.func_177958_n() + 0.5, q.func_177956_o() - 0.5, q.func_177952_p() + 0.5, (EntityPlayer)AutoCrystal.mc.field_71439_g);
                }
                final RayTraceResult result = AutoCrystal.mc.field_71441_e.func_72933_a(new Vec3d(AutoCrystal.mc.field_71439_g.field_70165_t, AutoCrystal.mc.field_71439_g.field_70163_u + AutoCrystal.mc.field_71439_g.func_70047_e(), AutoCrystal.mc.field_71439_g.field_70161_v), new Vec3d(q.func_177958_n() + 0.5, q.func_177956_o() - 0.5, q.func_177952_p() + 0.5));
                if (this.raytrace.getValue()) {
                    if (result == null || result.field_178784_b == null) {
                        q = null;
                        this.f = null;
                        this.render = null;
                        resetRotation();
                        this.isActive = false;
                        return;
                    }
                    this.f = result.field_178784_b;
                }
                if (!offhand && AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c != crystalSlot) {
                    if (this.autoSwitch.getValue()) {
                        if (this.noGappleSwitch.getValue() && this.isEatingGap()) {
                            this.isActive = false;
                            resetRotation();
                            return;
                        }
                        this.isActive = true;
                        AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c = crystalSlot;
                        resetRotation();
                        this.switchCooldown = true;
                    }
                    return;
                }
                if (this.switchCooldown) {
                    this.switchCooldown = false;
                    return;
                }
                if (q != null && AutoCrystal.mc.field_71439_g != null) {
                    this.isActive = true;
                    if (this.raytrace.getValue() && this.f != null) {
                        AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(q, this.f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                    }
                    else {
                        AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(q, EnumFacing.UP, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                    }
                }
                this.isActive = false;
            }
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        final Color c = new Color(255, 0, 0, 255);
        if (this.render != null && AutoCrystal.mc.field_71439_g != null) {
            KamiTessellator.prepare(7);
            this.drawCurrentBlock(this.render, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
            KamiTessellator.release();
        }
    }
    
    private boolean isEatingGap() {
        return AutoCrystal.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemAppleGold && AutoCrystal.mc.field_71439_g.func_184587_cr();
    }
    
    private void drawCurrentBlock(final BlockPos render, final int r, final int g, final int b, final int a) {
        KamiTessellator.drawBox(render, r, g, b, a, 63);
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.func_177982_a(0, 1, 0);
        final BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
        return (AutoCrystal.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150357_h || AutoCrystal.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150343_Z) && AutoCrystal.mc.field_71441_e.func_180495_p(boost).func_177230_c() == Blocks.field_150350_a && AutoCrystal.mc.field_71441_e.func_180495_p(boost2).func_177230_c() == Blocks.field_150350_a && AutoCrystal.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && AutoCrystal.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(AutoCrystal.mc.field_71439_g.field_70165_t), Math.floor(AutoCrystal.mc.field_71439_g.field_70163_u), Math.floor(AutoCrystal.mc.field_71439_g.field_70161_v));
    }
    
    private List<BlockPos> findCrystalBlocks() {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.func_191196_a();
        final double range = this.placeRange.getValue();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), (float)range, (int)range, false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)positions;
    }
    
    public List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final List<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = loc.func_177958_n();
        final int cy = loc.func_177956_o();
        final int cz = loc.func_177952_p();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int)r) : cy; y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }
    
    public static float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.func_70011_f(posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.field_70170_p.func_72842_a(vec3d, entity.func_174813_aQ());
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)AutoCrystal.mc.field_71441_e, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }
    
    public static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.func_94539_a(explosion);
            damage = CombatRules.func_189427_a(damage, (float)ep.func_70658_aO(), (float)ep.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
            final int k = EnchantmentHelper.func_77508_a(ep.func_184193_aE(), ds);
            final float f = MathHelper.func_76131_a((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.func_70644_a(Potion.func_188412_a(11))) {
                damage -= damage / 4.0f;
            }
            return damage;
        }
        damage = CombatRules.func_189427_a(damage, (float)entity.func_70658_aO(), (float)entity.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
        return damage;
    }
    
    private static float getDamageMultiplied(final float damage) {
        final int diff = AutoCrystal.mc.field_71441_e.func_175659_aa().func_151525_a();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public static float calculateDamage(final EntityEnderCrystal crystal, final Entity entity) {
        return calculateDamage(crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v, entity);
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        AutoCrystal.yaw = yaw1;
        AutoCrystal.pitch = pitch1;
        AutoCrystal.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (AutoCrystal.isSpoofingAngles) {
            AutoCrystal.yaw = AutoCrystal.mc.field_71439_g.field_70177_z;
            AutoCrystal.pitch = AutoCrystal.mc.field_71439_g.field_70125_A;
            AutoCrystal.isSpoofingAngles = false;
        }
    }
    
    public static double[] calculateLookAt(final double px, final double py, final double pz, final EntityPlayer me) {
        double dirx = me.field_70165_t - px;
        double diry = me.field_70163_u - py;
        double dirz = me.field_70161_v - pz;
        final double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        dirx /= len;
        diry /= len;
        dirz /= len;
        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);
        pitch = pitch * 180.0 / 3.141592653589793;
        yaw = yaw * 180.0 / 3.141592653589793;
        yaw += 90.0;
        return new double[] { yaw, pitch };
    }
    
    public void onEnable() {
        AtomMod.EVENT_BUS.subscribe(this);
        this.isActive = false;
        if (this.chat.getValue() && AutoCrystal.mc.field_71439_g != null) {
            Command.sendChatMessage("AutoCrystal toggled §2on");
        }
    }
    
    public void onDisable() {
        AtomMod.EVENT_BUS.unsubscribe(this);
        this.render = null;
        this.renderEnt = null;
        resetRotation();
        this.isActive = false;
        if (this.chat.getValue()) {
            Command.sendChatMessage("AutoCrystal toggled §4off");
        }
    }
}
