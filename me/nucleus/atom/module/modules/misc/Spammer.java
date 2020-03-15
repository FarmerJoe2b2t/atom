// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import java.util.Random;
import me.nucleus.atom.command.Command;
import me.nucleus.atom.util.Files;
import me.nucleus.atom.setting.Settings;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "Spammer", category = Category.MISC)
public class Spammer extends Module
{
    private Setting<String> file;
    private Setting<Mode> mode;
    private Setting<Double> delaySeconds;
    private Setting<Boolean> greentext;
    private Setting<Boolean> clientSideOnly;
    private int currentLine;
    private String[] lines;
    private long lastTime;
    
    public Spammer() {
        this.file = this.register(Settings.stringBuilder("File").withValue("AtomSpam.txt").build());
        this.mode = this.register(Settings.e("Mode", Mode.IN_ORDER));
        this.delaySeconds = this.register(Settings.d("DelaySeconds", 5.0));
        this.greentext = this.register(Settings.b("Greentext", false));
        this.clientSideOnly = this.register(Settings.b("ClientSideOnly", true));
    }
    
    public void onEnable() {
        final String path = this.file.getValue();
        this.lines = Files.readFileAllLines(path);
        if (this.lines == null) {
            Command.sendErrorMessage("Cannot read file: " + path + ". Disabling Spammer.");
            this.disable();
            return;
        }
        if (this.lines.length == 0) {
            Command.sendErrorMessage("File empty: " + path + ". Disabling Spammer.");
            this.disable();
            return;
        }
        this.currentLine = 0;
        this.lastTime = 0L;
    }
    
    @Override
    public void onUpdate() {
        final long now = System.currentTimeMillis();
        final long delayMillis = (long)(this.delaySeconds.getValue() * 1000.0);
        if (now - this.lastTime >= delayMillis) {
            this.lastTime = now;
            switch (this.mode.getValue()) {
                case RANDOM: {
                    this.writeChat(this.lines[new Random().nextInt(this.lines.length)]);
                    break;
                }
                case IN_ORDER: {
                    this.writeChat(this.lines[this.currentLine]);
                    ++this.currentLine;
                    if (this.currentLine >= this.lines.length) {
                        Command.sendChatMessagePermanent("Spammer reached end of file. Restarting from beginning.");
                        this.currentLine = 0;
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    private void writeChat(final String msg) {
        if (this.clientSideOnly.getValue()) {
            Command.sendChatMessage(msg);
        }
        else {
            String chatMsg = "";
            if (this.greentext.getValue()) {
                chatMsg += ">";
            }
            chatMsg += msg;
            Spammer.mc.field_71442_b.field_78774_b.func_147297_a((Packet)new CPacketChatMessage(chatMsg));
        }
    }
    
    @Override
    public String getHudInfo() {
        if (this.mode.getValue() == Mode.IN_ORDER) {
            return "" + this.currentLine + "/" + this.lines.length;
        }
        return "";
    }
    
    public enum Mode
    {
        RANDOM, 
        IN_ORDER;
    }
}
