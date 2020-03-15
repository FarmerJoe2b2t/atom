// 
// Decompiled by Procyon v0.5.36
// 

package me.nucleus.atom.module.modules.render;

import net.minecraft.enchantment.Enchantment;
import me.nucleus.atom.util.ColourHolder;
import java.awt.Color;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.InventoryPlayer;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.BufferBuilder;
import java.util.Optional;
import java.util.Collection;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.math.Vec3d;
import me.nucleus.atom.util.Friends;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.network.NetworkPlayerInfo;
import me.nucleus.atom.module.ModuleManager;
import me.nucleus.atom.module.modules.combat.TotemPopCount;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.RenderHelper;
import java.util.function.Consumer;
import java.util.Comparator;
import net.minecraft.entity.player.EntityPlayer;
import java.util.function.Predicate;
import me.nucleus.atom.util.EntityUtil;
import net.minecraft.client.renderer.GlStateManager;
import me.nucleus.atom.event.events.RenderEvent;
import me.nucleus.atom.setting.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import me.nucleus.atom.setting.Setting;
import me.nucleus.atom.module.Module;

@Info(name = "Nametags", description = "Draws descriptive nametags above entities", category = Category.RENDER)
public class Nametags extends Module
{
    private Setting<Boolean> players;
    private Setting<Boolean> animals;
    private Setting<Boolean> mobs;
    private Setting<Double> range;
    private Setting<Float> scale;
    private Setting<Boolean> health;
    private Setting<Boolean> armor;
    RenderItem itemRenderer;
    static final Minecraft mc;
    
    public Nametags() {
        this.players = this.register(Settings.b("Players", true));
        this.animals = this.register(Settings.b("Animals", false));
        this.mobs = this.register(Settings.b("Mobs", false));
        this.range = this.register(Settings.d("Range", 200.0));
        this.scale = this.register((Setting<Float>)Settings.floatBuilder("Scale").withMinimum(0.5f).withMaximum(10.0f).withValue(2.5f).build());
        this.health = this.register(Settings.b("Health", true));
        this.armor = this.register(Settings.b("Armor", true));
        this.itemRenderer = Nametags.mc.func_175599_af();
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (Nametags.mc.func_175598_ae().field_78733_k == null) {
            return;
        }
        GlStateManager.func_179098_w();
        GlStateManager.func_179140_f();
        GlStateManager.func_179097_i();
        Minecraft.func_71410_x().field_71441_e.field_72996_f.stream().filter(EntityUtil::isLiving).filter(entity -> !EntityUtil.isFakeLocalPlayer(entity)).filter(entity -> (entity instanceof EntityPlayer) ? (this.players.getValue() && Nametags.mc.field_71439_g != entity) : (EntityUtil.isPassive(entity) ? this.animals.getValue() : ((boolean)this.mobs.getValue()))).filter(entity -> Nametags.mc.field_71439_g.func_70032_d(entity) < this.range.getValue()).sorted(Comparator.comparing(entity -> -Nametags.mc.field_71439_g.func_70032_d(entity))).forEach(this::drawNametag);
        GlStateManager.func_179090_x();
        RenderHelper.func_74518_a();
        GlStateManager.func_179145_e();
        GlStateManager.func_179126_j();
    }
    
