// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.gui.kami.theme.kami;

import me.nucleus.atom.gui.rgui.component.Component;
import me.nucleus.atom.gui.rgui.component.container.Container;
import me.nucleus.atom.gui.kami.KamiGUI;
import org.lwjgl.opengl.GL11;
import me.nucleus.atom.gui.rgui.render.font.FontRenderer;
import java.awt.Color;
import me.nucleus.atom.gui.rgui.render.AbstractComponentUI;
import me.nucleus.atom.gui.rgui.component.use.CheckButton;

public class RootCheckButtonUI<T extends CheckButton> extends AbstractComponentUI<CheckButton>
{
    protected Color backgroundColour;
    protected Color backgroundColourHover;
    protected Color idleColourNormal;
    protected Color downColourNormal;
    protected Color idleColourToggle;
    protected Color downColourToggle;
    
    public RootCheckButtonUI() {
        this.backgroundColour = new Color(200, 56, 56);
        this.backgroundColourHover = new Color(255, 66, 66);
        this.idleColourNormal = new Color(200, 200, 200);
        this.downColourNormal = new Color(190, 190, 190);
        this.idleColourToggle = new Color(250, 120, 120);
        this.downColourToggle = this.idleColourToggle.brighter();
    }
    
    @Override
    public void renderComponent(final CheckButton component, final FontRenderer ff) {
        GL11.glColor4f(this.backgroundColour.getRed() / 255.0f, this.backgroundColour.getGreen() / 255.0f, this.backgroundColour.getBlue() / 255.0f, component.getOpacity());
        if (component.isToggled()) {
            GL11.glColor3f(0.9f, this.backgroundColour.getGreen() / 255.0f, this.backgroundColour.getBlue() / 255.0f);
        }
        if (component.isHovered() || component.isPressed()) {
            GL11.glColor4f(this.backgroundColourHover.getRed() / 255.0f, this.backgroundColourHover.getGreen() / 255.0f, this.backgroundColourHover.getBlue() / 255.0f, component.getOpacity());
        }
        final String text = component.getName();
        int c = component.isPressed() ? 11184810 : (component.isToggled() ? 16724787 : 14540253);
        if (component.isHovered()) {
            c = (c & 0x7F7F7F) << 1;
        }
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        KamiGUI.fontRenderer.drawString(2, KamiGUI.fontRenderer.getFontHeight() / 2 - 2, c, text);
        GL11.glDisable(3553);
        GL11.glDisable(3042);
    }
    
    @Override
    public void handleAddComponent(final CheckButton component, final Container container) {
        component.setWidth(KamiGUI.fontRenderer.getStringWidth(component.getName()) + 28);
        component.setHeight(KamiGUI.fontRenderer.getFontHeight() + 2);
    }
}
