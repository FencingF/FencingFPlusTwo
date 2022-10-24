package org.fenci.fencingfplus2.mixin.mixins.entity;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.fenci.fencingfplus2.events.player.PushEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = {Entity.class})
public abstract class MixinEntity {
    @Shadow
    public double posX;
    @Shadow
    public double posY;
    @Shadow
    public double posZ;
    @Shadow
    public float rotationYaw;
    @Shadow
    public float rotationPitch;
    @Shadow
    public World world;
    @Shadow
    public boolean inPortal;

    @Shadow
    public abstract int getMaxInPortalTime();

    @Redirect(method = {"onEntityUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getMaxInPortalTime()I"))
    private int getMaxInPortalTimeHook(Entity entity) {
        return this.getMaxInPortalTime();
    }

    @Redirect(method = {"applyEntityCollision"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void addVelocityHook(Entity entity, double x, double y, double z) {
        PushEvent event = new PushEvent(entity, x, y, z, true, 1);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            entity.motionX += event.x;
            entity.motionY += event.y;
            entity.motionZ += event.z;
            entity.isAirBorne = event.airbone;
        }
    }
}