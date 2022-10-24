package org.fenci.fencingfplus2.mixin.mixins.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.fenci.fencingfplus2.events.render.SetupFogEvent;
import org.fenci.fencingfplus2.features.module.modules.render.NoRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {EntityRenderer.class})
public abstract class MixinEntityRenderer {

    @Redirect(method = {"setupFog"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ActiveRenderInfo;getBlockStateAtEntityViewpoint(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;F)Lnet/minecraft/block/state/IBlockState;"))
    public IBlockState getBlockStateAtEntityViewpointHook(World worldIn, Entity entityIn, float p_186703_2_) {
        return ActiveRenderInfo.getBlockStateAtEntityViewpoint(worldIn, entityIn, p_186703_2_);
    }

    @Inject(method = {"hurtCameraEffect"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void hurtCameraEffectHook(float ticks, CallbackInfo info) {
        if (NoRender.INSTANCE.isToggled() && NoRender.hurtcam.getValue()) {
            info.cancel();
        }
    }

    @Inject(method = {"updateLightmap"}, at = {@At(value = "HEAD")}, cancellable = true)
    private void updateLightmap(float partialTicks, CallbackInfo info) {
        if (NoRender.INSTANCE.isToggled() && NoRender.skylight.getValue()) {
            info.cancel();
        }
    }

    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    public void setupFog(int startCoords, float partialTicks, CallbackInfo callbackInfo) {
        SetupFogEvent event = new SetupFogEvent();
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isCanceled()) {
            callbackInfo.cancel();
        }
    }
}
