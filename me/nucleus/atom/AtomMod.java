// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom;

import me.zero.alpine.EventManager;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import com.google.gson.JsonPrimitive;
import me.nucleus.atom.gui.rgui.component.Component;
import java.util.Optional;
import java.util.Iterator;
import me.nucleus.atom.gui.rgui.component.container.Container;
import me.nucleus.atom.gui.rgui.util.ContainerHelper;
import me.nucleus.atom.gui.rgui.component.AlignedComponent;
import me.nucleus.atom.gui.rgui.util.Docking;
import me.nucleus.atom.gui.rgui.component.container.use.Frame;
import com.google.gson.JsonElement;
import java.util.Map;
import me.nucleus.atom.setting.config.Configuration;
import java.nio.file.LinkOption;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.nio.file.NoSuchFileException;
import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import me.nucleus.atom.module.modules.gui.InventoryViewer;
import me.nucleus.atom.module.modules.misc.Chat;
import me.nucleus.atom.module.modules.hidden.TabFriends;
import me.nucleus.atom.module.modules.misc.DiscordSettings;
import me.nucleus.atom.module.Module;
import me.nucleus.atom.module.modules.capes.Capes;
import me.nucleus.atom.setting.SettingsRegister;
import me.nucleus.atom.command.Command;
import me.nucleus.atom.util.Friends;
import me.nucleus.atom.util.Wrapper;
import me.nucleus.atom.util.LagCompensator;
import me.nucleus.atom.event.ForgeEventProcessor;
import net.minecraftforge.common.MinecraftForge;
import java.util.function.Consumer;
import me.nucleus.atom.module.ModuleManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraft.client.Minecraft;
import me.nucleus.atom.util.RichPresence;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import me.nucleus.atom.setting.Settings;
import com.google.common.base.Converter;
import com.google.gson.JsonObject;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.command.CommandManager;
import me.nucleus.atom.gui.kami.KamiGUI;
import me.zero.alpine.EventBus;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "atom", name = "Atom Utility Mod", version = "v0.1")
public class AtomMod
{
    static final String MODID = "atom";
    static final String MODNAME = "Atom Utility Mod";
    public static final String MODVER = "v0.1";
    public static final String APP_ID = "675826209879359488";
    static final String UPDATE_JSON = "https://raw.githubusercontent.com/S-B99/kamiblue/assets/assets/updateChecker.json";
    public static final String DONATORS_JSON = "https://raw.githubusercontent.com/S-B99/kamiblue/assets/assets/donators.json";
    public static final String CAPES_JSON = "https://raw.githubusercontent.com/S-B99/kamiblue/assets/assets/capes.json";
    public static final String KAMI_KANJI = "ATOM";
    public static final String KAMI_BLUE = "\u1d00\u1d1b\u1d0f\u1d0d";
    public static final String KAMI_JAPANESE_ONTOP = "\u4e0a\u306b\u30ab\u30df\u30d6\u30eb\u30fc";
    public static final String KAMI_ONTOP = "\u1d00\u1d1b\u1d0f\u1d0d \u1d0f\u0274 \u1d1b\u1d0f\u1d18";
    public static final String KAMI_WEBSITE = "\u0299\u1d07\u029f\u029f\u1d00.\u1d21\u1d1b\ua730/\u1d0b\u1d00\u1d0d\u026a\u0299\u029f\u1d1c\u1d07";
    public static final char colour = '§';
    public static final char separator = '\u23d0';
    public static final char quoteLeft = '«';
    public static final char quoteRight = '»';
    private static final String KAMI_CONFIG_NAME_DEFAULT = "AtomConfig.json";
    public static final Logger log;
    public static final EventBus EVENT_BUS;
    @Mod.Instance
    private static AtomMod INSTANCE;
    public KamiGUI guiManager;
    public CommandManager commandManager;
    private Setting<JsonObject> guiStateSetting;
    
