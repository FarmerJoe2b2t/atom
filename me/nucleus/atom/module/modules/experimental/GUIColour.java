// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.experimental;

import me.nucleus.atom.setting.Settings;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "GUI Colour", description = "Change GUI Colours", category = Category.EXPERIMENTAL)
public class GUIColour extends Module
{
    public Setting<Integer> arrayListRed;
    public Setting<Integer> arrayListGreen;
    public Setting<Integer> arrayListBlue;
    public static Setting<Integer> chatBGRed;
    public static Setting<Integer> chatBGGreen;
    public static Setting<Integer> chatBGBlue;
    public static Setting<Integer> chatBGAlpha;
    public static Setting<Boolean> disableGlow;
    public static Setting<Boolean> changeGlowColor;
    public static Setting<Integer> glowRed;
    public static Setting<Integer> glowGreen;
    public static Setting<Integer> glowBlue;
    public static GUIColour INSTANCE;
    
    public GUIColour() {
        this.arrayListRed = this.register((Setting<Integer>)Settings.integerBuilder("ArrayListRed").withMinimum(0).withValue(255).withMaximum(255).build());
        this.arrayListGreen = this.register((Setting<Integer>)Settings.integerBuilder("ArrayListGreen").withMinimum(0).withValue(0).withMaximum(255).build());
        this.arrayListBlue = this.register((Setting<Integer>)Settings.integerBuilder("ArrayListBlue").withMinimum(0).withValue(0).withMaximum(255).build());
        (GUIColour.INSTANCE = this).register(GUIColour.chatBGRed);
        this.register(GUIColour.chatBGGreen);
        this.register(GUIColour.chatBGBlue);
        this.register(GUIColour.chatBGAlpha);
        this.register(GUIColour.disableGlow);
        this.register(GUIColour.changeGlowColor);
        this.register(GUIColour.glowRed);
        this.register(GUIColour.glowGreen);
        this.register(GUIColour.glowBlue);
    }
    
    public static int getChatBackgroundColor() {
        if (!GUIColour.INSTANCE.isEnabled()) {
            return -16777216;
        }
        final int r = GUIColour.chatBGRed.getValue() & 0xFF;
        final int g = GUIColour.chatBGGreen.getValue() & 0xFF;
        final int b = GUIColour.chatBGBlue.getValue() & 0xFF;
        final int a = GUIColour.chatBGAlpha.getValue() & 0xFF;
        return a << 24 | r << 16 | g << 8 | b;
    }
    
    public static int getGlowColor() {
        final int r = GUIColour.glowRed.getValue() & 0xFF;
        final int g = GUIColour.glowGreen.getValue() & 0xFF;
        final int b = GUIColour.glowBlue.getValue() & 0xFF;
        final int a = 255;
        return a << 24 | r << 16 | g << 8 | b;
    }
    
    static {
        GUIColour.chatBGRed = (Setting<Integer>)Settings.integerBuilder("ChatBGRed").withMinimum(0).withValue(0).withMaximum(255).build();
        GUIColour.chatBGGreen = (Setting<Integer>)Settings.integerBuilder("ChatBGGreen").withMinimum(0).withValue(0).withMaximum(255).build();
        GUIColour.chatBGBlue = (Setting<Integer>)Settings.integerBuilder("ChatBGBlue").withMinimum(0).withValue(0).withMaximum(255).build();
        GUIColour.chatBGAlpha = (Setting<Integer>)Settings.integerBuilder("ChatBGAlpha").withMinimum(0).withValue(255).withMaximum(255).build();
        GUIColour.disableGlow = Settings.b("DisableGlow", false);
        GUIColour.changeGlowColor = Settings.b("ChangeGlowColor", true);
        GUIColour.glowRed = (Setting<Integer>)Settings.integerBuilder("GlowRed").withMinimum(0).withValue(255).withMaximum(255).build();
        GUIColour.glowGreen = (Setting<Integer>)Settings.integerBuilder("GlowGreen").withMinimum(0).withValue(0).withMaximum(255).build();
        GUIColour.glowBlue = (Setting<Integer>)Settings.integerBuilder("GlowBlue").withMinimum(0).withValue(0).withMaximum(255).build();
        GUIColour.INSTANCE = new GUIColour();
    }
}
