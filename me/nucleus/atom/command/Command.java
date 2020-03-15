// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.text.TextComponentBase;
import me.nucleus.atom.setting.Settings;
import net.minecraft.launchwrapper.LogWrapper;
import net.minecraft.util.text.ITextComponent;
import me.nucleus.atom.util.Wrapper;
import java.util.Collection;
import java.util.Arrays;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.command.syntax.SyntaxChunk;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;

public abstract class Command
{
    protected String label;
    protected String syntax;
    protected String description;
    protected ArrayList<String> aliases;
    public final Minecraft mc;
    protected SyntaxChunk[] syntaxChunks;
    public static Setting<String> commandPrefix;
    
    public Command(final String label, final SyntaxChunk[] syntaxChunks, final ArrayList<String> aliases) {
        this.mc = Minecraft.func_71410_x();
        this.label = label;
        this.syntaxChunks = syntaxChunks;
        this.description = "Descriptionless";
        this.aliases = aliases;
    }
    
    public Command(final String label, final SyntaxChunk[] syntaxChunks, final String... aliases) {
        this.mc = Minecraft.func_71410_x();
        this.label = label;
        this.syntaxChunks = syntaxChunks;
        this.description = "Descriptionless";
        this.aliases = new ArrayList<String>(Arrays.asList(aliases));
    }
    
    public static void sendChatMessagePermanent(final String message) {
        sendRawChatMessagePermanent("&d[ATOM] &r&7" + message);
    }
    
    public static void sendChatMessage(final String message) {
        sendRawChatMessage("&d[ATOM] &r&7" + message);
    }
    
    public static void sendErrorMessage(final String message) {
        sendRawChatMessage("&d[ATOM] &r&7" + message);
    }
    
    public static void sendWarningMessage(final String message) {
        sendRawChatMessage("&d[ATOM] &r&7" + message);
    }
    
    public static void sendStringChatMessage(final String[] messages) {
        sendChatMessage("");
        for (final String s : messages) {
            sendRawChatMessage(s);
        }
    }
    
    public static void sendRawChatMessage(final String message) {
        if (isSendable()) {
            if (Wrapper.getMinecraft().field_71456_v != null) {
                Wrapper.getMinecraft().field_71456_v.func_146158_b().func_146234_a((ITextComponent)new ChatMessage(message), 42069);
            }
        }
        else {
            LogWrapper.info("KAMI Blue: Avoided NPE by logging to file instead of chat\n" + message, new Object[0]);
        }
    }
    
    public static void sendRawChatMessagePermanent(final String message) {
        if (isSendable()) {
            if (Wrapper.getMinecraft().field_71456_v != null) {
                Wrapper.getMinecraft().field_71456_v.func_146158_b().func_146227_a((ITextComponent)new ChatMessage(message));
            }
        }
        else {
            LogWrapper.info("KAMI Blue: Avoided NPE by logging to file instead of chat\n" + message, new Object[0]);
        }
    }
    
    public static boolean isSendable() {
        return Minecraft.func_71410_x().field_71439_g != null;
    }
    
    protected void setDescription(final String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public static String getCommandPrefix() {
        return Command.commandPrefix.getValue();
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public ArrayList<String> getAliases() {
        return this.aliases;
    }
    
    public abstract void call(final String[] p0);
    
    public SyntaxChunk[] getSyntaxChunks() {
        return this.syntaxChunks;
    }
    
    protected SyntaxChunk getSyntaxChunk(final String name) {
        for (final SyntaxChunk c : this.syntaxChunks) {
            if (c.getType().equals(name)) {
                return c;
            }
        }
        return null;
    }
    
    static {
        Command.commandPrefix = Settings.s("commandPrefix", "*");
    }
    
    public static class ChatMessage extends TextComponentBase
    {
        String text;
        
        public ChatMessage(final String text) {
            final Pattern p = Pattern.compile("&[0123456789abcdefrlosmk]");
            final Matcher m = p.matcher(text);
            final StringBuffer sb = new StringBuffer();
            while (m.find()) {
                final String replacement = "§" + m.group().substring(1);
                m.appendReplacement(sb, replacement);
            }
            m.appendTail(sb);
            this.text = sb.toString();
        }
        
        public String func_150261_e() {
            return this.text;
        }
        
        public ITextComponent func_150259_f() {
            return (ITextComponent)new ChatMessage(this.text);
        }
    }
}
