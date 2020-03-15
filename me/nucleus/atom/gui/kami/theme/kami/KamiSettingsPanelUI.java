// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.gui.kami.theme.kami;

import me.nucleus.atom.gui.rgui.component.Component;
import me.nucleus.atom.gui.kami.RenderHelper;
import me.nucleus.atom.module.ModuleManager;
import me.nucleus.atom.module.modules.experimental.GUIColour;
import org.lwjgl.opengl.GL11;
import me.nucleus.atom.gui.rgui.render.font.FontRenderer;
import me.nucleus.atom.gui.kami.component.SettingsPanel;
import me.nucleus.atom.gui.rgui.render.AbstractComponentUI;

public class KamiSettingsPanelUI extends AbstractComponentUI<SettingsPanel>
{
    @Override
    public void renderComponent(final SettingsPanel component, final FontRenderer fontRenderer) {
        super.renderComponent(component, fontRenderer);
        GL11.glLineWidth(2.0f);
        final float red = ((GUIColour)ModuleManager.getModuleByName("GUI Colour")).arrayListRed.getValue() / 255.0f;
        final float green = ((GUIColour)ModuleManager.getModuleByName("GUI Colour")).arrayListGreen.getValue() / 255.0f;
        final float blue = ((GUIColour)ModuleManager.getModuleByName("GUI Colour")).arrayListBlue.getValue() / 255.0f;
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.8f);
        RenderHelper.drawFilledRectangle(0.0f, 0.0f, (float)component.getWidth(), (float)component.getHeight());
        GL11.glColor3f(0.59f, 0.05f, 0.11f);
        GL11.glLineWidth(1.5f);
        RenderHelper.drawRectangle(0.0f, 0.0f, (float)component.getWidth(), (float)component.getHeight());
    }
}
