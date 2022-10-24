package org.fenci.fencingfplus2.mixin.mixins.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import org.fenci.fencingfplus2.events.player.EventRenderEntity;
import org.fenci.fencingfplus2.events.render.RenderLivingEntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RenderLivingBase.class})
public abstract class MixinRenderLivingBase {

    @Shadow
    protected ModelBase mainModel;

    @Inject(method = {"renderModel"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V")}, cancellable = true)
    private void renderModel(EntityLivingBase entityLivingBase, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, CallbackInfo info) {
        RenderLivingEntityEvent renderLivingEntityEvent = new RenderLivingEntityEvent(mainModel, entityLivingBase, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        MinecraftForge.EVENT_BUS.post(renderLivingEntityEvent);
        if (renderLivingEntityEvent.isCanceled()) {
            info.cancel();
        }
    }

    @Redirect(method = {"renderModel"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void renderModelHook(final ModelBase modelBase, final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        EventRenderEntity.Head eventRenderEntity = new EventRenderEntity.Head(entityIn, EventRenderEntity.Type.COLOR);
        MinecraftForge.EVENT_BUS.post(eventRenderEntity);
        if (eventRenderEntity.isCanceled()) return;
        modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        EventRenderEntity.Return eventRenderEntity1 = new EventRenderEntity.Return(entityIn, EventRenderEntity.Type.COLOR);
        MinecraftForge.EVENT_BUS.post(eventRenderEntity1);
    }
}
