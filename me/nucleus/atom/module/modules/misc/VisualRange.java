// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.misc;

import me.nucleus.atom.command.Command;
import java.util.Iterator;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.nucleus.atom.util.Friends;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import java.util.ArrayList;
import me.nucleus.atom.module.ModuleManager;
import me.nucleus.atom.setting.Settings;
import java.util.List;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "VisualRange", description = "Shows players who enter and leave range in chat", category = Category.MISC)
public class VisualRange extends Module
{
    private Setting<Boolean> leaving;
    private List<String> knownPlayers;
    
    public VisualRange() {
        this.leaving = this.register(Settings.b("Leaving", false));
    }
    
    @Override
    public void onUpdate() {
        if (VisualRange.mc.field_71439_g == null) {
            return;
        }
        final TelegramNotifications telegram = (TelegramNotifications)ModuleManager.getModuleByNameUnsafe("TelegramNotifications");
        final List<String> tickPlayerList = new ArrayList<String>();
        for (final Entity entity : VisualRange.mc.field_71441_e.func_72910_y()) {
            if (entity instanceof EntityPlayer) {
                tickPlayerList.add(entity.func_70005_c_());
            }
        }
        if (tickPlayerList.size() > 0) {
            for (final String playerName : tickPlayerList) {
                if (playerName.equals(VisualRange.mc.field_71439_g.func_70005_c_())) {
                    continue;
                }
                if (!this.knownPlayers.contains(playerName)) {
                    this.knownPlayers.add(playerName);
                    if (Friends.isFriend(playerName)) {
                        this.sendNotification(ChatFormatting.GREEN.toString() + playerName + ChatFormatting.RESET.toString() + " entered the Battlefield!");
                    }
                    else {
                        this.sendNotification(ChatFormatting.RED.toString() + playerName + ChatFormatting.RESET.toString() + " entered the Battlefield!");
                    }
                    if (telegram != null && telegram.isEnabled() && telegram.visualRange.getValue()) {
                        telegram.notify(playerName + " entered visual range");
                    }
                    return;
                }
            }
        }
        if (this.knownPlayers.size() > 0) {
            for (final String playerName : this.knownPlayers) {
                if (!tickPlayerList.contains(playerName)) {
                    this.knownPlayers.remove(playerName);
                    if (this.leaving.getValue()) {
                        if (Friends.isFriend(playerName)) {
                            this.sendNotification(ChatFormatting.GREEN.toString() + playerName + ChatFormatting.RESET.toString() + " left the Battlefield!");
                        }
                        else {
                            this.sendNotification(ChatFormatting.RED.toString() + playerName + ChatFormatting.RESET.toString() + " left the Battlefield!");
                        }
                        if (telegram != null && telegram.isEnabled() && telegram.visualRange.getValue()) {
                            telegram.notify(playerName + " left visual range");
                        }
                    }
                }
            }
        }
    }
    
    private void sendNotification(final String s) {
        Command.sendChatMessage(s);
    }
    
    public void onEnable() {
        this.knownPlayers = new ArrayList<String>();
    }
}
