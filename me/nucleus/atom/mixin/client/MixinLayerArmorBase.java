// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.mixin.client;

import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.nucleus.atom.module.modules.experimental.GUIColour;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import me.nucleus.atom.module.modules.gui.ArmourHide;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ LayerArmorBase.class })
public abstract class MixinLayerArmorBase
{
    @Shadow
    private boolean field_177193_i;
    private static ItemStack lastArmor;
    
    @Inject(method = { "renderArmorLayer" }, at = { @At("HEAD") }, cancellable = true)
    public void onRenderArmorLayer(final EntityLivingBase entityLivingBaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final EntityEquipmentSlot slotIn, final CallbackInfo ci) {
        if (ArmourHide.INSTANCE.isEnabled()) {
            if (!ArmourHide.INSTANCE.player.getValue() && entityLivingBaseIn instanceof EntityPlayer) {
                if (!ArmourHide.shouldRenderPiece(slotIn)) {
                    ci.cancel();
                }
            }
            else if (!ArmourHide.INSTANCE.armourstand.getValue() && entityLivingBaseIn instanceof EntityArmorStand) {
                if (!ArmourHide.shouldRenderPiece(slotIn)) {
                    ci.cancel();
                }
            }
            else if (!ArmourHide.INSTANCE.mobs.getValue() && entityLivingBaseIn instanceof EntityMob && !ArmourHide.shouldRenderPiece(slotIn)) {
                ci.cancel();
            }
        }
        if (GUIColour.INSTANCE.isEnabled() && GUIColour.disableGlow.getValue()) {
            this.field_177193_i = true;
        }
    }
    
    @Inject(method = { "renderArmorLayer(Lnet/minecraft/entity/EntityLivingBase;FFFFFFFLnet/minecraft/inventory/EntityEquipmentSlot;)V" }, at = { @At(value = "INVOKE_ASSIGN", target = "net.minecraft.entity.EntityLivingBase.getItemStackFromSlot(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lnet/minecraft/item/ItemStack;") }, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onArmorRendered(final EntityLivingBase entityLivingBaseIn, final float arg1, final float arg2, final float arg3, final float arg4, final float arg5, final float arg6, final float arg7, final EntityEquipmentSlot slotIn, final CallbackInfo ci, final ItemStack itemstack) {
        MixinLayerArmorBase.lastArmor = itemstack;
    }
    
    @Inject(method = { "renderEnchantedGlint(Lnet/minecraft/client/renderer/entity/RenderLivingBase;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/model/ModelBase;FFFFFFF)V" }, at = { @At(value = "INVOKE", target = "net.minecraft.client.renderer.GlStateManager.color(FFFF)V", ordinal = 1, shift = At.Shift.AFTER) })
    private static void changeArmorGlowColor(final CallbackInfo callback) {
        if (GUIColour.INSTANCE.isEnabled() && GUIColour.changeGlowColor.getValue()) {
            GlStateManager.func_179131_c(GUIColour.glowRed.getValue() / 255.0f, GUIColour.glowGreen.getValue() / 255.0f, GUIColour.glowBlue.getValue() / 255.0f, 1.0f);
        }
    }
    
    static {
        MixinLayerArmorBase.lastArmor = ItemStack.field_190927_a;
    }
}
