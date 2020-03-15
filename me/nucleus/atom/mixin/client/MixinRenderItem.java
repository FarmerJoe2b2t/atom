// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.mixin.client;

import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import me.nucleus.atom.module.modules.experimental.GUIColour;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderItem.class })
public class MixinRenderItem
{
    private ItemStack lastItemRendered;
    
    public MixinRenderItem() {
        this.lastItemRendered = ItemStack.field_190927_a;
    }
    
    @Inject(method = { "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V" }, at = { @At("HEAD") })
    private void onItemRendered(final ItemStack stack, final IBakedModel model, final CallbackInfo callback) {
        this.lastItemRendered = stack;
    }
    
    @Inject(method = { "renderEffect(Lnet/minecraft/client/renderer/block/model/IBakedModel;)V" }, at = { @At("HEAD") }, cancellable = true)
    private void disableEffect(final IBakedModel model, final CallbackInfo callback) {
        if (GUIColour.INSTANCE.isEnabled() && GUIColour.disableGlow.getValue()) {
            callback.cancel();
        }
    }
    
    @ModifyConstant(method = { "renderEffect(Lnet/minecraft/client/renderer/block/model/IBakedModel;)V" }, constant = { @Constant(intValue = -8372020) })
    private int modifyGlowColor(final int originalColor) {
        if (GUIColour.INSTANCE.isEnabled() && GUIColour.changeGlowColor.getValue()) {
            return GUIColour.getGlowColor();
        }
        return originalColor;
    }
}
