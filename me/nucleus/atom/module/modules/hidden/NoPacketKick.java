// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.hidden;

import me.nucleus.atom.module.Module;

@Module.Info(name = "NoPacketKick", category = Module.Category.HIDDEN, description = "Prevent large packets from kicking you")
public class NoPacketKick
{
    private static NoPacketKick INSTANCE;
    
    public NoPacketKick() {
        NoPacketKick.INSTANCE = this;
    }
    
    public static boolean isEnabled() {
        final NoPacketKick instance = NoPacketKick.INSTANCE;
        return isEnabled();
    }
}
