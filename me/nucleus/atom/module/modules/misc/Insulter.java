// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.nucleus.atom.command.Command;
import net.minecraft.client.network.NetworkPlayerInfo;
import java.util.Collection;
import java.util.Random;
import me.nucleus.atom.util.Files;
import me.nucleus.atom.setting.builder.SettingBuilder;
import me.nucleus.atom.setting.Settings;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "Insulter", category = Category.MISC)
public class Insulter extends Module
{
    private Setting<Double> delay;
    private Setting<Boolean> clientSideOnly;
    private long lastTime;
    private String INSULTS_FILE;
    
    public Insulter() {
        this.delay = this.register(Settings.doubleBuilder("DelaySeconds").withMinimum(0.0).withValue(30.0).withMaximum(120.0));
        this.clientSideOnly = this.register(Settings.b("ClientSideOnly", true));
        this.INSULTS_FILE = "AtomInsults.txt";
    }
    
    public void onEnable() {
        this.lastTime = System.currentTimeMillis();
    }
    
    @Override
    public void onUpdate() {
        final long time = System.currentTimeMillis();
        final long delayMillis = (long)(this.delay.getValue() * 1000.0);
        if (time - this.lastTime >= delayMillis) {
            this.insult();
            this.lastTime = time;
        }
    }
    
    private String getSingleInsult() {
        final String[] insults = Files.readFileAllLines(this.INSULTS_FILE);
        if (insults == null) {
            final String[] singleInsults = { "$ you're a fucking retard", "Fuck you $", "$ is a bitch", "$ is so EZ LOL", "$ EZ LOG", "Hey $, you're fucking adopted", "Hey $, you're so fucking bad at this game", "Kill yourself $", "Imagine being as retarded as $", "$ is a retarded 12 year old autist", "$ is fucking stupid", "$ is literally cancer", "Don't trust $, he's a fucking scammer", "$ is a fucking scammer nigger" };
            Files.writeFile(this.INSULTS_FILE, singleInsults);
            return this.getSingleInsult();
        }
        if (insults.length == 0) {
            return "Fuck you $";
        }
        return insults[new Random().nextInt(insults.length)];
    }
    
    private void insult() {
        if (Insulter.mc.func_147114_u() == null) {
            return;
        }
        final Collection<NetworkPlayerInfo> playerInfos = (Collection<NetworkPlayerInfo>)Insulter.mc.func_147114_u().func_175106_d();
        if (playerInfos == null) {
            return;
        }
        if (playerInfos.size() == 0) {
            return;
        }
        if (new Random().nextInt(4) == 0) {
            final String name1 = this.getRandomPlayer(playerInfos);
            this.getRandomPlayer(playerInfos);
        }
        else {
            final String name2 = this.getRandomPlayer(playerInfos);
            if (name2.equals("")) {
                return;
            }
            String randInsult = this.getSingleInsult();
            randInsult = randInsult.replace("$", name2);
            this.writeChat(randInsult);
        }
    }
    
    private void writeChat(final String msg) {
        if (this.clientSideOnly.getValue()) {
            Command.sendChatMessage(msg);
        }
        else {
            Insulter.mc.field_71442_b.field_78774_b.func_147297_a((Packet)new CPacketChatMessage(msg));
        }
    }
    
    private String getRandomPlayer(final Collection<NetworkPlayerInfo> playerInfos) {
        final int i = new Random().nextInt(playerInfos.size());
        final NetworkPlayerInfo playerInfo = (NetworkPlayerInfo)playerInfos.toArray()[i];
        if (playerInfo.func_178845_a().getName().equals(Insulter.mc.field_71439_g.func_70005_c_())) {
            return "";
        }
        return playerInfo.func_178845_a().getName();
    }
}
