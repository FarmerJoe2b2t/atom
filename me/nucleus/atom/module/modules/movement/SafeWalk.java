// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.movement;

import me.nucleus.atom.module.Module;

@Info(name = "SafeWalk", category = Category.MOVEMENT, description = "Keeps you from walking off edges")
public class SafeWalk extends Module
{
    private static SafeWalk INSTANCE;
    
    public SafeWalk() {
        SafeWalk.INSTANCE = this;
    }
    
    public static boolean shouldSafewalk() {
        return SafeWalk.INSTANCE.isEnabled();
    }
}
