// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.render;

import java.awt.Color;
import me.nucleus.atom.util.KamiTessellator;
import me.nucleus.atom.event.events.RenderEvent;
import net.minecraft.block.Block;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.math.Vec3i;
import net.minecraft.init.Blocks;
import me.nucleus.atom.module.ModuleManager;
import me.nucleus.atom.module.modules.hidden.CrystalAura;
import me.nucleus.atom.setting.Settings;
import java.util.concurrent.ConcurrentHashMap;
import me.nucleus.atom.setting.Setting;
import net.minecraft.util.math.BlockPos;
import me.nucleus.atom.module.Module;

@Info(name = "HoleESP", category = Category.RENDER, description = "Show safe holes")
public class HoleESP extends Module
{
    private final BlockPos[] surroundOffset;
    private Setting<Double> renderDistance;
    private Setting<Integer> a0;
    private Setting<Integer> r1;
    private Setting<Integer> g1;
    private Setting<Integer> b1;
    private Setting<Integer> r2;
    private Setting<Integer> g2;
    private Setting<Integer> b2;
    private Setting<RenderMode> renderModeSetting;
    private Setting<RenderBlocks> renderBlocksSetting;
    private ConcurrentHashMap<BlockPos, Boolean> safeHoles;
    
    public HoleESP() {
        this.surroundOffset = new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
        this.renderDistance = this.register(Settings.d("Render Distance", 8.0));
        this.a0 = this.register((Setting<Integer>)Settings.integerBuilder("Transparency").withMinimum(0).withValue(32).withMaximum(255).build());
        this.r1 = this.register((Setting<Integer>)Settings.integerBuilder("Red (Obby)").withMinimum(0).withValue(208).withMaximum(255).build());
        this.g1 = this.register((Setting<Integer>)Settings.integerBuilder("Green (Obby)").withMinimum(0).withValue(144).withMaximum(255).build());
        this.b1 = this.register((Setting<Integer>)Settings.integerBuilder("Blue (Obby)").withMinimum(0).withValue(255).withMaximum(255).build());
        this.r2 = this.register((Setting<Integer>)Settings.integerBuilder("Red (Bedrock)").withMinimum(0).withValue(144).withMaximum(255).build());
        this.g2 = this.register((Setting<Integer>)Settings.integerBuilder("Green (Bedrock)").withMinimum(0).withValue(144).withMaximum(255).build());
        this.b2 = this.register((Setting<Integer>)Settings.integerBuilder("Blue (Bedrock)").withMinimum(0).withValue(255).withMaximum(255).build());
        this.renderModeSetting = this.register(Settings.e("Render Mode", RenderMode.BLOCK));
        this.renderBlocksSetting = this.register(Settings.e("Render", RenderBlocks.BOTH));
    }
    
    @Override
    public void onUpdate() {
        if (this.safeHoles == null) {
            this.safeHoles = new ConcurrentHashMap<BlockPos, Boolean>();
        }
        else {
            this.safeHoles.clear();
        }
        final int range = (int)Math.ceil(this.renderDistance.getValue());
        final CrystalAura crystalAura = (CrystalAura)ModuleManager.getModuleByName("CrystalAura");
        final List<BlockPos> blockPosList = crystalAura.getSphere(CrystalAura.getPlayerPos(), (float)range, range, false, true, 0);
        for (final BlockPos pos : blockPosList) {
            if (!HoleESP.mc.field_71441_e.func_180495_p(pos).func_177230_c().equals(Blocks.field_150350_a)) {
                continue;
            }
            if (!HoleESP.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_177230_c().equals(Blocks.field_150350_a)) {
                continue;
            }
            if (!HoleESP.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 2, 0)).func_177230_c().equals(Blocks.field_150350_a)) {
                continue;
            }
            boolean isSafe = true;
            boolean isBedrock = true;
            for (final BlockPos offset : this.surroundOffset) {
                final Block block = HoleESP.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)offset)).func_177230_c();
                if (block != Blocks.field_150357_h) {
                    isBedrock = false;
                }
                if (block != Blocks.field_150357_h && block != Blocks.field_150343_Z && block != Blocks.field_150477_bB && block != Blocks.field_150467_bQ) {
                    isSafe = false;
                    break;
                }
            }
            if (!isSafe) {
                continue;
            }
            this.safeHoles.put(pos, isBedrock);
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (HoleESP.mc.field_71439_g == null || this.safeHoles == null) {
            return;
        }
        if (this.safeHoles.isEmpty()) {
            return;
        }
        KamiTessellator.prepare(7);
        this.safeHoles.forEach((blockPos, isBedrock) -> {
            switch (this.renderBlocksSetting.getValue()) {
                case BOTH: {
                    if (isBedrock) {
                        this.drawBox(blockPos, this.r2.getValue(), this.g2.getValue(), this.b2.getValue());
                        break;
                    }
                    else {
                        this.drawBox(blockPos, this.r1.getValue(), this.g1.getValue(), this.b1.getValue());
                        break;
                    }
                    break;
                }
                case OBBY: {
                    if (!isBedrock) {
                        this.drawBox(blockPos, this.r1.getValue(), this.g1.getValue(), this.b1.getValue());
                        break;
                    }
                    else {
                        break;
                    }
                    break;
                }
                case BEDROCK: {
                    if (isBedrock) {
                        this.drawBox(blockPos, this.r2.getValue(), this.g2.getValue(), this.b2.getValue());
                        break;
                    }
                    else {
                        break;
                    }
                    break;
                }
            }
            return;
        });
        KamiTessellator.release();
    }
    
    private void drawBox(final BlockPos blockPos, final int r, final int g, final int b) {
        final Color color = new Color(r, g, b, this.a0.getValue());
        if (this.renderModeSetting.getValue().equals(RenderMode.DOWN)) {
            KamiTessellator.drawBox(blockPos, color.getRGB(), 1);
        }
        else if (this.renderModeSetting.getValue().equals(RenderMode.BLOCK)) {
            KamiTessellator.drawBox(blockPos, color.getRGB(), 63);
        }
    }
    
    private enum RenderMode
    {
        DOWN, 
        BLOCK;
    }
    
    private enum RenderBlocks
    {
        OBBY, 
        BEDROCK, 
        BOTH;
    }
}
