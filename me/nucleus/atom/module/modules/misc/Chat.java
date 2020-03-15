// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.misc;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.play.client.CPacketChatMessage;
import java.util.function.Predicate;
import me.nucleus.atom.command.Command;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import net.minecraft.network.play.server.SPacketChat;
import me.nucleus.atom.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.nucleus.atom.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "Chat", category = Category.MISC, description = "Chat ending", showOnArray = ShowOnArray.OFF)
public class Chat extends Module
{
    public Setting<Boolean> startupGlobal;
    public static Setting<Boolean> changeChatBackground;
    private Setting<Boolean> addSuffix;
    public Setting<Boolean> addTimestamp;
    private Setting<TextMode> textMode;
    private Setting<DecoMode> decoMode;
    private Setting<Boolean> commands;
    public static Chat INSTANCE;
    @EventHandler
    public Listener<PacketEvent.Receive> receiveListener;
    @EventHandler
    public Listener<PacketEvent.Send> listener;
    
    public Chat() {
        this.startupGlobal = this.register(Settings.b("Enable Automatically", true));
        this.addSuffix = this.register(Settings.b("Suffix", false));
        this.addTimestamp = this.register(Settings.b("Timestamp", true));
        this.textMode = this.register(Settings.e("Content", TextMode.NAME));
        this.decoMode = this.register(Settings.e("Punctuation", DecoMode.CLASSIC));
        this.commands = this.register(Settings.b("Commands", false));
        Calendar calendar;
        SimpleDateFormat formatter;
        String timestampString;
        SPacketChat packet;
        Command.ChatMessage newMessage;
        this.receiveListener = new Listener<PacketEvent.Receive>(event -> {
            if (this.addTimestamp.getValue()) {
                if (!(!(event.getPacket() instanceof SPacketChat))) {
                    calendar = Calendar.getInstance();
                    formatter = new SimpleDateFormat("HH:mm");
                    timestampString = "&7[" + formatter.format(calendar.getTime()) + "]&r ";
                    packet = (SPacketChat)event.getPacket();
                    newMessage = new Command.ChatMessage(timestampString);
                    newMessage.func_150257_a(packet.func_148915_c());
                    packet.field_148919_a = (ITextComponent)newMessage;
                }
            }
            return;
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
        String s;
        String s2;
        this.listener = new Listener<PacketEvent.Send>(event -> {
            if (!this.addSuffix.getValue()) {
                return;
            }
            else {
                if (event.getPacket() instanceof CPacketChatMessage) {
                    s = ((CPacketChatMessage)event.getPacket()).func_149439_c();
                    if (!this.commands.getValue()) {
                        if (s.startsWith("/")) {
                            return;
                        }
                        else if (s.startsWith(",")) {
                            return;
                        }
                        else if (s.startsWith(".")) {
                            return;
                        }
                        else if (s.startsWith("-")) {
                            return;
                        }
                        else if (s.startsWith(";")) {
                            return;
                        }
                        else if (s.startsWith("?")) {
                            return;
                        }
                        else if (s.startsWith("*")) {
                            return;
                        }
                        else if (s.startsWith("^")) {
                            return;
                        }
                        else if (s.startsWith("&")) {
                            return;
                        }
                    }
                    s2 = s + this.getFull(this.decoMode.getValue());
                    if (s2.length() >= 256) {
                        s2 = s2.substring(0, 256);
                    }
                    ((CPacketChatMessage)event.getPacket()).field_149440_a = s2;
                }
                return;
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
        (Chat.INSTANCE = this).register(Chat.changeChatBackground);
    }
    
    private String getText(final TextMode t) {
        switch (t) {
            case NAME: {
                return "\u1d00\u1d1b\u1d0f\u1d0d";
            }
            case ONTOP: {
                return "\u1d00\u1d1b\u1d0f\u1d0d \u1d0f\u0274 \u1d1b\u1d0f\u1d18";
            }
            default: {
                return "";
            }
        }
    }
    
    private String getFull(final DecoMode d) {
        switch (d) {
            case NONE: {
                return " " + this.getText(this.textMode.getValue());
            }
            case CLASSIC: {
                return " « " + this.getText(this.textMode.getValue()) + " " + '»';
            }
            case SEPARATOR: {
                return " \u23d0 " + this.getText(this.textMode.getValue());
            }
            default: {
                return "";
            }
        }
    }
    
    static {
        Chat.changeChatBackground = Settings.b("Chat Background", true);
        Chat.INSTANCE = new Chat();
    }
    
    private enum TextMode
    {
        NAME, 
        ONTOP;
    }
    
    private enum DecoMode
    {
        SEPARATOR, 
        CLASSIC, 
        NONE;
    }
}
