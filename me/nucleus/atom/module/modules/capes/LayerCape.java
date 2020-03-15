// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.capes;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class LayerCape implements LayerRenderer<AbstractClientPlayer>
{
    private final RenderPlayer playerRenderer;
    
    public LayerCape(final RenderPlayer playerRenderer) {
        this.playerRenderer = playerRenderer;
    }
    
    public void doRenderLayer(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        final ResourceLocation rl = Capes.getCapeResource(player);
        final ItemStack itemstack = player.func_184582_a(EntityEquipmentSlot.CHEST);
        if (!player.func_152122_n() || player.func_82150_aj() || !player.func_175148_a(EnumPlayerModelParts.CAPE) || itemstack.func_77973_b() == Items.field_185160_cR || rl == null) {
            return;
        }
        float f9 = 0.14f;
        float f10 = 0.0f;
        if (player.func_70093_af()) {
            f9 = 0.1f;
            f10 = 0.09f;
        }
        GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
        this.playerRenderer.func_110776_a(rl);
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b(0.0f, f10, f9);
        final double d0 = player.field_71091_bM + (player.field_71094_bP - player.field_71091_bM) * new Float(partialTicks) - (player.field_70169_q + (player.field_70165_t - player.field_70169_q) * new Float(partialTicks));
        final double d2 = player.field_71096_bN + (player.field_71095_bQ - player.field_71096_bN) * new Float(partialTicks) - (player.field_70167_r + (player.field_70163_u - player.field_70167_r) * new Float(partialTicks));
        final double d3 = player.field_71097_bO + (player.field_71085_bR - player.field_71097_bO) * new Float(partialTicks) - (player.field_70166_s + (player.field_70161_v - player.field_70166_s) * new Float(partialTicks));
        final float f11 = player.field_70760_ar + (player.field_70761_aq - player.field_70760_ar) * partialTicks;
        final double d4 = new Float(MathHelper.func_76126_a(f11 * 0.01745329f));
        final double d5 = new Float(-MathHelper.func_76134_b(f11 * 0.01745329f));
        float f12 = new Double(d2).floatValue() * 10.0f;
        f12 = MathHelper.func_76131_a(f12, 3.0f, 32.0f);
        float f13 = new Double(d0 * d4 + d3 * d5).floatValue() * 100.0f;
        final float f14 = new Double(d0 * d5 - d3 * d4).floatValue() * 100.0f;
        if (f13 < 0.0f) {
            f13 = 0.0f;
        }
        final float f15 = player.field_71107_bF + (player.field_71109_bG - player.field_71107_bF) * partialTicks;
        f12 += MathHelper.func_76126_a((player.field_70141_P + (player.field_70140_Q - player.field_70141_P) * partialTicks) * 6.0f) * 32.0f * f15;
        if (player.func_70093_af()) {
            f12 += 20.0f;
        }
        GlStateManager.func_179114_b(5.0f + f13 / 2.0f + f12, 1.0f, 0.0f, 0.0f);
        GlStateManager.func_179114_b(f14 / 2.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.func_179114_b(-f14 / 2.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.func_179114_b(180.0f, 0.0f, 1.0f, 0.0f);
        this.playerRenderer.func_177087_b().func_178728_c(0.0625f);
        GlStateManager.func_179121_F();
    }
    
    public boolean func_177142_b() {
        return false;
    }
}