    public AtomMod() {
        this.guiStateSetting = Settings.custom("gui", new JsonObject(), new Converter<JsonObject, JsonObject>() {
            protected JsonObject doForward(final JsonObject jsonObject) {
                return jsonObject;
            }
            
            protected JsonObject doBackward(final JsonObject jsonObject) {
                return jsonObject;
            }
        }).buildAndRegister("");
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        if (RichPresence.INSTANCE.customUsers != null) {
            for (final RichPresence.CustomUser user : RichPresence.INSTANCE.customUsers) {
                if (user.uuid.equalsIgnoreCase(Minecraft.func_71410_x().field_71449_j.func_148256_e().getId().toString())) {
                    switch (Integer.parseInt(user.type)) {
                        case 0: {
                            DiscordPresence.presence.smallImageKey = "booster";
                            DiscordPresence.presence.smallImageText = "booster uwu";
                            break;
                        }
                        case 1: {
                            DiscordPresence.presence.smallImageKey = "inviter";
                            DiscordPresence.presence.smallImageText = "inviter owo";
                            break;
                        }
                        case 2: {
                            DiscordPresence.presence.smallImageKey = "giveaway";
                            DiscordPresence.presence.smallImageText = "giveaway winner";
                            break;
                        }
                        case 3: {
                            DiscordPresence.presence.smallImageKey = "contest";
                            DiscordPresence.presence.smallImageText = "contest winner";
                            break;
                        }
                        case 4: {
                            DiscordPresence.presence.smallImageKey = "nine";
                            DiscordPresence.presence.smallImageText = "900th member";
                            break;
                        }
                        default: {
                            DiscordPresence.presence.smallImageKey = "donator2";
                            DiscordPresence.presence.smallImageText = "donator <3";
                            break;
                        }
                    }
                }
            }
        }
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        AtomMod.log.info("\n\nInitializing ATOM v0.1");
        ModuleManager.initialize();
        ModuleManager.getModules().stream().filter(module -> module.alwaysListening).forEach(AtomMod.EVENT_BUS::subscribe);
        MinecraftForge.EVENT_BUS.register((Object)new ForgeEventProcessor());
        LagCompensator.INSTANCE = new LagCompensator();
        Wrapper.init();
        (this.guiManager = new KamiGUI()).initializeGUI();
        this.commandManager = new CommandManager();
        Friends.initFriends();
        SettingsRegister.register("commandPrefix", Command.commandPrefix);
        loadConfiguration();
        AtomMod.log.info("Settings loaded");
        new Capes();
        AtomMod.log.info("Capes init!\n");
        new RichPresence();
        AtomMod.log.info("Rich Presence Users init!\n");
        ModuleManager.getModules().stream().filter(Module::isEnabled).forEach(Module::enable);
        try {
            ModuleManager.getModuleByName("InfoOverlay").setEnabled(true);
            if (((DiscordSettings)ModuleManager.getModuleByName("DiscordRPC")).startupGlobal.getValue()) {
                ModuleManager.getModuleByName("DiscordRPC").setEnabled(true);
            }
            if (((TabFriends)ModuleManager.getModuleByName("TabFriends")).startupGlobal.getValue()) {
                ModuleManager.getModuleByName("TabFriends").setEnabled(true);
            }
            if (((Chat)ModuleManager.getModuleByName("Chat")).startupGlobal.getValue()) {
                ModuleManager.getModuleByName("Chat").setEnabled(true);
            }
            if (((InventoryViewer)ModuleManager.getModuleByName("InventoryViewer")).startupGlobal.getValue()) {
                ModuleManager.getModuleByName("InventoryViewer").setEnabled(true);
            }
        }
        catch (NullPointerException e) {
            AtomMod.log.info("NPE in loading always enabled modules\n");
        }
        AtomMod.log.info("ATOM Client initialized!\n");
    }
    
