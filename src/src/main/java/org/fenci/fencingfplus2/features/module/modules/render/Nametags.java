package org.fenci.fencingfplus2.features.module.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.features.module.modules.client.CustomFont;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.MathUtil;
import org.fenci.fencingfplus2.util.render.ColorUtil;
import org.fenci.fencingfplus2.util.render.RenderUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Objects;

import static org.fenci.fencingfplus2.util.render.ColorUtil.getHealthColor;

public class Nametags extends Module {

    public static final Setting<Float> size = new Setting<>("Size", 2f, 0.1, 10);
    public static final Setting<Boolean> health = new Setting<>("HP", true);
    //public static final Setting<Boolean> armor = new Setting<>("Armor", true);
    public static final Setting<Boolean> items = new Setting<>("Items", true);
    public static final Setting<Boolean> ping = new Setting<>("ping", true);
    public static final Setting<Boolean> gameMode = new Setting<>("GameMode", false);
    public static final Setting<Boolean> rainbowOutline = new Setting<>("RainbowOutline", false);
    public static Nametags INSTANCE;

    public Nametags() {
        super("Nametags", "Shows player information above their heads", Category.Render);
        INSTANCE = this;
    }
//very cool
    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (!fullNullCheck()) return;
        for (final EntityPlayer p : mc.world.playerEntities) {
            if (p != mc.getRenderViewEntity() && p.isEntityAlive()) {
                final double pX = p.lastTickPosX + (p.posX - p.lastTickPosX) * (Nametags.mc).timer.renderPartialTicks - (Nametags.mc.getRenderManager()).renderPosX;
                final double pY = p.lastTickPosY + (p.posY - p.lastTickPosY) * (Nametags.mc).timer.renderPartialTicks - (Nametags.mc.getRenderManager()).renderPosY;
                final double pZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * (Nametags.mc).timer.renderPartialTicks - (Nametags.mc.getRenderManager()).renderPosZ;
                if (p.getName().startsWith("Body #")) {
                    continue;
                }
                this.renderNametag(p, pX, pY, pZ);
            }
        }
    }

    private void renderNametag(final EntityPlayer player, final double x, final double y, final double z) {
        GL11.glPushMatrix();
        String name = (getFencing().friendManager.isFriend(player.getUniqueID()) ? ChatFormatting.AQUA : ChatFormatting.WHITE) + player.getName();
        if (gameMode.getValue()) {
            name = name + "" + this.getGMText(player) + "§f";
        }
        if (ping.getValue()) {
            name = name + " " + this.getPing(player) + "ms";
        }
        if (health.getValue()) {
            name = name + " §r" + MathHelper.ceil(player.getHealth() + player.getAbsorptionAmount());
        }
        final float var14 = 0.016666668f * this.getNametagSize(player);
        GL11.glTranslated((float) x, (float) y + 2.5, (float) z);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-var14, -var14, var14);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GL11.glDisable(2929);
        Color c;
        if (rainbowOutline.getValue()) {
            c = ColorUtil.releasedDynamicRainbow(0, 255f, 255f);
        } else {
            c = new Color(0, 0, 0, 140);
        }
        final int width = RenderUtil.getStringWidth(name) / 2;

        RenderUtil.drawBorderedRect(-width - 3, 8.0, width + 2, 20.0, 1.2, 1962934272, c.getRGB());

        if (CustomFont.INSTANCE.isOn()) {
            FencingFPlus2.INSTANCE.fontManager.drawStringWithShadow(name, (float) (-width), 10.0f, getHealthColor(player));
        } else {
            mc.fontRenderer.drawStringWithShadow(name, (float) (-width), 10.0f, getHealthColor(player));
        }

        int xOffset = 0;
        for (final ItemStack armourStack : player.inventory.armorInventory) {
            if (armourStack != null) {
                xOffset -= 8;
            }
        }
        player.getHeldItemMainhand();
        xOffset -= 8;
        final ItemStack renderStack = player.getHeldItemMainhand().copy();
        this.renderItem(renderStack, xOffset, -10);
        xOffset += 16;
        for (int index = 3; index >= 0; --index) {
            final ItemStack armourStack2 = player.inventory.armorInventory.get(index);
            final ItemStack renderStack2 = armourStack2.copy();
            this.renderItem(renderStack2, xOffset, -10);
            xOffset += 16;
        }
        player.getHeldItemOffhand();
        xOffset += 0;
        final ItemStack renderOffhand = player.getHeldItemOffhand().copy();
        this.renderItem(renderOffhand, xOffset, -10);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private float getNametagSize(final EntityLivingBase player) {
        final ScaledResolution scaledRes = new ScaledResolution(Nametags.mc);
        final double twoDscale = scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 0.0 + size.getValue());
        return (float) twoDscale + Nametags.mc.player.getDistance(player) / 5.6f;
    }

    public String getGMText(final EntityPlayer player) {
        if (player.isCreative()) {
            return " [C]";
        }
        if (player.isSpectator()) {
            return " [I]";
        }
        if (!player.isAllowEdit() && !player.isSpectator()) {
            return " [A]";
        }
        if (!player.isCreative() && !player.isSpectator() && player.isAllowEdit()) {
            return " [S]";
        }
        return "";
    }

    private void renderItem(final ItemStack stack, final int x, final int y) {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        mc.getRenderItem().zLevel = -100.0f;
        GlStateManager.scale(1.0f, 1.0f, 0.01f);
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y / 2 - 12);
        mc.getRenderItem().renderItemOverlays(mc.fontRenderer, stack, x, y / 2 - 12);
        mc.getRenderItem().zLevel = 0.0f;
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        this.renderEnchantText(stack, x, y - 18);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
    }

    public int getPing(final EntityPlayer player) {
        int ping = 0;
        try {
            ping = (int) MathUtil.clamp((float) Objects.requireNonNull(mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime(), 1.0f, 300.0f);
        } catch (NullPointerException ex) {
        }
        return ping;
    }

    private void renderEnchantText(final ItemStack stack, final int x, final int y) {
        int encY = y - 24;
        int yCount = encY + 5;
        if (stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemTool) {
            final float green = (stack.getMaxDamage() - (float) stack.getItemDamage()) / stack.getMaxDamage();
            final float red = 1.0f - green;
            final int dmg = 100 - (int) (red * 100.0f);
            assert red <= 255;
            assert green <= 255;
            RenderUtil.drawStringWithShadow(dmg + "%", x * 2 + 8, y + 26, new Color((int) (red * 255.0f), (int) (green * 255.0f), 0).getRGB());
        }
        final NBTTagList enchants = stack.getEnchantmentTagList();
        for (int index = 0; index < enchants.tagCount(); ++index) {
            final short id = enchants.getCompoundTagAt(index).getShort("id");
            final short level = enchants.getCompoundTagAt(index).getShort("lvl");
            final Enchantment enc = Enchantment.getEnchantmentByID(id);
            if (enc != null) {
                String encName = enc.isCurse() ? (TextFormatting.RED + enc.getTranslatedName(level).substring(11).substring(0, 1).toLowerCase()) : enc.getTranslatedName(level).substring(0, 1).toLowerCase();
                encName += level;
                GL11.glPushMatrix();
                GL11.glScalef(0.9f, 0.9f, 0.0f);
                RenderUtil.drawStringWithShadow(encName, x * 2 + 13, yCount, -1);
                GL11.glScalef(1.0f, 1.0f, 1.0f);
                GL11.glPopMatrix();
                encY += 8;
                yCount -= 10;
            }
        }
    }
}
