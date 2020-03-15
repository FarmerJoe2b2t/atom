// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.player;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.entity.Entity;
import me.nucleus.atom.util.EntityUtil;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketPlayer;
import me.nucleus.atom.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.nucleus.atom.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(category = Category.PLAYER, description = "Prevents fall damage", name = "NoFall")
public class NoFall extends Module
{
    private Setting<FallMode> fallMode;
    private Setting<Boolean> pickup;
    private Setting<Integer> distance;
    private long last;
    @EventHandler
    public Listener<PacketEvent.Send> sendListener;
    
    public NoFall() {
        this.fallMode = this.register(Settings.e("Mode", FallMode.PACKET));
        this.pickup = this.register(Settings.b("Pickup", true));
        this.distance = this.register(Settings.i("Distance", 3));
        this.last = 0L;
        this.sendListener = new Listener<PacketEvent.Send>(event -> {
            if (this.fallMode.getValue().equals(FallMode.PACKET) && event.getPacket() instanceof CPacketPlayer) {
                ((CPacketPlayer)event.getPacket()).field_149474_g = true;
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (this.fallMode.getValue().equals(FallMode.BUCKET) && NoFall.mc.field_71439_g.field_70143_R >= this.distance.getValue() && !EntityUtil.isAboveWater((Entity)NoFall.mc.field_71439_g) && System.currentTimeMillis() - this.last > 100L) {
            Vec3d posVec = NoFall.mc.field_71439_g.func_174791_d();
            RayTraceResult result = NoFall.mc.field_71441_e.func_147447_a(posVec, posVec.func_72441_c(0.0, -5.329999923706055, 0.0), true, true, false);
            if (result != null && result.field_72313_a == RayTraceResult.Type.BLOCK) {
                EnumHand hand = EnumHand.MAIN_HAND;
                if (NoFall.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151131_as) {
                    hand = EnumHand.OFF_HAND;
                }
                else if (NoFall.mc.field_71439_g.func_184614_ca().func_77973_b() != Items.field_151131_as) {
                    for (int i = 0; i < 9; ++i) {
                        if (NoFall.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == Items.field_151131_as) {
                            NoFall.mc.field_71439_g.field_71071_by.field_70461_c = i;
                            NoFall.mc.field_71439_g.field_70125_A = 90.0f;
                            this.last = System.currentTimeMillis();
                            return;
                        }
                    }
                    return;
                }
                NoFall.mc.field_71439_g.field_70125_A = 90.0f;
                NoFall.mc.field_71442_b.func_187101_a((EntityPlayer)NoFall.mc.field_71439_g, (World)NoFall.mc.field_71441_e, hand);
                true;
            }
            System.out.println("KAMI BLUE: Ran this");
            if (this.pickup.getValue()) {
                System.out.println("KAMI BLUE: Ran this");
                posVec = NoFall.mc.field_71439_g.func_174791_d();
                result = NoFall.mc.field_71441_e.func_147447_a(posVec, posVec.func_72441_c(0.0, -1.5, 0.0), false, true, false);
                if (result != null && result.field_72313_a != RayTraceResult.Type.BLOCK) {
                    System.out.println("KAMI BLUE: Ran this");
                    EnumHand hand = EnumHand.MAIN_HAND;
                    if (NoFall.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151133_ar) {
                        hand = EnumHand.OFF_HAND;
                    }
                    else if (NoFall.mc.field_71439_g.func_184614_ca().func_77973_b() != Items.field_151133_ar) {
                        for (int iN = 0; iN < 9; ++iN) {
                            if (NoFall.mc.field_71439_g.field_71071_by.func_70301_a(iN).func_77973_b() == Items.field_151133_ar) {
                                NoFall.mc.field_71439_g.field_71071_by.field_70461_c = iN;
                                NoFall.mc.field_71439_g.field_70125_A = 90.0f;
                                this.last = System.currentTimeMillis();
                                return;
                            }
                        }
                        return;
                    }
                    NoFall.mc.field_71439_g.field_70125_A = 90.0f;
                    NoFall.mc.field_71442_b.func_187101_a((EntityPlayer)NoFall.mc.field_71439_g, (World)NoFall.mc.field_71441_e, hand);
                    false;
                }
            }
        }
    }
    
    private enum FallMode
    {
        BUCKET, 
        PACKET;
    }
}
