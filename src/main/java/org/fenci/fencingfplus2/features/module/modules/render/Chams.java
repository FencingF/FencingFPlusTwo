package org.fenci.fencingfplus2.features.module.modules.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.player.EventRenderEntity;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.render.RenderUtil;

public class Chams extends Module {
    public Chams() {
        super("Chams", "Changes the way players render", Category.Render);
    }

    public static final Setting<Boolean> players = new Setting<>("Players", true);
    public static final Setting<Boolean> crystals = new Setting<>("Crystals", true);
    public static final Setting<Boolean> mobs = new Setting<>("Mobs", false);
    public static final Setting<Boolean> transparent = new Setting<>("Transparent", false);
    public static final Setting<Integer> fillred = new Setting<>("Red", 57, 0, 255);
    public static final Setting<Integer> fillgreen = new Setting<>("Green", 236, 0, 255);
    public static final Setting<Integer> fillblue = new Setting<>("Blue", 255, 0, 255);
    public static final Setting<Integer> alpha = new Setting<>("Alpha", 80, 0, 255);
    public static final Setting<Boolean> self = new Setting<>("Self", true);
    public static final Setting<Texture> texture = new Setting<>("Texture", Texture.Color);

    @SubscribeEvent
    public void onRender(EventRenderEntity.Head event) {
        if (event.getType() == EventRenderEntity.Type.COLOR && texture.getValue().equals(Texture.Skin)) return;
        else if (event.getType() == EventRenderEntity.Type.TEXTURE && texture.getValue().equals(Texture.Color)) return;

        if (transparent.getValue()) {
            GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof EntityPlayer && players.getValue() && (self.getValue() || entity != mc.player)) {
            renderChamsPre(true);
        }

        if (mobs.getValue() && (entity instanceof EntityCreature || entity instanceof EntitySlime || entity instanceof EntitySquid)) {
            renderChamsPre(false);
        }

        if (crystals.getValue() && entity instanceof EntityEnderCrystal) {
            renderChamsPre(false);
        }
    }

    @SubscribeEvent
    public void onRender(EventRenderEntity.Return event) {
        if (event.getType() == EventRenderEntity.Type.COLOR && texture.getValue().equals(Texture.Skin)) return;
        else if (event.getType() == EventRenderEntity.Type.TEXTURE && texture.getValue().equals(Texture.Color)) return;
        Entity entity = event.getEntity();
        if (entity instanceof EntityPlayer && players.getValue() && (self.getValue() || entity != mc.player)) {
            renderChamsPost(true);
        }

        if (mobs.getValue() && (entity instanceof EntityCreature || entity instanceof EntitySlime || entity instanceof EntitySquid)) {
            renderChamsPost(false);
        }

        if (crystals.getValue() && entity instanceof EntityEnderCrystal) {
            renderChamsPost(false);
        }
    }

    private void renderChamsPre(boolean isPlayer) {
        if (texture.getValue().equals(Texture.Skin)) {
            RenderUtil.createChamsPre();
        } else if (texture.getValue().equals(Texture.Color)) {
            RenderUtil.createColorPre(isPlayer, fillred.getValue(), fillgreen.getValue(), fillblue.getValue(), alpha.getValue());
        }
    }

    public void renderChamsPost(boolean isPlayer) {
        if (texture.getValue().equals(Texture.Skin)) {
            RenderUtil.createChamsPost();
        } else if (texture.getValue().equals(Texture.Color)) {
            RenderUtil.createColorPost(isPlayer);
        }
    }

    public enum Texture {
        Color, Skin
    }
}
