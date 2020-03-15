// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.command.commands;

import me.nucleus.atom.command.syntax.ChunkBuilder;
import me.nucleus.atom.command.Command;

public class PrefixCommand extends Command
{
    public PrefixCommand() {
        super("prefix", new ChunkBuilder().append("character").build(), new String[0]);
        this.setDescription("Changes the prefix to your new key");
    }
    
    @Override
    public void call(final String[] args) {
        if (args.length <= 0) {
            Command.sendChatMessage("Please specify a new prefix!");
            return;
        }
        if (args[0] != null) {
            Command.commandPrefix.setValue(args[0]);
            Command.sendChatMessage("Prefix set to &b" + Command.commandPrefix.getValue());
        }
        else if (args[0].equals("\\")) {
            Command.sendChatMessage("Error: \"\\\" is not a supported prefix");
        }
        else {
            Command.sendChatMessage("Please specify a new prefix!");
        }
    }
}
