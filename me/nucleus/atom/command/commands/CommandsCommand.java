// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.command.commands;

import java.util.Comparator;
import me.nucleus.atom.AtomMod;
import me.nucleus.atom.command.syntax.SyntaxChunk;
import me.nucleus.atom.command.Command;

public class CommandsCommand extends Command
{
    public CommandsCommand() {
        super("commands", SyntaxChunk.EMPTY, new String[] { "cmd", "cmds" });
        this.setDescription("Gives you this list of commands");
    }
    
    @Override
    public void call(final String[] args) {
        AtomMod.getInstance().getCommandManager().getCommands().stream().sorted(Comparator.comparing(command -> command.getLabel())).forEach(command -> Command.sendChatMessage("&7" + Command.getCommandPrefix() + command.getLabel() + "&r ~ &8" + command.getDescription()));
    }
}
