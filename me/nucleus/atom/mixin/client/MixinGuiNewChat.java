// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.mixin.client;

import me.nucleus.atom.module.modules.gui.CleanGUI;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.gui.Gui;
import me.nucleus.atom.module.modules.experimental.GUIColour;
import me.nucleus.atom.module.modules.misc.Chat;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiNewChat.class })
public abstract class MixinGuiNewChat
{
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    private void drawRectBackgroundClean(final int left, final int top, final int right, final int bottom, final int color) {
        if (Chat.INSTANCE.isEnabled() && Chat.changeChatBackground.getValue()) {
            Gui.func_73734_a(left, top, right, bottom, GUIColour.getChatBackgroundColor());
        }
        else {
            Gui.func_73734_a(left, top, right, bottom, color);
        }
    }
    
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int drawStringWithShadowClean(final FontRenderer fontRenderer, final String text, final float x, final float y, final int color) {
        if (!CleanGUI.enabled() || (CleanGUI.enabled() && !CleanGUI.chatGlobal.getValue())) {
            return fontRenderer.func_175063_a(text, x, y, color);
        }
        return fontRenderer.func_78276_b(text, (int)x, (int)y, color);
    }
}
