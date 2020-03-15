// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.nucleus.atom.command.Command;
import java.util.Collection;
import net.minecraft.client.network.NetworkPlayerInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import me.nucleus.atom.util.Files;
import me.nucleus.atom.setting.Settings;
import java.util.List;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "Announcer", category = Category.MISC)
public class Announcer extends Module
{
    private Setting<Boolean> joinMsg;
    private Setting<Boolean> leaveMsg;
    private Setting<Boolean> clientSideOnly;
    private Setting<Double> messageDelaySecs;
    private List<String> prevPlayers;
    private long lastMessageTicks;
    private String JOIN_MESSAGES_FILE;
    private String LEAVE_MESSAGES_FILE;
    
    public Announcer() {
        this.joinMsg = this.register(Settings.b("Join", true));
        this.leaveMsg = this.register(Settings.b("Leave", true));
        this.clientSideOnly = this.register(Settings.b("ClientSideOnly", true));
        this.messageDelaySecs = this.register(Settings.d("DelaySeconds", 5.0));
        this.JOIN_MESSAGES_FILE = "AtomJoinMessages.txt";
        this.LEAVE_MESSAGES_FILE = "AtomLeaveMessages.txt";
    }
    
    public void onEnable() {
        this.prevPlayers = this.getCurrentPlayers();
        this.lastMessageTicks = System.currentTimeMillis();
    }
    
    private String getJoinMessage(final String name) {
        final String[] messages = Files.readFileAllLines(this.JOIN_MESSAGES_FILE);
        if (messages == null) {
            Files.writeFile(this.JOIN_MESSAGES_FILE, new String[] { "Hello, $", "Greetings, $" });
            return "Hello " + name;
        }
        if (messages.length == 0) {
            return "Hello " + name;
        }
        final String obfuscatedName = name.charAt(0) + "." + name.substring(1);
        final String msg = messages[new Random().nextInt(messages.length)];
        return msg.replace("$", obfuscatedName);
    }
    
    private String getLeaveMessage(final String name) {
        final String[] messages = Files.readFileAllLines(this.LEAVE_MESSAGES_FILE);
        if (messages == null) {
            Files.writeFile(this.LEAVE_MESSAGES_FILE, new String[] { "Goodbye, $", "Farewell, $" });
            return "Goodbye " + name;
        }
        if (messages.length == 0) {
            return "Goodbye " + name;
        }
        final String msg = messages[new Random().nextInt(messages.length)];
        return msg.replace("$", name);
    }
    
    @Override
    public void onUpdate() {
        final List<String> currentPlayers = this.getCurrentPlayers();
        if (currentPlayers == null) {
            this.prevPlayers = null;
            return;
        }
        if (this.prevPlayers == null) {
            this.prevPlayers = this.getCurrentPlayers();
        }
        final boolean msg = false;
        for (final String prevPlayer : this.prevPlayers) {
            if (!currentPlayers.contains(prevPlayer) && this.leaveMsg.getValue()) {
                this.writeChat(this.getLeaveMessage(prevPlayer));
            }
        }
        for (final String currentPlayer : currentPlayers) {
            if (!this.prevPlayers.contains(currentPlayer) && this.joinMsg.getValue()) {
                this.writeChat(this.getJoinMessage(currentPlayer));
            }
        }
        this.prevPlayers = currentPlayers;
    }
    
    private List<String> getCurrentPlayers() {
        if (Announcer.mc.func_147114_u() == null) {
            return null;
        }
        final Collection<NetworkPlayerInfo> playerInfos = (Collection<NetworkPlayerInfo>)Announcer.mc.func_147114_u().func_175106_d();
        if (playerInfos == null) {
            return null;
        }
        final ArrayList<String> ret = new ArrayList<String>();
        final Iterator<NetworkPlayerInfo> it = playerInfos.iterator();
        while (it.hasNext()) {
            ret.add(it.next().func_178845_a().getName());
        }
        return ret;
    }
    
    private void writeChat(String msg) {
        if (msg.length() == 0) {
            return;
        }
        msg = msg.replace("\n", "");
        msg = msg.replace("\r", "");
        final long now = System.currentTimeMillis();
        final long delayMillis = (long)(this.messageDelaySecs.getValue() * 1000.0);
        if (now - this.lastMessageTicks >= delayMillis) {
            if (this.clientSideOnly.getValue()) {
                Command.sendChatMessage(msg);
            }
            else {
                Announcer.mc.field_71442_b.field_78774_b.func_147297_a((Packet)new CPacketChatMessage(">" + msg));
            }
            this.lastMessageTicks = now;
        }
    }
}
