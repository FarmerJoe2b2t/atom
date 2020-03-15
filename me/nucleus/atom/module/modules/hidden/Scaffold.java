// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.hidden;

import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import me.nucleus.atom.util.BlockInteractionHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import me.nucleus.atom.util.Wrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import me.nucleus.atom.util.EntityUtil;
import me.nucleus.atom.module.ModuleManager;
import net.minecraft.util.MovementInput;
import java.util.function.Predicate;
import me.nucleus.atom.setting.builder.SettingBuilder;
import me.nucleus.atom.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.client.event.InputUpdateEvent;
import me.zero.alpine.listener.Listener;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "Scaffold", category = Category.HIDDEN, description = "Places blocks under you")
public class Scaffold extends Module
{
    private Setting<Integer> future;
    private Setting legitBridge;
    private Setting autoPlace;
    boolean shouldSlow;
    private static Scaffold INSTANCE;
    @EventHandler
    private Listener<InputUpdateEvent> eventListener;
    
    public Scaffold() {
        this.future = this.register(Settings.integerBuilder("Ticks").withMinimum(0).withMaximum(60).withValue(2));
        this.legitBridge = this.register(Settings.b("Legit Bridge", false));
        this.autoPlace = this.register(Settings.b("AutoPlace", false));
        this.shouldSlow = false;
        final MovementInput movementInput;
        final MovementInput movementInput2;
        this.eventListener = new Listener<InputUpdateEvent>(event -> {
            if (this.legitBridge.getValue() && this.shouldSlow) {
                event.getMovementInput();
                movementInput.field_78902_a *= 0.2f;
                event.getMovementInput();
                movementInput2.field_192832_b *= 0.2f;
            }
            return;
        }, (Predicate<InputUpdateEvent>[])new Predicate[0]);
        Scaffold.INSTANCE = this;
    }
    
    public static boolean shouldScaffold() {
        return Scaffold.INSTANCE.isEnabled();
    }
    
    @Override
    public void onUpdate() {
        this.shouldSlow = false;
        if (this.isDisabled() || Scaffold.mc.field_71439_g == null || ModuleManager.isModuleEnabled("Freecam")) {
            return;
        }
        Vec3d vec3d = EntityUtil.getInterpolatedPos((Entity)Scaffold.mc.field_71439_g, this.future.getValue());
        if (this.legitBridge.getValue()) {
            vec3d = EntityUtil.getInterpolatedPos((Entity)Scaffold.mc.field_71439_g, 0.0f);
        }
        final BlockPos blockPos = new BlockPos(vec3d).func_177977_b();
        final BlockPos belowBlockPos = blockPos.func_177977_b();
        final BlockPos legitPos = new BlockPos(EntityUtil.getInterpolatedPos((Entity)Scaffold.mc.field_71439_g, 2.0f));
        if (Wrapper.getWorld().func_180495_p(legitPos.func_177977_b()).func_185904_a().func_76222_j() && this.legitBridge.getValue() && Scaffold.mc.field_71439_g.field_70122_E) {
            this.shouldSlow = true;
            Scaffold.mc.field_71439_g.field_71158_b.field_78899_d = true;
            Scaffold.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Scaffold.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
        }
        if (!Wrapper.getWorld().func_180495_p(blockPos).func_185904_a().func_76222_j()) {
            return;
        }
        int newSlot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Wrapper.getPlayer().field_71071_by.func_70301_a(i);
            if (stack != ItemStack.field_190927_a) {
                if (stack.func_77973_b() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
                    if (!BlockInteractionHelper.blackList.contains(block)) {
                        if (!(block instanceof BlockContainer)) {
                            if (Block.func_149634_a(stack.func_77973_b()).func_176223_P().func_185913_b()) {
                                if (!(((ItemBlock)stack.func_77973_b()).func_179223_d() instanceof BlockFalling) || !Wrapper.getWorld().func_180495_p(belowBlockPos).func_185904_a().func_76222_j()) {
                                    newSlot = i;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (newSlot == -1) {
            return;
        }
        final int oldSlot = Wrapper.getPlayer().field_71071_by.field_70461_c;
        Wrapper.getPlayer().field_71071_by.field_70461_c = newSlot;
        if (!BlockInteractionHelper.checkForNeighbours(blockPos)) {
            return;
        }
        if (this.autoPlace.getValue()) {
            BlockInteractionHelper.placeBlockScaffold(blockPos);
        }
        Scaffold.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Scaffold.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        this.shouldSlow = false;
        Wrapper.getPlayer().field_71071_by.field_70461_c = oldSlot;
    }
}
