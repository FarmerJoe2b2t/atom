// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.gui.kami.theme.kami;

import me.nucleus.atom.gui.rgui.component.Component;
import org.lwjgl.opengl.GL11;
import me.nucleus.atom.gui.rgui.component.AlignedComponent;
import me.nucleus.atom.gui.rgui.render.font.FontRenderer;
import me.nucleus.atom.gui.rgui.render.AbstractComponentUI;
import me.nucleus.atom.gui.rgui.component.use.Label;

public class RootLabelUI<T extends Label> extends AbstractComponentUI<Label>
{
    @Override
    public void renderComponent(final Label component, FontRenderer a) {
        a = component.getFontRenderer();
        final String[] lines = component.getLines();
        int y = 0;
        final boolean shadow = component.isShadow();
        for (final String s : lines) {
            int x = 0;
            if (component.getAlignment() == AlignedComponent.Alignment.CENTER) {
                x = component.getWidth() / 2 - a.getStringWidth(s) / 2;
            }
            else if (component.getAlignment() == AlignedComponent.Alignment.RIGHT) {
                x = component.getWidth() - a.getStringWidth(s);
            }
            if (shadow) {
                a.drawStringWithShadow(x, y, 255, 255, 255, s);
            }
            else {
                a.drawString(x, y, s);
            }
            y += a.getFontHeight() + 3;
        }
        GL11.glDisable(3553);
        GL11.glDisable(3042);
    }
    
    @Override
    public void handleSizeComponent(final Label component) {
        final String[] lines = component.getLines();
        int y = 0;
        int w = 0;
        for (final String s : lines) {
            w = Math.max(w, component.getFontRenderer().getStringWidth(s));
            y += component.getFontRenderer().getFontHeight() + 3;
        }
        component.setWidth(w);
        component.setHeight(y);
    }
}
