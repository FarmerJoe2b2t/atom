// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.render;

import java.awt.Color;
import me.nucleus.atom.util.KamiTessellator;
import me.nucleus.atom.event.events.RenderEvent;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import me.nucleus.atom.module.ModuleManager;
import me.nucleus.atom.module.modules.hidden.CrystalAura;
import java.util.ArrayList;
import me.nucleus.atom.setting.Settings;
import java.util.List;
import me.nucleus.atom.setting.Setting;
import net.minecraft.util.math.BlockPos;
import me.nucleus.atom.module.Module;

@Info(name = "VoidESP", category = Category.RENDER, description = "Show void holes")
public class VoidESP extends Module
{
    private final BlockPos[] surroundOffset;
    private Setting<Double> renderDistance;
    private Setting<Integer> a;
    private Setting<Integer> r;
    private Setting<Integer> g;
    private Setting<Integer> b;
    private Setting<RenderMode> renderModeSetting;
    private List<BlockPos> holes;
    
    public VoidESP() {
        this.surroundOffset = new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
        this.renderDistance = this.register(Settings.d("Render Distance", 8.0));
        this.a = this.register((Setting<Integer>)Settings.integerBuilder("Transparency").withMinimum(0).withValue(32).withMaximum(255).build());
        this.r = this.register((Setting<Integer>)Settings.integerBuilder("Red").withMinimum(0).withValue(208).withMaximum(255).build());
        this.g = this.register((Setting<Integer>)Settings.integerBuilder("Green").withMinimum(0).withValue(0).withMaximum(255).build());
        this.b = this.register((Setting<Integer>)Settings.integerBuilder("Blue").withMinimum(0).withValue(0).withMaximum(255).build());
        this.renderModeSetting = this.register(Settings.e("Render Mode", RenderMode.BLOCK));
        this.holes = new ArrayList<BlockPos>();
    }
    
    @Override
    public void onUpdate() {
        this.holes.clear();
        final int range = (int)Math.ceil(this.renderDistance.getValue());
        final CrystalAura crystalAura = (CrystalAura)ModuleManager.getModuleByName("CrystalAura");
        final List<BlockPos> blockPosList = crystalAura.getSphere(CrystalAura.getPlayerPos(), (float)range, range, false, true, 0);
        for (final BlockPos pos : blockPosList) {
            if (pos.field_177960_b != 0) {
                continue;
            }
            if (!VoidESP.mc.field_71441_e.func_180495_p(pos).func_177230_c().equals(Blocks.field_150350_a)) {
                continue;
            }
            if (!VoidESP.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_177230_c().equals(Blocks.field_150350_a)) {
                continue;
            }
            this.holes.add(pos);
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (VoidESP.mc.field_71439_g == null || this.holes.isEmpty()) {
            return;
        }
        KamiTessellator.prepare(7);
        this.holes.forEach(blockPos -> this.drawBox(blockPos, this.r.getValue(), this.g.getValue(), this.b.getValue()));
        KamiTessellator.release();
    }
    
    private void drawBox(final BlockPos blockPos, final int r, final int g, final int b) {
        final Color color = new Color(r, g, b, this.a.getValue());
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
