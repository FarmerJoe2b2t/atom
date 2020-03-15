// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.hidden;

import me.nucleus.atom.util.Friends;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.network.NetworkPlayerInfo;
import me.nucleus.atom.setting.Settings;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "TabFriends", description = "Highlights friends in the tab menu", category = Category.HIDDEN, showOnArray = ShowOnArray.OFF)
public class TabFriends extends Module
{
    public Setting<Boolean> startupGlobal;
    public static TabFriends INSTANCE;
    
    public TabFriends() {
        this.startupGlobal = this.register(Settings.b("Enable Automatically", true));
        TabFriends.INSTANCE = this;
    }
    
    public static String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        final String dname = (networkPlayerInfoIn.func_178854_k() != null) ? networkPlayerInfoIn.func_178854_k().func_150254_d() : ScorePlayerTeam.func_96667_a((Team)networkPlayerInfoIn.func_178850_i(), networkPlayerInfoIn.func_178845_a().getName());
        if (Friends.isFriend(dname)) {
            return String.format("%sa%s", '§', dname);
        }
        return dname;
    }
}
