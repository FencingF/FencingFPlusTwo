package org.fenci.fencingfplus2.mixin.mixins.entity;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import org.fenci.fencingfplus2.events.player.ClickBlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {
    @Inject(method = "clickBlock", at = @At(value = "HEAD"))
    public void clickBlock(BlockPos loc, EnumFacing face, CallbackInfoReturnable<Boolean> cir) {
        MinecraftForge.EVENT_BUS.post(new ClickBlockEvent(loc, face));
    }
}