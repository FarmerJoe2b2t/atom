// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.misc;

import java.util.Iterator;
import java.util.List;
import me.nucleus.atom.command.Command;
import net.minecraft.entity.player.EntityPlayer;
import me.nucleus.atom.setting.Settings;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import java.util.HashMap;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "CoordLogger", category = Category.MISC, description = "Logs teleport coordinates to a file")
public class CoordLogger extends Module
{
    private Setting<Boolean> tpExploit;
    private HashMap<Entity, Vec3d> knownPlayers;
    
    public CoordLogger() {
        this.tpExploit = this.register(Settings.b("TP Exploit", true));
        this.knownPlayers = new HashMap<Entity, Vec3d>();
    }
    
    public void onEnable() {
    }
    
    @Override
    public void onUpdate() {
        if (!this.tpExploit.getValue()) {
            return;
        }
        final List<Entity> tickEntityList = (List<Entity>)CoordLogger.mc.field_71441_e.field_72996_f;
        if (tickEntityList == null) {
            return;
        }
        for (final Entity e : tickEntityList) {
            if (!(e instanceof EntityPlayer)) {
                continue;
            }
            if (e == CoordLogger.mc.field_71439_g) {
                continue;
            }
            final Vec3d targetPos = new Vec3d(e.field_70165_t, e.field_70163_u, e.field_70161_v);
            if (this.knownPlayers.containsKey(e)) {
                final double dist = Math.abs(CoordLogger.mc.field_71439_g.func_174791_d().func_178788_d(targetPos).func_72433_c());
                if (dist > 128.0 && Math.abs(this.knownPlayers.get(e).func_178788_d(targetPos).func_72433_c()) >= 64.0) {
                    this.knownPlayers.put(e, targetPos);
                    Command.sendChatMessage("[CoordLogger] Player " + e.func_70005_c_() + " moved to position " + targetPos);
                    continue;
                }
            }
            this.knownPlayers.put(e, targetPos);
        }
    }
}
