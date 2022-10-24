package org.fenci.fencingfplus2.mixin.mixins.entity;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.module.modules.client.Capes;
import org.fenci.fencingfplus2.features.module.modules.render.NoRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

@Mixin({ AbstractClientPlayer.class })
public abstract class MixinAbstractClientPlayer {

    @Inject(method = { "getFovModifier" }, at = { @At("HEAD") }, cancellable = true)
    private void getFovModifierHook(final CallbackInfoReturnable<Float> info) {
        if (NoRender.dynamicFOV.getValue() && NoRender.INSTANCE.isOn()) {
            info.setReturnValue(1.0f);
        }
    }

    @Shadow
    @Nullable
    protected abstract NetworkPlayerInfo getPlayerInfo();

    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    public void getLocationCape(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        UUID uuid = Objects.requireNonNull(getPlayerInfo()).getGameProfile().getId();
        if (Capes.INSTANCE.isOn() && FencingFPlus2.INSTANCE.capeManager.hasCape(uuid)) {
            callbackInfoReturnable.setReturnValue(new ResourceLocation("textures/cape.png"));
        }
    }
}
