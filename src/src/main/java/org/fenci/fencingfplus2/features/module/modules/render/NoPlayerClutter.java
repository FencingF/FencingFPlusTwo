package org.fenci.fencingfplus2.features.module.modules.render;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.render.RenderArmorEvent;
import org.fenci.fencingfplus2.events.render.RenderLivingEntityEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

import java.util.HashSet;
import java.util.Set;

public class NoPlayerClutter extends Module {

    public static final Setting<Boolean> removeArmor = new Setting<>("NoArmor", true);
    public static final Setting<Boolean> texture = new Setting<>("Texture", true);

    public NoPlayerClutter() {
        super("NoPlayerClutter", "Removes player clutter", Category.Render);
    }

    @SubscribeEvent
    public void onRenderLivingEntity(RenderLivingEntityEvent event) {
        if (!fullNullCheck() || !(event.getEntityLivingBase() instanceof EntityOtherPlayerMP)) return;
        for (EntityPlayer player : getPlayers()) {
            if (event.getEntityLivingBase().equals(player)) {
                event.setCanceled(!texture.getValue());
                GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
        }
    }

    @SubscribeEvent
    public void onArmorRenderEvent(RenderArmorEvent event) {
        if (!removeArmor.getValue()) return;
        for (EntityPlayer player : getPlayers()) {
            if (event.getEntity().equals(player)) {
                event.setCanceled(true);
            }
        }
    }

    public Set<EntityPlayer> getPlayers() {

        Set<EntityPlayer> players = new HashSet<>();

        for (EntityPlayer player : mc.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(mc.player.boundingBox.minX, mc.player.boundingBox.minY, mc.player.boundingBox.minZ, mc.player.boundingBox.maxX, mc.player.boundingBox.maxY, mc.player.boundingBox.maxZ))) {
            if (player.getEntityId() == mc.player.entityId || player.isDead) continue;
            players.add(player);
        }
        return players;
    }
}
