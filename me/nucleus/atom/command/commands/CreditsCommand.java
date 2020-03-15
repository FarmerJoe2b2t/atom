// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.command.commands;

import me.nucleus.atom.command.syntax.SyntaxChunk;
import me.nucleus.atom.command.Command;

public class CreditsCommand extends Command
{
    public CreditsCommand() {
        super("credits", null, new String[] { "creds" });
        this.setDescription("Prints KAMI Blue's authors and contributors");
    }
    
    @Override
    public void call(final String[] args) {
        Command.sendChatMessage("\n&l&9Author: \n&b086\n&l&9Contributors: \n&bBella (S-B99)\n&bhub (blockparole)\n&bQther (d1gress)\n&bSasha (EmotionalLove)\n&bHHSGPA\n&bcats (Cuhnt)\n&b20kdc\n&bVonr\n&bKatatje\n&bDeauthorized\n&bkdb424\n&bElementars\n&bfsck\n&bJamie (jamie27)\n&bTBM\n&bWaizy\n&bcookiedragon234\n&b0x2E (PretendingToCode)");
    }
}
