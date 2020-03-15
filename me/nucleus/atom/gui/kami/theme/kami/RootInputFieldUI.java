// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.gui.kami.theme.kami;

import me.nucleus.atom.gui.rgui.component.Component;
import me.nucleus.atom.gui.rgui.component.container.Container;
import me.nucleus.atom.gui.kami.RenderHelper;
import org.lwjgl.opengl.GL11;
import me.nucleus.atom.gui.rgui.render.font.FontRenderer;
import me.nucleus.atom.gui.rgui.render.AbstractComponentUI;
import me.nucleus.atom.gui.rgui.component.use.InputField;

public class RootInputFieldUI<T extends InputField> extends AbstractComponentUI<InputField>
{
    @Override
    public void renderComponent(final InputField component, final FontRenderer fontRenderer) {
        GL11.glColor3f(0.33f, 0.22f, 0.22f);
        RenderHelper.drawFilledRectangle(0.0f, 0.0f, (float)component.getWidth(), (float)component.getHeight());
        GL11.glLineWidth(1.5f);
        GL11.glColor4f(1.0f, 0.33f, 0.33f, 0.6f);
        RenderHelper.drawRectangle(0.0f, 0.0f, (float)component.getWidth(), (float)component.getHeight());
    }
    
    @Override
    public void handleAddComponent(final InputField component, final Container container) {
        component.setWidth(200);
        component.setHeight(component.getTheme().getFontRenderer().getFontHeight());
    }
}
