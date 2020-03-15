// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.gui;

import me.nucleus.atom.setting.Settings;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "CleanGUI", category = Category.GUI, showOnArray = ShowOnArray.OFF)
public class CleanGUI extends Module
{
    public Setting<Boolean> inventoryGlobal;
    public static Setting<Boolean> chatGlobal;
    private static CleanGUI INSTANCE;
    
    public CleanGUI() {
        this.inventoryGlobal = this.register(Settings.b("Inventory", true));
        (CleanGUI.INSTANCE = this).register(CleanGUI.chatGlobal);
    }
    
    public static boolean enabled() {
        return CleanGUI.INSTANCE.isEnabled();
    }
    
    static {
        CleanGUI.chatGlobal = Settings.b("Chat", true);
        CleanGUI.INSTANCE = new CleanGUI();
    }
}
