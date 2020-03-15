// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.mixin.client;

import org.spongepowered.asm.mixin.injection.Inject;
import me.nucleus.atom.AtomMod;
import me.nucleus.atom.event.events.PlayerMoveEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.MoverType;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityPlayerSP.class })
public class MixinEntityPlayerSP
{
    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;closeScreen()V"))
    public void closeScreen(final EntityPlayerSP entityPlayerSP) {
    }
    
    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"))
    public void closeScreen(final Minecraft minecraft, final GuiScreen screen) {
    }
    
    @Inject(method = { "move" }, at = { @At("HEAD") }, cancellable = true)
    public void move(final MoverType type, final double x, final double y, final double z, final CallbackInfo info) {
        final PlayerMoveEvent event = new PlayerMoveEvent(type, x, y, z);
        AtomMod.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }
}
