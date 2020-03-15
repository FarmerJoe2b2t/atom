// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.World;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ AbstractClientPlayer.class })
public class MixinAbstractClientPlayer
{
    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    public void AbstractClientPlayer(final World worldIn, final GameProfile playerProfile, final CallbackInfo callbackInfo) {
    }
}
