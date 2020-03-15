// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.command.syntax.parsers;

import me.nucleus.atom.module.ModuleManager;
import me.nucleus.atom.module.Module;
import me.nucleus.atom.command.syntax.SyntaxChunk;

public class ModuleParser extends AbstractParser
{
    @Override
    public String getChunk(final SyntaxChunk[] chunks, final SyntaxChunk thisChunk, final String[] values, final String chunkValue) {
        if (chunkValue == null) {
            return this.getDefaultChunk(thisChunk);
        }
        final Module chosen = ModuleManager.getModules().stream().filter(module -> module.getName().toLowerCase().startsWith(chunkValue.toLowerCase())).findFirst().orElse(null);
        if (chosen == null) {
            return null;
        }
        return chosen.getName().substring(chunkValue.length());
    }
}
