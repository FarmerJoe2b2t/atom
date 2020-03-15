// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.hidden;

import me.nucleus.atom.module.Module;

@Info(name = "Strafe", description = "Automatically makes the player sprint", category = Category.HIDDEN, showOnArray = ShowOnArray.OFF)
public class Strafe extends Module
{
    @Override
    public void onUpdate() {
        try {
            if (!Strafe.mc.field_71439_g.field_70123_F && Strafe.mc.field_71439_g.field_191988_bg > 0.0f) {
                Strafe.mc.field_71439_g.func_70031_b(true);
            }
            else {
                Strafe.mc.field_71439_g.func_70031_b(false);
            }
        }
        catch (Exception ex) {}
    }
}
