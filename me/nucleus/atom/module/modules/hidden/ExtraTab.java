// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.hidden;

import me.nucleus.atom.setting.Settings;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "ExtraTab", description = "Expands the player tab menu", category = Category.HIDDEN)
public class ExtraTab extends Module
{
    public Setting<Integer> tabSize;
    public static ExtraTab INSTANCE;
    
    public ExtraTab() {
        this.tabSize = this.register((Setting<Integer>)Settings.integerBuilder("Players").withMinimum(1).withValue(80).build());
        ExtraTab.INSTANCE = this;
    }
}