    private void drawNametag(final Entity entityIn) {
        GlStateManager.func_179094_E();
        final Vec3d interp = EntityUtil.getInterpolatedRenderPos(entityIn, Nametags.mc.func_184121_ak());
        final float yAdd = entityIn.field_70131_O + 0.5f - (entityIn.func_70093_af() ? 0.25f : 0.0f);
        final double x = interp.field_72450_a;
        final double y = interp.field_72448_b + yAdd;
        final double z = interp.field_72449_c;
        final float viewerYaw = Nametags.mc.func_175598_ae().field_78735_i;
        final float viewerPitch = Nametags.mc.func_175598_ae().field_78732_j;
        final boolean isThirdPersonFrontal = Nametags.mc.func_175598_ae().field_78733_k.field_74320_O == 2;
        GlStateManager.func_179137_b(x, y, z);
        GlStateManager.func_179114_b(-viewerYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.func_179114_b((isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0f, 0.0f, 0.0f);
        final float f = Nametags.mc.field_71439_g.func_70032_d(entityIn);
        final float m = f / 8.0f * (float)Math.pow(1.258925437927246, this.scale.getValue());
        GlStateManager.func_179152_a(m, m, m);
        final FontRenderer fontRendererIn = Nametags.mc.field_71466_p;
        GlStateManager.func_179152_a(-0.025f, -0.025f, 0.025f);
        String str = entityIn.func_70005_c_() + (this.health.getValue() ? (" §a" + Math.round(((EntityLivingBase)entityIn).func_110143_aJ() + ((entityIn instanceof EntityPlayer) ? ((EntityPlayer)entityIn).func_110139_bj() : 0.0f))) : "");
        if (entityIn instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)entityIn;
            final TotemPopCount totemPopCount = (TotemPopCount)ModuleManager.getModuleByNameUnsafe("TotemPopCount");
            if (totemPopCount != null && totemPopCount.isEnabled()) {
                Integer popCount = totemPopCount.popList.get(player.func_70005_c_());
                if (popCount == null) {
                    popCount = 0;
                }
                str = str + " §e" + popCount + "";
            }
            final Collection<NetworkPlayerInfo> playerInfos = (Collection<NetworkPlayerInfo>)Nametags.mc.func_147114_u().func_175106_d();
            final Optional<NetworkPlayerInfo> info = playerInfos.stream().filter(i -> i.func_178845_a().getName().equals(player.func_70005_c_())).findFirst();
            if (info.isPresent()) {
                str = str + " §b" + info.get().func_178853_c() + "ms";
            }
        }
        final int j = fontRendererIn.func_78256_a(str) / 2;
        GlStateManager.func_179147_l();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.func_179090_x();
        final Tessellator tessellator = Tessellator.func_178181_a();
        final BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_179097_i();
        GL11.glTranslatef(0.0f, -20.0f, 0.0f);
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b((double)(-j - 1), 8.0, 0.0).func_181666_a(0.0f, 0.0f, 0.0f, 0.5f).func_181675_d();
        bufferbuilder.func_181662_b((double)(-j - 1), 19.0, 0.0).func_181666_a(0.0f, 0.0f, 0.0f, 0.5f).func_181675_d();
        bufferbuilder.func_181662_b((double)(j + 1), 19.0, 0.0).func_181666_a(0.0f, 0.0f, 0.0f, 0.5f).func_181675_d();
        bufferbuilder.func_181662_b((double)(j + 1), 8.0, 0.0).func_181666_a(0.0f, 0.0f, 0.0f, 0.5f).func_181675_d();
        tessellator.func_78381_a();
        bufferbuilder.func_181668_a(2, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b((double)(-j - 1), 8.0, 0.0).func_181666_a(0.1f, 0.1f, 0.1f, 0.1f).func_181675_d();
        bufferbuilder.func_181662_b((double)(-j - 1), 19.0, 0.0).func_181666_a(0.1f, 0.1f, 0.1f, 0.1f).func_181675_d();
        bufferbuilder.func_181662_b((double)(j + 1), 19.0, 0.0).func_181666_a(0.1f, 0.1f, 0.1f, 0.1f).func_181675_d();
        bufferbuilder.func_181662_b((double)(j + 1), 8.0, 0.0).func_181666_a(0.1f, 0.1f, 0.1f, 0.1f).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_187432_a(0.0f, 1.0f, 0.0f);
        if (!entityIn.func_70093_af()) {
            fontRendererIn.func_78276_b(str, -j, 10, (entityIn instanceof EntityPlayer) ? (Friends.isFriend(entityIn.func_70005_c_()) ? 49151 : 16777215) : 16777215);
        }
        else {
            fontRendererIn.func_78276_b(str, -j, 10, 16755200);
        }
        if (entityIn instanceof EntityPlayer && this.armor.getValue()) {
            this.renderArmor((EntityPlayer)entityIn, 0, -(fontRendererIn.field_78288_b + 1) - 20);
        }
        GlStateManager.func_187432_a(0.0f, 0.0f, 0.0f);
        GL11.glTranslatef(0.0f, 20.0f, 0.0f);
        GlStateManager.func_179152_a(-40.0f, -40.0f, 40.0f);
        GlStateManager.func_179126_j();
        GlStateManager.func_179121_F();
    }
    
    public void renderArmor(final EntityPlayer player, int x, final int y) {
        final InventoryPlayer items = player.field_71071_by;
        final ItemStack inHand = player.func_184614_ca();
        final ItemStack boots = items.func_70440_f(0);
        final ItemStack leggings = items.func_70440_f(1);
        final ItemStack body = items.func_70440_f(2);
        final ItemStack helm = items.func_70440_f(3);
        final ItemStack offHand = player.func_184592_cb();
        ItemStack[] stuff = null;
        if (inHand != null && offHand != null) {
            stuff = new ItemStack[] { inHand, helm, body, leggings, boots, offHand };
        }
        else if (inHand != null && offHand == null) {
            stuff = new ItemStack[] { inHand, helm, body, leggings, boots };
        }
        else if (inHand == null && offHand != null) {
            stuff = new ItemStack[] { helm, body, leggings, boots, offHand };
        }
        else {
            stuff = new ItemStack[] { helm, body, leggings, boots };
        }
        final List<ItemStack> stacks = new ArrayList<ItemStack>();
        ItemStack[] array;
        for (int length = (array = stuff).length, j = 0; j < length; ++j) {
            final ItemStack i = array[j];
            if (i != null && i.func_77973_b() != null) {
                stacks.add(i);
            }
        }
        final int width = 16 * stacks.size() / 2;
        x -= width;
        GlStateManager.func_179097_i();
        for (final ItemStack stack : stacks) {
            this.renderItem(stack, x, y);
            x += 16;
        }
        GlStateManager.func_179126_j();
    }
    
    public void renderItem(final ItemStack stack, final int x, int y) {
        final FontRenderer fontRenderer = Nametags.mc.field_71466_p;
        final RenderItem renderItem = Nametags.mc.func_175599_af();
        final EnchantEntry[] enchants = { new EnchantEntry(Enchantments.field_180310_c, "Pro"), new EnchantEntry(Enchantments.field_92091_k, "Thr"), new EnchantEntry(Enchantments.field_185302_k, "Sha"), new EnchantEntry(Enchantments.field_77334_n, "Fia"), new EnchantEntry(Enchantments.field_180313_o, "Knb"), new EnchantEntry(Enchantments.field_185307_s, "Unb"), new EnchantEntry(Enchantments.field_185309_u, "Pow"), new EnchantEntry(Enchantments.field_77329_d, "Fpr"), new EnchantEntry(Enchantments.field_180309_e, "Fea"), new EnchantEntry(Enchantments.field_185297_d, "Bla"), new EnchantEntry(Enchantments.field_180308_g, "Ppr"), new EnchantEntry(Enchantments.field_185298_f, "Res"), new EnchantEntry(Enchantments.field_185299_g, "Aqu"), new EnchantEntry(Enchantments.field_185300_i, "Dep"), new EnchantEntry(Enchantments.field_185301_j, "Fro"), new EnchantEntry(Enchantments.field_190941_k, "Bin"), new EnchantEntry(Enchantments.field_185303_l, "Smi"), new EnchantEntry(Enchantments.field_180312_n, "Ban"), new EnchantEntry(Enchantments.field_185304_p, "Loo"), new EnchantEntry(Enchantments.field_191530_r, "Swe"), new EnchantEntry(Enchantments.field_185305_q, "Eff"), new EnchantEntry(Enchantments.field_185306_r, "Sil"), new EnchantEntry(Enchantments.field_185308_t, "For"), new EnchantEntry(Enchantments.field_185311_w, "Fla"), new EnchantEntry(Enchantments.field_151370_z, "Luc"), new EnchantEntry(Enchantments.field_151369_A, "Lur"), new EnchantEntry(Enchantments.field_185296_A, "Men"), new EnchantEntry(Enchantments.field_190940_C, "Van"), new EnchantEntry(Enchantments.field_185310_v, "Pun") };
        GlStateManager.func_179094_E();
        GlStateManager.func_179094_E();
        final float scale1 = 0.3f;
        GlStateManager.func_179109_b((float)(x - 3), (float)(y + 8), 0.0f);
        GlStateManager.func_179152_a(0.3f, 0.3f, 0.3f);
        GlStateManager.func_179121_F();
        RenderHelper.func_74520_c();
        renderItem.field_77023_b = -100.0f;
        GlStateManager.func_179097_i();
        renderItem.func_175042_a(stack, x, y);
        renderItem.func_180453_a(fontRenderer, stack, x, y, (String)null);
        GlStateManager.func_179126_j();
        GlStateManager.func_179152_a(0.75f, 0.75f, 0.75f);
        if (stack.func_77984_f()) {
            this.drawDamage(stack, x, y);
        }
        GlStateManager.func_179152_a(1.33f, 1.33f, 1.33f);
        EnchantEntry[] array;
        for (int length = (array = enchants).length, i = 0; i < length; ++i) {
            final EnchantEntry enchant = array[i];
            final int level = EnchantmentHelper.func_77506_a(enchant.getEnchant(), stack);
            String levelDisplay = "" + level;
            if (level > 10) {
                levelDisplay = "10+";
            }
            if (level > 0) {
                final float scale2 = 0.32f;
                GlStateManager.func_179109_b((float)(x - 1), (float)(y + 2), 0.0f);
                GlStateManager.func_179152_a(0.42f, 0.42f, 0.42f);
                GlStateManager.func_179097_i();
                GlStateManager.func_179140_f();
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                fontRenderer.func_175065_a("§f" + enchant.getName() + " " + levelDisplay, (float)(20 - fontRenderer.func_78256_a("§f" + enchant.getName() + " " + levelDisplay) / 2), 0.0f, Color.WHITE.getRGB(), true);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.func_179145_e();
                GlStateManager.func_179126_j();
                GlStateManager.func_179152_a(2.42f, 2.42f, 2.42f);
                GlStateManager.func_179109_b((float)(-x + 1), (float)(-y), 0.0f);
                y += (int)((fontRenderer.field_78288_b + 3) * 0.28f);
            }
        }
        renderItem.field_77023_b = 0.0f;
        RenderHelper.func_74518_a();
        GlStateManager.func_179141_d();
        GlStateManager.func_179084_k();
        GlStateManager.func_179140_f();
        GlStateManager.func_179121_F();
    }
    
    public void drawDamage(final ItemStack itemstack, final int x, final int y) {
        final float green = (itemstack.func_77958_k() - (float)itemstack.func_77952_i()) / itemstack.func_77958_k();
        final float red = 1.0f - green;
        final int dmg = 100 - (int)(red * 100.0f);
        GlStateManager.func_179097_i();
        Nametags.mc.field_71466_p.func_175063_a(dmg + "", (float)(x + 8 - Nametags.mc.field_71466_p.func_78256_a(dmg + "") / 2), (float)(y - 11), ColourHolder.toHex((int)(red * 255.0f), (int)(green * 255.0f), 0));
        GlStateManager.func_179126_j();
    }
    
    static {
        mc = Minecraft.func_71410_x();
    }
    
    public static class EnchantEntry
    {
        private Enchantment enchant;
        private String name;
        
        public EnchantEntry(final Enchantment enchant, final String name) {
            this.enchant = enchant;
            this.name = name;
        }
        
        public Enchantment getEnchant() {
            return this.enchant;
        }
        
        public String getName() {
            return this.name;
        }
    }
}
