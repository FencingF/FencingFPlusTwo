package org.fenci.fencingfplus2.mixin.mixins.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.module.modules.misc.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Inject(method = "shutdown", at = @At(value = "HEAD"))
    public void shutdown(CallbackInfo ci) {
        FencingFPlus2.INSTANCE.configManager.save();
        //Covenant.INSTANCE.configManager.saveFriends();
        FencingFPlus2.INSTANCE.configManager.savePrefix();
        //FencingFPlus2.INSTANCE.configManager.saveFriends();
        FakePlayer.INSTANCE.setToggled(false);
    }

    @Inject(method = "crashed", at = @At(value = "HEAD"))
    public void crashed(CrashReport crash, CallbackInfo ci) {
        FencingFPlus2.INSTANCE.configManager.save();
        //Covenant.INSTANCE.configManager.saveFriends();
        FencingFPlus2.INSTANCE.configManager.savePrefix();
        //FencingFPlus2.INSTANCE.configManager.saveFriends();
        FakePlayer.INSTANCE.setToggled(false);
    }
}