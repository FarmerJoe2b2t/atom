// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.util;

import org.lwjgl.input.Keyboard;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import me.nucleus.atom.gui.kami.KamiGUI;
import me.nucleus.atom.gui.rgui.render.font.FontRenderer;

public class Wrapper
{
    private static FontRenderer fontRenderer;
    
    public static void init() {
        Wrapper.fontRenderer = KamiGUI.fontRenderer;
    }
    
    public static Minecraft getMinecraft() {
        return Minecraft.func_71410_x();
    }
    
    public static EntityPlayerSP getPlayer() {
        return getMinecraft().field_71439_g;
    }
    
    public static World getWorld() {
        return (World)getMinecraft().field_71441_e;
    }
    
    public static int getKey(final String keyname) {
        return Keyboard.getKeyIndex(keyname.toUpperCase());
    }
    
    public static FontRenderer getFontRenderer() {
        return Wrapper.fontRenderer;
    }
}
