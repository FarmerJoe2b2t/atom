// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.misc;

import java.net.URLEncoder;
import me.nucleus.atom.command.Command;
import java.util.Scanner;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import com.google.gson.JsonParser;
import java.time.Instant;
import me.nucleus.atom.module.ModuleManager;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiDisconnected;
import java.util.function.Predicate;
import net.minecraft.client.multiplayer.GuiConnecting;
import me.nucleus.atom.setting.builder.SettingBuilder;
import me.nucleus.atom.setting.Settings;
import me.nucleus.atom.event.events.PacketEvent;
import me.zero.alpine.listener.EventHandler;
import me.nucleus.atom.event.events.GuiScreenEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.multiplayer.ServerData;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "TelegramNotifications", category = Category.MISC)
public class TelegramNotifications extends Module
{
    private Setting<String> botToken;
    private Setting<String> channelID;
    public Setting<Boolean> queue;
    public Setting<Integer> queueStep;
    public Setting<Boolean> death;
    public Setting<Boolean> disconnect;
    public Setting<Boolean> chat;
    public Setting<Boolean> stashFinder;
    public Setting<Boolean> visualRange;
    public Setting<Boolean> poll;
    public Setting<Double> pollIntervalSecs;
    private ServerData cServer;
    @EventHandler
    public Listener<GuiScreenEvent.Closed> closedListener;
    @EventHandler
    public Listener<GuiScreenEvent.Displayed> displayedListener;
    @EventHandler
    public Listener<PacketEvent.Receive> packetListener;
    
    public TelegramNotifications() {
        this.botToken = this.register(Settings.s("BotToken", ""));
        this.channelID = this.register(Settings.s("ChatID", ""));
        this.queue = this.register(Settings.b("Queue", true));
        this.queueStep = this.register(Settings.integerBuilder("QueueStep").withMinimum(10).withValue(50).withMaximum(100));
        this.death = this.register(Settings.b("Death", true));
        this.disconnect = this.register(Settings.b("Disconnect", true));
        this.chat = this.register(Settings.b("Chat", true));
        this.stashFinder = this.register(Settings.b("StashFinder", true));
        this.visualRange = this.register(Settings.b("VisualRange", true));
        this.poll = this.register(Settings.b("Poll", false));
        this.pollIntervalSecs = this.register(Settings.d("PollIntervalSecs", 3.0));
        this.closedListener = new Listener<GuiScreenEvent.Closed>(event -> {
            if (event.getScreen() instanceof GuiConnecting) {
                this.cServer = TelegramNotifications.mc.field_71422_O;
            }
            return;
        }, (Predicate<GuiScreenEvent.Closed>[])new Predicate[0]);
        GuiDisconnected guiDisconnected;
        this.displayedListener = new Listener<GuiScreenEvent.Displayed>(event -> {
            if (this.isEnabled() && event.getScreen() instanceof GuiDisconnected && (this.cServer != null || TelegramNotifications.mc.field_71422_O != null) && this.disconnect.getValue()) {
                guiDisconnected = (GuiDisconnected)event.getScreen();
                this.notify("You were disconnected: " + guiDisconnected.field_146306_a);
            }
            if (this.isEnabled() && event.getScreen() instanceof GuiGameOver && this.death.getValue()) {
                this.notify("You died");
            }
            return;
        }, (Predicate<GuiScreenEvent.Displayed>[])new Predicate[0]);
        String text;
        Chat chat;
        this.packetListener = new Listener<PacketEvent.Receive>(event -> {
            if (this.isEnabled() && event.getPacket() instanceof SPacketChat && this.chat.getValue()) {
                text = ((SPacketChat)event.getPacket()).func_148915_c().func_150260_c();
                chat = (Chat)ModuleManager.getModuleByNameUnsafe("Chat");
                if (chat != null && chat.isEnabled() && chat.addTimestamp.getValue() && text.startsWith("§7")) {
                    text = text.substring(text.split(" ")[0].length());
                }
                this.notify(text);
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    public void onEnable() {
        if (this.poll.getValue()) {
            new Thread(() -> this.doPolling(this.poll, this.botToken.getValue(), this.channelID.getValue(), (long)(this.pollIntervalSecs.getValue() * 1000.0))).start();
        }
    }
    
    private void doPolling(final Setting<Boolean> pollSetting, final String botToken, final String chatID, final long intervalMillis) {
        long time = Instant.now().getEpochSecond();
        while (true) {
            try {
                Thread.sleep(intervalMillis);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!pollSetting.getValue()) {
                break;
            }
            final String updates = this.fetchUpdates(botToken);
            if (updates == null) {
                continue;
            }
            final JsonElement element = new JsonParser().parse(updates);
            final JsonArray results = element.getAsJsonObject().get("result").getAsJsonArray();
            for (int i = 0; i < results.size(); ++i) {
                final JsonObject message = results.get(i).getAsJsonObject().get("message").getAsJsonObject();
                if (message.get("chat").getAsJsonObject().get("id").getAsString().equals(chatID)) {
                    final long messageTime = message.get("date").getAsLong();
                    if (messageTime > time) {
                        final String text = message.get("text").getAsString();
                        TelegramNotifications.mc.field_71442_b.field_78774_b.func_147297_a((Packet)new CPacketChatMessage(text));
                        time = messageTime;
                    }
                }
            }
        }
    }
    
    private String fetchUpdates(final String botToken) {
        try {
            final String query = "https://api.telegram.org/bot" + botToken + "/getUpdates";
            final URL url = new URL(query);
            final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            final InputStream in = new BufferedInputStream(conn.getInputStream());
            final String res = convertStreamToString(in);
            in.close();
            if (conn.getResponseCode() != 200) {
                System.out.println("Telegram API returned: " + conn.getResponseCode());
                System.out.println(res);
            }
            conn.disconnect();
            return res;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static String convertStreamToString(final InputStream is) {
        final Scanner s = new Scanner(is).useDelimiter("\\A");
        final String r = s.hasNext() ? s.next() : "/";
        return r;
    }
    
    public void notify(final String msg) {
        if (this.botToken.getValue().length() == 0 || this.channelID.getValue().length() == 0) {
            Command.sendChatMessage("Telegram Notifications not properly configured.");
            return;
        }
        new Thread(() -> this.sendMessage(msg)).start();
    }
    
    private void sendMessage(final String msg) {
        try {
            final String query = "https://api.telegram.org/bot" + this.botToken.getValue() + "/sendMessage?chat_id=" + this.channelID.getValue() + "&text=" + URLEncoder.encode(msg, "UTF-8");
            final URL url = new URL(query);
            final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            final InputStream in = new BufferedInputStream(conn.getInputStream());
            in.close();
            if (conn.getResponseCode() != 200) {
                System.out.println("Telegram API returned: " + conn.getResponseCode());
            }
            conn.disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
