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

public class RenameModuleCommand extends Command
{
    public RenameModuleCommand() {
        super("renamemodule", new ChunkBuilder().append("module", true, new ModuleParser()).append("name").build(), new String[0]);
        this.setDescription("Rename a module to something else");
    }
    
    @Override
    public void call(final String[] args) {
        if (args.length == 0) {
            Command.sendChatMessage("Please specify a module!");
            return;
        }
        final Module module = ModuleManager.getModuleByName(args[0]);
        if (module == null) {
            Command.sendChatMessage("Unknown module '" + args[0] + "'!");
            return;
        }
        final String name = (args.length == 1) ? module.getOriginalName() : args[1];
        if (!name.matches("[a-zA-Z]+")) {
            Command.sendChatMessage("Name must be alphabetic!");
            return;
        }
        Command.sendChatMessage("&b" + module.getName() + "&r renamed to &b" + name);
        module.setName(name);
    }
}
