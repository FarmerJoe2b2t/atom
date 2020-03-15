// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.command.commands;

import me.nucleus.atom.setting.builder.SettingBuilder;
import me.nucleus.atom.setting.Settings;
import me.nucleus.atom.module.Module;
import me.nucleus.atom.util.Wrapper;
import me.nucleus.atom.module.ModuleManager;
import me.nucleus.atom.command.syntax.SyntaxParser;
import me.nucleus.atom.command.syntax.parsers.ModuleParser;
import me.nucleus.atom.command.syntax.ChunkBuilder;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.command.Command;

public class BindCommand extends Command
{
    public static Setting<Boolean> modifiersEnabled;
    
    public BindCommand() {
        super("bind", new ChunkBuilder().append("[module]|modifiers", true, new ModuleParser()).append("[key]|[on|off]", true).build(), new String[0]);
        this.setDescription("Binds a command and or settings to a key");
    }
    
    @Override
    public void call(final String[] args) {
        if (args.length == 1) {
            Command.sendChatMessage("Please specify a module.");
            return;
        }
        final String module = args[0];
        final String rkey = args[1];
        if (module.equalsIgnoreCase("modifiers")) {
            if (rkey == null) {
                Command.sendChatMessage("Expected: on or off");
                return;
            }
            if (rkey.equalsIgnoreCase("on")) {
                BindCommand.modifiersEnabled.setValue(true);
                Command.sendChatMessage("Turned modifiers on.");
            }
            else if (rkey.equalsIgnoreCase("off")) {
                BindCommand.modifiersEnabled.setValue(false);
                Command.sendChatMessage("Turned modifiers off.");
            }
            else {
                Command.sendChatMessage("Expected: on or off");
            }
        }
        else {
            final Module m = ModuleManager.getModuleByName(module);
            if (m == null) {
                Command.sendChatMessage("Unknown module '" + module + "'!");
                return;
            }
            if (rkey == null) {
                Command.sendChatMessage(m.getName() + " is bound to &b" + m.getBindName());
                return;
            }
            int key = Wrapper.getKey(rkey);
            if (rkey.equalsIgnoreCase("none")) {
                key = -1;
            }
            if (key == 0) {
                Command.sendChatMessage("Unknown key '" + rkey + "'!");
                return;
            }
            m.getBind().setKey(key);
            Command.sendChatMessage("Bind for &b" + m.getName() + "&r set to &b" + rkey.toUpperCase());
        }
    }
    
    static {
        BindCommand.modifiersEnabled = SettingBuilder.register(Settings.b("modifiersEnabled", false), "binds");
    }
}
