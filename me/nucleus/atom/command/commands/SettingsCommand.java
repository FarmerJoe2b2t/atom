// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.command.commands;

import java.util.List;
import me.nucleus.atom.module.Module;
import me.nucleus.atom.setting.impl.EnumSetting;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.ModuleManager;
import me.nucleus.atom.command.syntax.SyntaxParser;
import me.nucleus.atom.command.syntax.parsers.ModuleParser;
import me.nucleus.atom.command.syntax.ChunkBuilder;
import me.nucleus.atom.command.Command;

public class SettingsCommand extends Command
{
    public SettingsCommand() {
        super("settings", new ChunkBuilder().append("module", true, new ModuleParser()).build(), new String[0]);
        this.setDescription("List the possible settings of a command");
    }
    
    @Override
    public void call(final String[] args) {
        if (args[0] == null) {
            Command.sendChatMessage("Please specify a module to display the settings of.");
            return;
        }
        final Module m = ModuleManager.getModuleByName(args[0]);
        if (m == null) {
            Command.sendChatMessage("Couldn't find a module &b" + args[0] + "!");
            return;
        }
        final List<Setting> settings = m.settingList;
        final String[] result = new String[settings.size()];
        for (int i = 0; i < settings.size(); ++i) {
            final Setting setting = settings.get(i);
            result[i] = "&b" + setting.getName() + "&3(=" + setting.getValue() + ")  &ftype: &3" + setting.getValue().getClass().getSimpleName();
            if (setting instanceof EnumSetting) {
                final StringBuilder sb = new StringBuilder();
                final String[] array = result;
                final int n = i;
                array[n] = sb.append(array[n]).append("  (").toString();
                final Enum[] array2;
                final Enum[] enums = array2 = (Enum[])((EnumSetting)setting).clazz.getEnumConstants();
                for (final Enum e : array2) {
                    final StringBuilder sb2 = new StringBuilder();
                    final String[] array3 = result;
                    final int n2 = i;
                    array3[n2] = sb2.append(array3[n2]).append(e.name()).append(", ").toString();
                }
                result[i] = result[i].substring(0, result[i].length() - 2) + ")";
            }
        }
        Command.sendStringChatMessage(result);
    }
}
