// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.event;

import me.nucleus.atom.gui.rgui.component.Component;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraft.client.gui.GuiChat;
import me.nucleus.atom.command.Command;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import me.nucleus.atom.module.modules.hidden.BossStack;
import me.nucleus.atom.util.KamiTessellator;
import me.nucleus.atom.gui.UIRenderer;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import me.nucleus.atom.gui.rgui.component.container.Container;
import me.nucleus.atom.module.ModuleManager;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.IInventory;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import me.nucleus.atom.util.Wrapper;
import net.minecraft.client.gui.ScaledResolution;
import me.nucleus.atom.command.commands.PeekCommand;
import me.nucleus.atom.gui.kami.KamiGUI;
import me.nucleus.atom.gui.rgui.component.container.use.Frame;
import me.nucleus.atom.event.events.DisplaySizeChangedEvent;
import me.nucleus.atom.AtomMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.living.LivingEvent;

public class ForgeEventProcessor
{
    private int displayWidth;
    private int displayHeight;
    
    @SubscribeEvent
    public void onUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (event.isCanceled()) {
            return;
        }
        if (Minecraft.func_71410_x().field_71443_c != this.displayWidth || Minecraft.func_71410_x().field_71440_d != this.displayHeight) {
            AtomMod.EVENT_BUS.post(new DisplaySizeChangedEvent());
            this.displayWidth = Minecraft.func_71410_x().field_71443_c;
            this.displayHeight = Minecraft.func_71410_x().field_71440_d;
            AtomMod.getInstance().getGuiManager().getChildren().stream().filter(component -> component instanceof Frame).forEach(component -> KamiGUI.dock(component));
        }
        if (PeekCommand.sb != null) {
            final ScaledResolution scaledresolution = new ScaledResolution(Minecraft.func_71410_x());
            final int i = scaledresolution.func_78326_a();
            final int j = scaledresolution.func_78328_b();
            final GuiShulkerBox gui = new GuiShulkerBox(Wrapper.getPlayer().field_71071_by, (IInventory)PeekCommand.sb);
            gui.func_146280_a(Wrapper.getMinecraft(), i, j);
            Minecraft.func_71410_x().func_147108_a((GuiScreen)gui);
            PeekCommand.sb = null;
        }
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (Wrapper.getPlayer() == null) {
            return;
        }
        ModuleManager.onUpdate();
        AtomMod.getInstance().getGuiManager().callTick(AtomMod.getInstance().getGuiManager());
    }
    
    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        ModuleManager.onWorldRender(event);
    }
    
    @SubscribeEvent
    public void onRenderPre(final RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && ModuleManager.isModuleEnabled("BossStack")) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (event.isCanceled()) {
            return;
        }
        RenderGameOverlayEvent.ElementType target = RenderGameOverlayEvent.ElementType.EXPERIENCE;
        if (!Wrapper.getPlayer().func_184812_l_() && Wrapper.getPlayer().func_184187_bx() instanceof AbstractHorse) {
            target = RenderGameOverlayEvent.ElementType.HEALTHMOUNT;
        }
        if (event.getType() == target) {
            ModuleManager.onRender();
            GL11.glPushMatrix();
            UIRenderer.renderAndUpdateFrames();
            GL11.glPopMatrix();
            KamiTessellator.releaseGL();
        }
        else if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && ModuleManager.isModuleEnabled("BossStack")) {
            BossStack.render(event);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState()) {
            return;
        }
        if (("" + Keyboard.getEventCharacter()).equalsIgnoreCase(Command.getCommandPrefix())) {
            Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiChat(Command.getCommandPrefix()));
        }
        else {
            ModuleManager.onBind(Keyboard.getEventKey());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatSent(final ClientChatEvent event) {
        if (event.getMessage().startsWith(Command.getCommandPrefix())) {
            event.setCanceled(true);
            try {
                Wrapper.getMinecraft().field_71456_v.func_146158_b().func_146239_a(event.getMessage());
                if (event.getMessage().length() > 1) {
                    AtomMod.getInstance().commandManager.callCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
                }
                else {
                    Command.sendChatMessage("Please enter a command.");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                Command.sendChatMessage("Error occured while running command! (" + e.getMessage() + ")");
            }
            event.setMessage("");
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerDrawn(final RenderPlayerEvent.Pre event) {
        AtomMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerDrawn(final RenderPlayerEvent.Post event) {
        AtomMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onChunkLoaded(final ChunkEvent.Load event) {
        AtomMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onChunkLoaded(final ChunkEvent.Unload event) {
        AtomMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onInputUpdate(final InputUpdateEvent event) {
        AtomMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onLivingEntityUseItemEventTick(final LivingEntityUseItemEvent.Start entityUseItemEvent) {
        AtomMod.EVENT_BUS.post(entityUseItemEvent);
    }
    
    @SubscribeEvent
    public void onLivingDamageEvent(final LivingDamageEvent event) {
        AtomMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onEntityJoinWorldEvent(final EntityJoinWorldEvent entityJoinWorldEvent) {
        AtomMod.EVENT_BUS.post(entityJoinWorldEvent);
    }
    
    @SubscribeEvent
    public void onPlayerPush(final PlayerSPPushOutOfBlocksEvent event) {
        AtomMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
        AtomMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onAttackEntity(final AttackEntityEvent entityEvent) {
        AtomMod.EVENT_BUS.post(entityEvent);
    }
    
    @SubscribeEvent
    public void onRenderBlockOverlay(final RenderBlockOverlayEvent event) {
        AtomMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onPlayerLogin(final PlayerEvent.PlayerLoggedInEvent event) {
        AtomMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onPlayerLeaved(final PlayerEvent.PlayerLoggedOutEvent event) {
        AtomMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onClientConnected(final FMLNetworkEvent.ClientConnectedToServerEvent event) {
        AtomMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onClientDisconnected(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        AtomMod.EVENT_BUS.post(event);
    }
}
