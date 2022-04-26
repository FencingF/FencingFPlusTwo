package org.fenci.fencingfplus2.mixin.mixins.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import org.fenci.fencingfplus2.events.player.ElytraEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityLivingBase.class, priority = Integer.MAX_VALUE)
public abstract class MixinEntityLivingBase extends MixinEntity {

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void onTravel(float strafe, float vertical, float forward, CallbackInfo ci) {
        ElytraEvent event = new ElytraEvent((EntityLivingBase) (Object) this);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
