// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.hidden;

import me.nucleus.atom.module.Module;

@Info(name = "AntiChunkLoadPatch", category = Category.HIDDEN, description = "Prevents loading of overloaded chunks", showOnArray = ShowOnArray.OFF)
public class AntiChunkLoadPatch extends Module
{
    private static AntiChunkLoadPatch INSTANCE;
    
    public AntiChunkLoadPatch() {
        AntiChunkLoadPatch.INSTANCE = this;
    }
    
    public static boolean enabled() {
        return AntiChunkLoadPatch.INSTANCE.isEnabled();
    }
    
    static {
        AntiChunkLoadPatch.INSTANCE = new AntiChunkLoadPatch();
    }
}
