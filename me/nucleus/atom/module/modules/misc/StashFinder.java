// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.misc;

import java.util.Iterator;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import me.nucleus.atom.module.ModuleManager;
import me.nucleus.atom.command.Command;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntity;
import me.nucleus.atom.setting.Settings;
import java.util.HashMap;
import me.nucleus.atom.setting.Setting;
import java.util.Map;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import me.nucleus.atom.module.Module;

@Info(name = "StashFinder", category = Category.MISC, description = "Logs chests and shulker boxes to a file")
public class StashFinder extends Module
{
    private ArrayList<BlockPos> chestPositions;
    private Map<Long, Integer> stashMap;
    private Setting<Integer> density;
    private Setting<Boolean> logToFile;
    private Setting<Boolean> logShulkers;
    private Setting<Boolean> playSound;
    
    public StashFinder() {
        this.chestPositions = new ArrayList<BlockPos>();
        this.stashMap = new HashMap<Long, Integer>();
        this.density = this.register((Setting<Integer>)Settings.integerBuilder("Chest Density").withMinimum(2).withValue(5).withMaximum(10).build());
        this.logToFile = this.register(Settings.b("Log to file", true));
        this.logShulkers = this.register(Settings.b("Log shulkers", true));
        this.playSound = this.register(Settings.b("Play sound", true));
    }
    
    public void onEnable() {
        this.chestPositions.clear();
        this.stashMap.clear();
    }
    
    @Override
    public void onUpdate() {
        for (final TileEntity tileEntity : StashFinder.mc.field_71441_e.field_147482_g) {
            final BlockPos pos = tileEntity.func_174877_v();
            if (tileEntity instanceof TileEntityChest || tileEntity instanceof TileEntityShulkerBox) {
                boolean alreadyAdded = false;
                for (final BlockPos p : this.chestPositions) {
                    if (p.equals((Object)pos)) {
                        alreadyAdded = true;
                    }
                }
                if (alreadyAdded) {
                    continue;
                }
                this.chestPositions.add(pos);
                final int chunkX = pos.func_177958_n() / 16;
                final int chunkZ = pos.func_177952_p() / 16;
                final long chunk = ChunkPos.func_77272_a(chunkX, chunkZ);
                if (!this.stashMap.containsKey(chunk)) {
                    this.stashMap.put(chunk, 0);
                }
                final int DENSITY = this.density.getValue();
                int count = this.stashMap.get(chunk) + 1;
                if (this.logShulkers.getValue() && tileEntity instanceof TileEntityShulkerBox && count < DENSITY) {
                    count = DENSITY;
                }
                this.stashMap.put(chunk, count);
                if (count != DENSITY) {
                    continue;
                }
                Command.sendChatMessagePermanent("[StashFinder] " + pos.toString());
                final TelegramNotifications telegram = (TelegramNotifications)ModuleManager.getModuleByNameUnsafe("TelegramNotifications");
                if (telegram != null && telegram.isEnabled() && telegram.stashFinder.getValue()) {
                    telegram.notify("[StashFinder] " + pos.toString() + ((tileEntity instanceof TileEntityShulkerBox) ? " Shulker" : ""));
                }
                if (this.playSound.getValue()) {
                    StashFinder.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_194007_a(SoundEvents.field_187604_bf, 1.0f, 1.0f));
                }
                if (!this.logToFile.getValue()) {
                    continue;
                }
                try {
                    final BufferedWriter writer = new BufferedWriter(new FileWriter("AtomStashFinder.txt", true));
                    String line = "";
                    final Calendar calendar = Calendar.getInstance();
                    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    line = line + "[" + formatter.format(calendar.getTime()) + "|";
                    if (StashFinder.mc.func_147104_D() != null) {
                        line = line + StashFinder.mc.func_147104_D().field_78845_b + "|";
                    }
                    switch (StashFinder.mc.field_71439_g.field_71093_bK) {
                        case 0: {
                            line += "Overworld";
                            break;
                        }
                        case 1: {
                            line += "End";
                            break;
                        }
                        case -1: {
                            line += "Nether";
                            break;
                        }
                    }
                    line += "] ";
                    line = line + pos.toString() + " ";
                    if (tileEntity instanceof TileEntityShulkerBox) {
                        line += "Shulker";
                    }
                    writer.write(line);
                    writer.newLine();
                    writer.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
