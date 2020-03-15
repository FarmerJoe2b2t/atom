// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.gui.rgui.component.container.use;

import me.nucleus.atom.gui.rgui.render.theme.Theme;
import me.nucleus.atom.gui.rgui.component.container.AbstractContainer;

public class Groupbox extends AbstractContainer
{
    String name;
    
    public Groupbox(final Theme theme, final String name) {
        super(theme);
        this.name = name;
    }
    
    public Groupbox(final Theme theme, final String name, final int x, final int y) {
        this(theme, name);
        this.setX(x);
        this.setY(y);
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}
