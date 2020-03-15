// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.command.commands;

import me.nucleus.atom.module.Module;
import me.nucleus.atom.module.ModuleManager;
import me.nucleus.atom.command.syntax.SyntaxParser;
import me.nucleus.atom.command.syntax.parsers.ModuleParser;
import me.nucleus.atom.command.syntax.ChunkBuilder;
import me.nucleus.atom.command.Command;

public class ToggleCommand extends Command
{
    public ToggleCommand() {
        super("toggle", new ChunkBuilder().append("module", true, new ModuleParser()).build(), new String[] { "t" });
        this.setDescription("Quickly toggle a module on and off");
    }
    
    @Override
    public void call(final String[] args) {
        if (args.length == 0) {
            Command.sendChatMessage("Please specify a module!");
            return;
        }
        final Module m = ModuleManager.getModuleByName(args[0]);
        if (m == null) {
            Command.sendChatMessage("Unknown module '" + args[0] + "'");
            return;
        }
        m.toggle();
        Command.sendChatMessage(m.getName() + (m.isEnabled() ? " &aenabled" : " &cdisabled"));
    }
}
