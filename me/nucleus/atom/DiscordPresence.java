// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom;

import net.minecraft.item.ItemStack;
import me.nucleus.atom.util.InfoCalculator;
import me.nucleus.atom.module.modules.misc.Queue;
import net.minecraft.client.Minecraft;
import me.nucleus.atom.module.ModuleManager;
import club.minnced.discord.rpc.DiscordEventHandlers;
import me.nucleus.atom.module.modules.misc.DiscordSettings;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class DiscordPresence
{
    public static DiscordRichPresence presence;
    private static boolean hasStarted;
    private static final DiscordRPC rpc;
    private static String details;
    private static String state;
    private static DiscordSettings discordSettings;
    
    public static void start() {
        AtomMod.log.info("Starting Discord RPC");
        if (DiscordPresence.hasStarted) {
            return;
        }
        DiscordPresence.hasStarted = true;
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.disconnected = ((var1, var2) -> AtomMod.log.info("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));
        DiscordPresence.rpc.Discord_Initialize("675826209879359488", handlers, true, "");
        DiscordPresence.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        setRpcFromSettings();
        new Thread(DiscordPresence::setRpcFromSettingsNonInt, "Discord-RPC-Callback-Handler").start();
        AtomMod.log.info("Discord RPC initialised successfully");
    }
    
    private static void setRpcFromSettingsNonInt() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                DiscordPresence.rpc.Discord_RunCallbacks();
                DiscordPresence.discordSettings = (DiscordSettings)ModuleManager.getModuleByName("DiscordRPC");
                final String separator = " | ";
                final Minecraft mc = Minecraft.func_71410_x();
                if (mc.func_147104_D() != null) {
                    DiscordPresence.presence.details = "Playing on " + mc.func_147104_D().field_78845_b;
                    DiscordPresence.presence.state = "";
                    final Queue q = (Queue)ModuleManager.getModuleByNameUnsafe("Queue");
                    if (q != null && q.isEnabled() && q.inQueue()) {
                        DiscordPresence.presence.state = "In queue: " + q.getQueuePosition();
                    }
                    else if (mc.field_71439_g != null) {
                        if (DiscordPresence.discordSettings.info.getValue() == DiscordSettings.LineInfo.HELD_ITEM) {
                            final ItemStack stack = mc.field_71439_g.func_184614_ca();
                            if (stack != null && !stack.func_82833_r().equals("Air")) {
                                DiscordPresence.presence.state = "Holding " + stack.func_82833_r();
                            }
                        }
                        else if (DiscordPresence.discordSettings.info.getValue() == DiscordSettings.LineInfo.HEALTH) {
                            DiscordPresence.presence.state = mc.field_71439_g.func_110143_aJ() + mc.field_71439_g.func_110139_bj() + " HP";
                        }
                        else if (DiscordPresence.discordSettings.info.getValue() == DiscordSettings.LineInfo.SPEED) {
                            DiscordPresence.presence.state = "Traveling at " + InfoCalculator.speed() + "km/h";
                        }
                    }
                }
                else {
                    DiscordPresence.presence.details = "Main Menu";
                    DiscordPresence.presence.state = "";
                }
                DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                Thread.sleep(4000L);
            }
            catch (InterruptedException e3) {
                e3.printStackTrace();
            }
        }
    }
    
    private static void setRpcFromSettings() {
        DiscordPresence.discordSettings = (DiscordSettings)ModuleManager.getModuleByName("DiscordRPC");
        final Minecraft mc = Minecraft.func_71410_x();
        if (mc.func_147104_D() != null) {
            DiscordPresence.presence.details = "Playing on " + mc.func_147104_D();
            DiscordPresence.presence.state = "";
            if (mc.field_71439_g != null) {
                DiscordPresence.presence.state = "with " + (int)mc.field_71439_g.func_110143_aJ() + " hp";
            }
        }
        else {
            DiscordPresence.presence.details = "Main Menu";
            DiscordPresence.presence.state = "";
        }
        DiscordPresence.presence.largeImageKey = "atom";
        DiscordPresence.presence.largeImageText = "discord.gg/52cU85e";
        DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
    }
    
    static {
        rpc = DiscordRPC.INSTANCE;
        DiscordPresence.presence = new DiscordRichPresence();
        DiscordPresence.hasStarted = false;
    }
}
