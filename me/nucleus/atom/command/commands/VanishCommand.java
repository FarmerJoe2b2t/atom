// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.command.commands;

import me.nucleus.atom.command.syntax.SyntaxChunk;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import me.nucleus.atom.command.Command;

public class VanishCommand extends Command
{
    private static Entity vehicle;
    Minecraft mc;
    
    public VanishCommand() {
        super("vanish", null, new String[] { "entitydesync", "edesync", "entityvanish", "evanish", "ev", "van" });
        this.mc = Minecraft.func_71410_x();
        this.setDescription("Allows you to vanish using an entity");
    }
    
    @Override
    public void call(final String[] args) {
        if (this.mc.field_71439_g.func_184187_bx() != null && VanishCommand.vehicle == null) {
            VanishCommand.vehicle = this.mc.field_71439_g.func_184187_bx();
            this.mc.field_71439_g.func_184210_p();
            this.mc.field_71441_e.func_73028_b(VanishCommand.vehicle.func_145782_y());
            Command.sendChatMessage("Vehicle " + VanishCommand.vehicle.func_70005_c_() + " removed.");
        }
        else if (VanishCommand.vehicle != null) {
            VanishCommand.vehicle.field_70128_L = false;
            this.mc.field_71441_e.func_73027_a(VanishCommand.vehicle.func_145782_y(), VanishCommand.vehicle);
            this.mc.field_71439_g.func_184205_a(VanishCommand.vehicle, true);
            Command.sendChatMessage("Vehicle " + VanishCommand.vehicle.func_70005_c_() + " created.");
            VanishCommand.vehicle = null;
        }
        else {
            Command.sendChatMessage("No Vehicle.");
        }
    }
}
