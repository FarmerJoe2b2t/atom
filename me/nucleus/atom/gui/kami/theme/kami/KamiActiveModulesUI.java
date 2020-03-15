// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.gui.kami.theme.kami;

import me.nucleus.atom.gui.rgui.component.Component;
import java.util.function.Function;
import java.awt.Color;
import me.nucleus.atom.module.modules.experimental.GUIColour;
import me.nucleus.atom.gui.rgui.component.AlignedComponent;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import me.nucleus.atom.module.ModuleManager;
import me.nucleus.atom.module.Module;
import java.util.List;
import me.nucleus.atom.util.Wrapper;
import org.lwjgl.opengl.GL11;
import me.nucleus.atom.gui.rgui.render.font.FontRenderer;
import me.nucleus.atom.gui.kami.component.ActiveModules;
import me.nucleus.atom.gui.rgui.render.AbstractComponentUI;

public class KamiActiveModulesUI extends AbstractComponentUI<ActiveModules>
{
    @Override
    public void renderComponent(final ActiveModules component, final FontRenderer f) {
        GL11.glDisable(2884);
        GL11.glEnable(3042);
        GL11.glEnable(3553);
        final FontRenderer renderer = Wrapper.getFontRenderer();
        String string;
        final FontRenderer fontRenderer;
        final StringBuilder sb;
        final List<Module> mods = ModuleManager.getModules().stream().filter(Module::isEnabled).sorted(Comparator.comparing(module -> {
            new StringBuilder().append(module.getName());
            if (module.getHudInfo() == null) {
                string = "";
            }
            else {
                string = module.getHudInfo() + " ";
            }
            return Integer.valueOf(fontRenderer.getStringWidth(sb.append(string).toString()) * (component.sort_up ? -1 : 1));
        })).collect((Collector<? super Object, ?, List<Module>>)Collectors.toList());
        final int[] y = { 2 };
        if (component.getParent().getY() < 26 && Wrapper.getPlayer().func_70651_bq().size() > 0 && component.getParent().getOpacity() == 0.0f) {
            y[0] = Math.max(component.getParent().getY(), 26 - component.getParent().getY());
        }
        final float[] hue = { System.currentTimeMillis() % 11520L / 11520.0f };
        final boolean lAlign = component.getAlignment() == AlignedComponent.Alignment.LEFT;
        switch (component.getAlignment()) {
            case RIGHT: {
                final Function<Integer, Integer> xFunc = (Function<Integer, Integer>)(i -> component.getWidth() - i);
                break;
            }
            case CENTER: {
                final Function<Integer, Integer> xFunc = (Function<Integer, Integer>)(i -> component.getWidth() / 2 - i / 2);
                break;
            }
            default: {
                final Function<Integer, Integer> xFunc = (Function<Integer, Integer>)(i -> 0);
                break;
            }
        }
        final GUIColour gui = (GUIColour)ModuleManager.getModuleByNameUnsafe("GUI Colour");
        final Object o;
        int rgb;
        String s;
        String string2;
        final StringBuilder sb2;
        String text;
        final FontRenderer fontRenderer2;
        int textwidth;
        int textheight;
        int red;
        int green;
        int blue;
        boolean useRainbow;
        final GUIColour guiColour;
        final Function<Integer, Integer> function;
        final Object o2;
        final int n;
        final int n2;
        mods.stream().forEach(module -> {
            if (module.getShowOnArray().equals(Module.ShowOnArray.ON)) {
                rgb = Color.HSBtoRGB(o[0], 1.0f, 1.0f);
                s = module.getHudInfo();
                new StringBuilder().append(module.getName());
                if (s == null) {
                    string2 = "";
                }
                else {
                    string2 = " §7" + s;
                }
                text = sb2.append(string2).toString();
                textwidth = fontRenderer2.getStringWidth(text);
                textheight = fontRenderer2.getFontHeight() + 1;
                red = (rgb >> 16 & 0xFF);
                green = (rgb >> 8 & 0xFF);
                blue = (rgb & 0xFF);
                useRainbow = false;
                if (guiColour == null) {
                    useRainbow = true;
                }
                else if (!guiColour.isEnabled()) {
                    useRainbow = true;
                }
                if (useRainbow) {
                    fontRenderer2.drawStringWithShadow((int)function.apply(textwidth), o2[0], red, green, blue, text);
                }
                else {
                    fontRenderer2.drawStringWithShadow((int)function.apply(textwidth), o2[0], (int)guiColour.arrayListRed.getValue(), (int)guiColour.arrayListGreen.getValue(), (int)guiColour.arrayListBlue.getValue(), text);
                }
                o[n] += 0.02f;
                o2[n2] += textheight;
            }
            return;
        });
        component.setHeight(y[0]);
        GL11.glEnable(2884);
        GL11.glDisable(3042);
    }
    
    @Override
    public void handleSizeComponent(final ActiveModules component) {
        component.setWidth(100);
        component.setHeight(100);
    }
}
