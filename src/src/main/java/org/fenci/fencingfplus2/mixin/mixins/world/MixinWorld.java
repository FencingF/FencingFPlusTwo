package org.fenci.fencingfplus2.mixin.mixins.world;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.fenci.fencingfplus2.events.player.PushEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = {World.class})
public class MixinWorld {
    @Redirect(method = {"handleMaterialAcceleration"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isPushedByWater()Z"))
    public boolean isPushedbyWaterHook(Entity entity) {
        PushEvent event = new PushEvent(entity, 2);
        MinecraftForge.EVENT_BUS.post(event);
        return entity.isPushedByWater() && !event.isCanceled();
    }
}
