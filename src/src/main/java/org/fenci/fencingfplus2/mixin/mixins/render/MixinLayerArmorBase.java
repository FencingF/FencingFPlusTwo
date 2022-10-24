package org.fenci.fencingfplus2.mixin.mixins.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import org.fenci.fencingfplus2.events.render.RenderArmorEvent;
import org.fenci.fencingfplus2.features.module.modules.render.NoRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public class MixinLayerArmorBase {
    @Inject(method = "renderEnchantedGlint", at = @At("HEAD"), cancellable = true)
    private static void renderEnchantedGlint(RenderLivingBase<?> p_188364_0_, EntityLivingBase p_188364_1_, ModelBase model, float p_188364_3_, float p_188364_4_, float p_188364_5_, float p_188364_6_, float p_188364_7_, float p_188364_8_, float p_188364_9_, CallbackInfo info) {
        if (NoRender.INSTANCE.isOn() && NoRender.enchantGlint.getValue()) {
            info.cancel();
        }
    }

    @Inject(method = "doRenderLayer", at = @At("HEAD"), cancellable = true)
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo info) {
        RenderArmorEvent event = new RenderArmorEvent(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }
}
