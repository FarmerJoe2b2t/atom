// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.gui.rgui.render.theme;

import me.nucleus.atom.gui.rgui.render.font.FontRenderer;
import me.nucleus.atom.gui.rgui.render.ComponentUI;
import me.nucleus.atom.gui.rgui.component.Component;

public interface Theme
{
    ComponentUI getUIForComponent(final Component p0);
    
    FontRenderer getFontRenderer();
}
