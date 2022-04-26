package org.fenci.fencingfplus2.mixin.mixins.render;

import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ItemRenderer.class})
public abstract class MixinItemRender {
//    @Inject(method = { "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V" }, at = { @At("HEAD") })
//    public void renderItemInFirstPersonHook(final AbstractClientPlayer player, final float p_187457_2_, final float p_187457_3_, final EnumHand hand, final float p_187457_5_, final ItemStack stack, final float p_187457_7_, final CallbackInfo info) {
//        if (ViewModel.INSTANCE.isOn() && mc.gameSettings.thirdPersonView == 0 && fullNullCheck()) {
//            GlStateManager.scale(ViewModel.sizex.getValue(), ViewModel.sizey.getValue(), ViewModel.sizez.getValue());
//            GlStateManager.rotate(ViewModel.rotationx.getValue() * 360.0f, 1.0f, 0.0f, 0.0f);
//            GlStateManager.rotate(ViewModel.rotationy.getValue() * 360.0f, 0.0f, 1.0f, 0.0f);
//            GlStateManager.rotate(ViewModel.rotationz.getValue() * 360.0f, 0.0f, 0.0f, 1.0f);
//            GlStateManager.translate(ViewModel.translationx.getValue(), ViewModel.translationy.getValue(), ViewModel.translationz.getValue());
//        }
//    }
}
