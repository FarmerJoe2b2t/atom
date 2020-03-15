// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.gui.kami.theme.kami;

import me.nucleus.atom.gui.rgui.render.AbstractComponentUI;
import me.nucleus.atom.gui.kami.KamiGUI;
import me.nucleus.atom.gui.kami.theme.staticui.TabGuiUI;
import me.nucleus.atom.gui.kami.theme.staticui.RadarUI;
import me.nucleus.atom.gui.rgui.render.ComponentUI;
import me.nucleus.atom.gui.rgui.render.font.FontRenderer;
import me.nucleus.atom.gui.rgui.render.theme.AbstractTheme;

public class KamiTheme extends AbstractTheme
{
    FontRenderer fontRenderer;
    
    public KamiTheme() {
        this.installUI(new RootButtonUI<Object>());
        this.installUI(new GUIUI());
        this.installUI(new RootGroupboxUI());
        this.installUI(new KamiFrameUI<Object>());
        this.installUI(new RootScrollpaneUI());
        this.installUI(new RootInputFieldUI<Object>());
        this.installUI(new RootLabelUI<Object>());
        this.installUI(new RootChatUI());
        this.installUI(new RootCheckButtonUI<Object>());
        this.installUI(new KamiActiveModulesUI());
        this.installUI(new KamiSettingsPanelUI());
        this.installUI(new RootSliderUI());
        this.installUI(new KamiEnumbuttonUI());
        this.installUI(new RootColorizedCheckButtonUI());
        this.installUI(new KamiUnboundSliderUI());
        this.installUI(new RadarUI());
        this.installUI(new TabGuiUI());
        this.fontRenderer = KamiGUI.fontRenderer;
    }
    
    @Override
    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
    
    public class GUIUI extends AbstractComponentUI<KamiGUI>
    {
    }
}
