package org.fenci.fencingfplus2.mixin.mixins.render;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import org.fenci.fencingfplus2.features.module.modules.render.Animations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LayerDeadmau5Head.class})
public class MixinLayerDeadmau5Head {
    @Shadow
    private final RenderPlayer playerRenderer;

    public MixinLayerDeadmau5Head(RenderPlayer playerRenderer) {
        this.playerRenderer = playerRenderer;
    }

    @Inject(method = {"doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V"}, at = {@At("HEAD")})
    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci) {
        if (Animations.INSTANCE.getPlayersToGiveEars().contains(entitylivingbaseIn) && entitylivingbaseIn.hasSkin() && !entitylivingbaseIn.isInvisible() && Animations.INSTANCE.isOn()) {
            this.playerRenderer.bindTexture(entitylivingbaseIn.getLocationSkin());

            for (int i = 0; i < 2; ++i) {
                float f = entitylivingbaseIn.prevRotationYaw + (entitylivingbaseIn.rotationYaw - entitylivingbaseIn.prevRotationYaw) * partialTicks - (entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks);
                float f1 = entitylivingbaseIn.prevRotationPitch + (entitylivingbaseIn.rotationPitch - entitylivingbaseIn.prevRotationPitch) * partialTicks;
                GlStateManager.pushMatrix();
                GlStateManager.rotate(f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(f1, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0.375F * (float) (i * 2 - 1), 0.0F, 0.0F);
                GlStateManager.translate(0.0F, -0.375F, 0.0F);
                GlStateManager.rotate(-f1, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(-f, 0.0F, 1.0F, 0.0F);
                float f2 = 1.3333334F;
                GlStateManager.scale(1.3333334F, 1.3333334F, 1.3333334F);
                this.playerRenderer.getMainModel().renderDeadmau5Head(0.0625F);
                GlStateManager.popMatrix();
            }
        }
    }
}