// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.hidden;

import net.minecraft.entity.Entity;
import java.util.function.Predicate;
import me.nucleus.atom.util.Friends;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import me.zero.alpine.listener.EventHandler;
import me.nucleus.atom.event.events.ClientPlayerAttackEvent;
import me.zero.alpine.listener.Listener;
import me.nucleus.atom.module.Module;

@Info(name = "AntiFriendHit", description = "Don't hit your friends", category = Category.HIDDEN, alwaysListening = true)
public class AntiFriendHit extends Module
{
    @EventHandler
    Listener<ClientPlayerAttackEvent> listener;
    
    public AntiFriendHit() {
        Entity e;
        this.listener = new Listener<ClientPlayerAttackEvent>(event -> {
            if (!(!this.isEnabled())) {
                e = AntiFriendHit.mc.field_71476_x.field_72308_g;
                if (e instanceof EntityOtherPlayerMP && Friends.isFriend(e.func_70005_c_())) {
                    event.cancel();
                }
            }
        }, (Predicate<ClientPlayerAttackEvent>[])new Predicate[0]);
    }
}
