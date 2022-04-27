package org.fenci.fencingfplus2.features.module.modules.render;

import com.mojang.authlib.GameProfile;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.network.ConnectionEvent;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.features.module.modules.client.Preferences;
import org.fenci.fencingfplus2.util.render.PopChamsUtil;
import org.fenci.fencingfplus2.util.render.RenderUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LogoutSpots extends Module {
    public LogoutSpots() {
        super("LogoutSpots", "Shows player logouts", Category.Render);
    }

    private final List<LogoutSpot> logoutSpots = new CopyOnWriteArrayList<>();

    public ModelPlayer playerModel;
    Color lineColorS;
    Color fillColorS;

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event) {
        if (!fullNullCheck()) return;
        for (LogoutSpot logoutSpot : logoutSpots) {
            if (logoutSpot.getPlayer() != null && logoutSpot.getPlayer().getEntityId() != LogoutSpots.mc.player.getEntityId()) {
                final double pX = logoutSpot.getPlayer().lastTickPosX + (logoutSpot.getPlayer().posX - logoutSpot.getPlayer().lastTickPosX) * (Nametags.mc).timer.renderPartialTicks - (Nametags.mc.getRenderManager()).renderPosX;
                final double pY = logoutSpot.getPlayer().lastTickPosY + (logoutSpot.getPlayer().posY - logoutSpot.getPlayer().lastTickPosY) * (Nametags.mc).timer.renderPartialTicks - (Nametags.mc.getRenderManager()).renderPosY;
                final double pZ = logoutSpot.getPlayer().lastTickPosZ + (logoutSpot.getPlayer().posZ - logoutSpot.getPlayer().lastTickPosZ) * (Nametags.mc).timer.renderPartialTicks - (Nametags.mc.getRenderManager()).renderPosZ;
                renderNametag(logoutSpot.getPlayer(), pX, pY, pZ);
                GameProfile profile = new GameProfile(mc.player.getUniqueID(), "");
                EntityOtherPlayerMP player = new EntityOtherPlayerMP(mc.world, profile);
                player.setLocationAndAngles(logoutSpot.getX(), logoutSpot.getY(), logoutSpot.getZ(), logoutSpot.getPlayer().rotationYaw, logoutSpot.getPlayer().rotationPitch);
                this.playerModel = new ModelPlayer(0.0f, false);
                this.playerModel.bipedHead.showModel = false;
                this.playerModel.bipedBody.showModel = false;
                this.playerModel.bipedLeftArmwear.showModel = false;
                this.playerModel.bipedLeftLegwear.showModel = false;
                this.playerModel.bipedRightArmwear.showModel = false;
                this.playerModel.bipedRightLegwear.showModel = false;

                GL11.glLineWidth(1.0f);
                if (!getFencing().friendManager.isFriend(logoutSpot.getPlayer().getUniqueID())) {
                    lineColorS = new Color(184, 15, 10, 50);
                    fillColorS = new Color(184, 15, 10, 50);
                }
                if (getFencing().friendManager.isFriend(logoutSpot.getPlayer().getUniqueID())) {
                    lineColorS = new Color(57, 236, 255, 50);
                    fillColorS = new Color(57, 236, 255, 50);
                }
                int lineA = lineColorS.getAlpha();
                int fillA = fillColorS.getAlpha();
                Color lineColor = PopChamsUtil.newAlpha(lineColorS, lineA);
                Color fillColor = PopChamsUtil.newAlpha(fillColorS, fillA);
                if (this.playerModel != null) {
                    RenderUtil.TessellatorUtil.prepareGL();
                    GL11.glPushAttrib(1048575);
                    GL11.glEnable(2881);
                    GL11.glEnable(2848);
                    Color outlineFinal = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), 75);
                    Color fillFinal = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), 75);
                    PopChamsUtil.glColor(fillFinal);
                    GL11.glPolygonMode(1032, 6914);
                    PopChamsUtil.renderEntity(player, this.playerModel, player.limbSwing, player.limbSwingAmount,  player.ticksExisted,  90,  90, 1.0f);
                    PopChamsUtil.glColor(outlineFinal);
                    GL11.glPolygonMode(1032, 6913);
                    PopChamsUtil.renderEntity(player, this.playerModel,  player.limbSwing,  player.limbSwingAmount,  player.ticksExisted,  90,  90, 1.0f);
                    GL11.glPolygonMode(1032, 6914);
                    GL11.glPopAttrib();
                    RenderUtil.TessellatorUtil.releaseGL();
                }
            }
        }
    }

    private void renderNametag(final EntityPlayer player, final double x, final double y, final double z) {
        GL11.glPushMatrix();
        String name = (getFencing().friendManager.isFriend(player.getUniqueID()) ? ChatFormatting.AQUA : ChatFormatting.WHITE) + player.getName();
        final float var14 = 0.016666668f * getNametagSize(player);
        GL11.glTranslated((float)x, (float)y + 2.5, (float)z);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-var14, -var14, var14);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GL11.glDisable(2929);
        Color c = new Color(0, 0, 0, 140);

        final int width = RenderUtil.getStringWidth(name) / 2;

        RenderUtil.drawBorderedRect(-width - 3, 8.0, width + 2, 20.0, 1.2, 1962934272, c.getRGB());

        mc.fontRenderer.drawStringWithShadow(name, (float)(-width), 10.0f, new Color(161, 61, 168).getRGB());

        player.getHeldItemMainhand();
        player.getHeldItemOffhand();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private float getNametagSize(final EntityLivingBase player) {
        final ScaledResolution scaledRes = new ScaledResolution(Nametags.mc);
        final double twoDscale = scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 0.0 + 13);
        return (float)twoDscale + Nametags.mc.player.getDistance(player) / 5.6f;
    }

    @SubscribeEvent
    public void onConnectionEvent(ConnectionEvent event) {
        if (!fullNullCheck()) return;
        if (event.getType().equals(ConnectionEvent.Type.Leave)) {
            if (event.getEntity().equals(mc.player)) {
                logoutSpots.clear();
            } else {
                EntityPlayer player = event.getEntity();
                double x = event.getEntity().posX;
                double y = event.getEntity().posY;
                double z = event.getEntity().posZ;
                logoutSpots.add(new LogoutSpot(player, x, y, z));
            }
        }
        if (event.getType().equals(ConnectionEvent.Type.Join)) {
            logoutSpots.removeIf(pos -> pos.getPlayer().getName().equalsIgnoreCase(event.getName()));
        }
    }

    @Override
    public void onDisable() {
        if (Preferences.logoutSpotsRemoveOnDisable.getValue()) {
            logoutSpots.clear();
        }
    }

    public static class LogoutSpot {
        private final EntityPlayer player;
        private final double x;
        private final double y;
        private final double z;

        public LogoutSpot(EntityPlayer player, double x, double y, double z) {
            this.player = player;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public EntityPlayer getPlayer() {
            return player;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }
    }
}
