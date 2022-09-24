package org.fenci.fencingfplus2.mixin.mixins.entity;

import net.minecraft.client.entity.AbstractClientPlayer;
import org.fenci.fencingfplus2.features.module.modules.render.NoRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ AbstractClientPlayer.class })
public abstract class MixinAbstractClientPlayer {

    @Inject(method = { "getFovModifier" }, at = { @At("HEAD") }, cancellable = true)
    private void getFovModifierHook(final CallbackInfoReturnable<Float> info) {
        if (NoRender.dynamicFOV.getValue() && NoRender.INSTANCE.isOn()) {
            info.setReturnValue(1.0f);
        }
    }
}