    public static String getConfigName() {
        final Path config = Paths.get("AtomLastConfig.txt", new String[0]);
        String kamiConfigName = "AtomConfig.json";
        try (final BufferedReader reader = Files.newBufferedReader(config)) {
            kamiConfigName = reader.readLine();
            if (!isFilenameValid(kamiConfigName)) {
                kamiConfigName = "AtomConfig.json";
            }
        }
        catch (NoSuchFileException e3) {
            try (final BufferedWriter writer = Files.newBufferedWriter(config, new OpenOption[0])) {
                writer.write("AtomConfig.json");
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return kamiConfigName;
    }
    
    public static void loadConfiguration() {
        try {
            loadConfigurationUnsafe();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void loadConfigurationUnsafe() throws IOException {
        final String kamiConfigName = getConfigName();
        final Path kamiConfig = Paths.get(kamiConfigName, new String[0]);
        if (!Files.exists(kamiConfig, new LinkOption[0])) {
            return;
        }
        Configuration.loadConfiguration(kamiConfig);
        final JsonObject gui = AtomMod.INSTANCE.guiStateSetting.getValue();
        for (final Map.Entry<String, JsonElement> entry : gui.entrySet()) {
            final Optional<Component> optional = AtomMod.INSTANCE.guiManager.getChildren().stream().filter(component -> component instanceof Frame).filter(component -> component.getTitle().equals(entry.getKey())).findFirst();
            if (optional.isPresent()) {
                final JsonObject object = entry.getValue().getAsJsonObject();
                final Frame frame = optional.get();
                frame.setX(object.get("x").getAsInt());
                frame.setY(object.get("y").getAsInt());
                final Docking docking = Docking.values()[object.get("docking").getAsInt()];
                if (docking.isLeft()) {
                    ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.LEFT);
                }
                else if (docking.isRight()) {
                    ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.RIGHT);
                }
                else if (docking.isCenterVertical()) {
                    ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.CENTER);
                }
                frame.setDocking(docking);
                frame.setMinimized(object.get("minimized").getAsBoolean());
                frame.setPinned(object.get("pinned").getAsBoolean());
            }
            else {
                System.err.println("Found GUI config entry for " + entry.getKey() + ", but found no frame with that name");
            }
        }
        getInstance().getGuiManager().getChildren().stream().filter(component -> component instanceof Frame && component.isPinnable() && component.isVisible()).forEach(component -> component.setOpacity(0.0f));
    }
    
    public static void saveConfiguration() {
        try {
            saveConfigurationUnsafe();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void saveConfigurationUnsafe() throws IOException {
        final JsonObject object = new JsonObject();
        final JsonObject frameObject;
        final JsonObject jsonObject;
        AtomMod.INSTANCE.guiManager.getChildren().stream().filter(component -> component instanceof Frame).map(component -> component).forEach(frame -> {
            frameObject = new JsonObject();
            frameObject.add("x", (JsonElement)new JsonPrimitive((Number)frame.getX()));
            frameObject.add("y", (JsonElement)new JsonPrimitive((Number)frame.getY()));
            frameObject.add("docking", (JsonElement)new JsonPrimitive((Number)Arrays.asList(Docking.values()).indexOf(frame.getDocking())));
            frameObject.add("minimized", (JsonElement)new JsonPrimitive(Boolean.valueOf(frame.isMinimized())));
            frameObject.add("pinned", (JsonElement)new JsonPrimitive(Boolean.valueOf(frame.isPinned())));
            jsonObject.add(frame.getTitle(), (JsonElement)frameObject);
            return;
        });
        AtomMod.INSTANCE.guiStateSetting.setValue(object);
        final Path outputFile = Paths.get(getConfigName(), new String[0]);
        if (!Files.exists(outputFile, new LinkOption[0])) {
            Files.createFile(outputFile, (FileAttribute<?>[])new FileAttribute[0]);
        }
        Configuration.saveConfiguration(outputFile);
        ModuleManager.getModules().forEach(Module::destroy);
    }
    
    public static boolean isFilenameValid(final String file) {
        final File f = new File(file);
        try {
            f.getCanonicalPath();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
    
    public static AtomMod getInstance() {
        return AtomMod.INSTANCE;
    }
    
    public KamiGUI getGuiManager() {
        return this.guiManager;
    }
    
    public CommandManager getCommandManager() {
        return this.commandManager;
    }
    
    static {
        log = LogManager.getLogger("ATOM");
        EVENT_BUS = new EventManager();
    }
}
