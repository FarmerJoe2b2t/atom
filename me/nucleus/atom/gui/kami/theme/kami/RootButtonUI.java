// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.gui.kami.theme.kami;

import me.nucleus.atom.gui.rgui.component.Component;
import me.nucleus.atom.gui.rgui.component.container.Container;
import me.nucleus.atom.gui.kami.KamiGUI;
import me.nucleus.atom.gui.kami.RenderHelper;
import org.lwjgl.opengl.GL11;
import me.nucleus.atom.gui.rgui.render.font.FontRenderer;
import java.awt.Color;
import me.nucleus.atom.gui.rgui.render.AbstractComponentUI;
import me.nucleus.atom.gui.rgui.component.use.Button;

public class RootButtonUI<T extends Button> extends AbstractComponentUI<Button>
{
    protected Color idleColour;
    protected Color downColour;
    
    public RootButtonUI() {
        this.idleColour = new Color(163, 163, 163);
        this.downColour = new Color(255, 255, 255);
    }
    
    @Override
    public void renderComponent(final Button component, final FontRenderer ff) {
        GL11.glColor3f(0.22f, 0.22f, 0.22f);
        if (component.isHovered() || component.isPressed()) {
            GL11.glColor3f(0.26f, 0.26f, 0.26f);
        }
        RenderHelper.drawRoundedRectangle(0.0f, 0.0f, (float)component.getWidth(), (float)component.getHeight(), 3.0f);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        KamiGUI.fontRenderer.drawString(component.getWidth() / 2 - KamiGUI.fontRenderer.getStringWidth(component.getName()) / 2, 0, component.isPressed() ? this.downColour : this.idleColour, component.getName());
        GL11.glDisable(3553);
        GL11.glDisable(3042);
    }
    
    @Override
    public void handleAddComponent(final Button component, final Container container) {
        component.setWidth(KamiGUI.fontRenderer.getStringWidth(component.getName()) + 28);
        component.setHeight(KamiGUI.fontRenderer.getFontHeight() + 2);
    }
}
