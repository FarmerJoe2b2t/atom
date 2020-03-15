// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.misc;

import me.nucleus.atom.DiscordPresence;
import me.nucleus.atom.setting.Settings;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "DiscordRPC", category = Category.MISC, description = "Discord Rich Presence")
public class DiscordSettings extends Module
{
    public Setting<Boolean> startupGlobal;
    public Setting<LineInfo> info;
    
    public DiscordSettings() {
        this.startupGlobal = this.register(Settings.b("Enable Automatically", true));
        this.info = this.register(Settings.e("Info", LineInfo.HELD_ITEM));
    }
    
    public String getLine(final LineInfo line) {
        return "";
    }
    
    public void onEnable() {
        DiscordPresence.start();
    }
    
    public enum LineInfo
    {
        HELD_ITEM, 
        HEALTH, 
        SPEED;
    }
}
