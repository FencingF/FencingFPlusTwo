package org.fenci.fencingfplus2.mixin.mixins.misc;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.fenci.fencingfplus2.events.player.EatEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFood.class)
public abstract class MixinItemFood {
    @Inject(method = "onItemUseFinish", at = @At("HEAD"))
    private void onItemUseFinishHook(ItemStack stack, World world, EntityLivingBase entity, CallbackInfoReturnable<ItemStack> info) {
        if (entity instanceof EntityPlayer) {
            MinecraftForge.EVENT_BUS.post(new EatEvent(stack, entity));
        }
    }
}
