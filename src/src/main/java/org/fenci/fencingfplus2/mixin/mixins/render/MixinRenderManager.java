package org.fenci.fencingfplus2.mixin.mixins.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraftforge.common.MinecraftForge;
import org.fenci.fencingfplus2.events.player.EventRenderEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderManager.class)
public class MixinRenderManager {
    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    public void renderEntityHead(Entity entityIn, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_, CallbackInfo callbackInfo) {
        EventRenderEntity.Head eventRenderEntity = new EventRenderEntity.Head(entityIn, EventRenderEntity.Type.TEXTURE);
        MinecraftForge.EVENT_BUS.post(eventRenderEntity);
        if (entityIn instanceof EntityEnderPearl || entityIn instanceof EntityXPOrb || entityIn instanceof EntityExpBottle || entityIn instanceof EntityEnderCrystal) {
            EventRenderEntity.Head eventRenderEntity1 = new EventRenderEntity.Head(entityIn, EventRenderEntity.Type.COLOR);
            MinecraftForge.EVENT_BUS.post(eventRenderEntity1);
            if (eventRenderEntity1.isCanceled()) {
                callbackInfo.cancel();
            }
        }
        if (eventRenderEntity.isCanceled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "renderEntity", at = @At("RETURN"), cancellable = true)
    public void renderEntityReturn(Entity entityIn, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_, CallbackInfo callbackInfo) {
        EventRenderEntity.Return eventRenderEntityReturn = new EventRenderEntity.Return(entityIn, EventRenderEntity.Type.TEXTURE);
        MinecraftForge.EVENT_BUS.post(eventRenderEntityReturn);
        if (entityIn instanceof EntityEnderPearl || entityIn instanceof EntityXPOrb || entityIn instanceof EntityExpBottle || entityIn instanceof EntityEnderCrystal) {
            EventRenderEntity.Return eventRenderEntityReturn1 = new EventRenderEntity.Return(entityIn, EventRenderEntity.Type.COLOR);
            MinecraftForge.EVENT_BUS.post(eventRenderEntityReturn1);
            if (eventRenderEntityReturn1.isCanceled()) {
                callbackInfo.cancel();
            }
        }
        if (eventRenderEntityReturn.isCanceled()) {
            callbackInfo.cancel();
        }
    }
}
