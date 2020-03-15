// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.command.commands;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import java.util.ArrayList;
import net.minecraft.item.ItemWritableBook;
import me.nucleus.atom.util.Wrapper;
import me.nucleus.atom.command.syntax.ChunkBuilder;
import me.nucleus.atom.command.Command;

public class SignBookCommand extends Command
{
    public SignBookCommand() {
        super("signbook", new ChunkBuilder().append("name").build(), new String[] { "book", "sign" });
        this.setDescription("Colored book names. #n for a new line and & for colour codes");
    }
    
    @Override
    public void call(final String[] args) {
        final ItemStack is = Wrapper.getPlayer().field_71071_by.func_70448_g();
        final int c = 167;
        if (args.length == 1) {
            Command.sendChatMessage("Please specify a title.");
            return;
        }
        if (is.func_77973_b() instanceof ItemWritableBook) {
            final ArrayList<String> toAdd = new ArrayList<String>();
            for (int i = 0; i < args.length; ++i) {
                toAdd.add(args[i]);
            }
            String futureTitle = String.join(" ", toAdd);
            futureTitle = futureTitle.replaceAll("&", Character.toString((char)c));
            futureTitle = futureTitle.replaceAll("#n", "\n");
            futureTitle = futureTitle.replaceAll("null", "");
            if (futureTitle.length() > 31) {
                Command.sendChatMessage("Title cannot be over 31 characters.");
                return;
            }
            final NBTTagList pages = new NBTTagList();
            final String pageText = "";
            pages.func_74742_a((NBTBase)new NBTTagString(pageText));
            final NBTTagCompound bookData = is.func_77978_p();
            if (is.func_77942_o()) {
                if (bookData != null) {
                    is.func_77982_d(bookData);
                }
                is.func_77978_p().func_74782_a("title", (NBTBase)new NBTTagString(futureTitle));
                is.func_77978_p().func_74782_a("author", (NBTBase)new NBTTagString(Wrapper.getPlayer().func_70005_c_()));
            }
            else {
                is.func_77983_a("pages", (NBTBase)pages);
                is.func_77983_a("title", (NBTBase)new NBTTagString(futureTitle));
                is.func_77983_a("author", (NBTBase)new NBTTagString(Wrapper.getPlayer().func_70005_c_()));
            }
            final PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
            buf.func_150788_a(is);
            Wrapper.getPlayer().field_71174_a.func_147297_a((Packet)new CPacketCustomPayload("MC|BSign", buf));
            Command.sendChatMessage("Signed book with title: " + futureTitle + "&r");
        }
        else {
            Command.sendChatMessage("You must be holding a writable book.");
        }
    }
}
