// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.gui.kami.theme.kami;

import me.nucleus.atom.gui.rgui.component.Component;
import me.nucleus.atom.gui.rgui.component.container.Container;
import me.nucleus.atom.gui.kami.RenderHelper;
import org.lwjgl.opengl.GL11;
import me.nucleus.atom.gui.rgui.render.font.FontRenderer;
import me.nucleus.atom.gui.kami.RootSmallFontRenderer;
import me.nucleus.atom.gui.rgui.component.use.Slider;
import me.nucleus.atom.gui.rgui.render.AbstractComponentUI;

public class RootSliderUI extends AbstractComponentUI<Slider>
{
    RootSmallFontRenderer smallFontRenderer;
    
    public RootSliderUI() {
        this.smallFontRenderer = new RootSmallFontRenderer();
    }
    
    @Override
    public void renderComponent(final Slider component, final FontRenderer aa) {
        GL11.glColor4f(1.0f, 0.33f, 0.33f, component.getOpacity());
        GL11.glLineWidth(2.5f);
        final int height = component.getHeight();
        final double value = component.getValue();
        double w = component.getWidth() * ((value - component.getMinimum()) / (component.getMaximum() - component.getMinimum()));
        final float downscale = 1.1f;
        GL11.glBegin(1);
        GL11.glVertex2d(0.0, (double)(height / downscale));
        GL11.glVertex2d(w, (double)(height / downscale));
        GL11.glColor3f(0.33f, 0.33f, 0.33f);
        GL11.glVertex2d(w, (double)(height / downscale));
        GL11.glVertex2d((double)component.getWidth(), (double)(height / downscale));
        GL11.glEnd();
        GL11.glColor3f(1.0f, 0.33f, 0.33f);
        RenderHelper.drawCircle((float)(int)w, height / downscale, 2.0f);
        final String s = value + "";
        if (component.isPressed()) {
            w -= this.smallFontRenderer.getStringWidth(s) / 2;
            w = Math.max(0.0, Math.min(w, component.getWidth() - this.smallFontRenderer.getStringWidth(s)));
            this.smallFontRenderer.drawString((int)w, 0, s);
        }
        else {
            this.smallFontRenderer.drawString(0, 0, component.getText());
            this.smallFontRenderer.drawString(component.getWidth() - this.smallFontRenderer.getStringWidth(s), 0, s);
        }
        GL11.glDisable(3553);
    }
    
    @Override
    public void handleAddComponent(final Slider component, final Container container) {
        component.setHeight(component.getTheme().getFontRenderer().getFontHeight() + 2);
        component.setWidth(this.smallFontRenderer.getStringWidth(component.getText()) + this.smallFontRenderer.getStringWidth(component.getMaximum() + "") + 3);
    }
}
