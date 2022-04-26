package org.fenci.fencingfplus2.mixin.mixins.entity;


import org.fenci.fencingfplus2.features.module.modules.render.Nametags;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderPlayer.class })
public abstract class MixinRenderPlayer
{
    @Inject(method = {"renderEntityName"}, at = { @At("HEAD") }, cancellable = true)
    public void renderLivingLabel(final AbstractClientPlayer entityIn, final double x, final double y, final double z, final String name, final double distanceSq, final CallbackInfo info) {
        if (Nametags.INSTANCE.isOn()) {
            info.cancel();
        }
    }
}
