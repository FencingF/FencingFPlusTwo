package org.fenci.fencingfplus2.mixin.mixins.render;

import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraftforge.common.MinecraftForge;
import org.fenci.fencingfplus2.events.render.BossBarEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiBossOverlay.class})
public class MixinGuiBossOverlay {
    @Inject(method = {"renderBossHealth"}, at = {@At("HEAD")}, cancellable = true)
    private void renderBossHealth(final CallbackInfo ci) {
        final BossBarEvent event = new BossBarEvent();
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}