package org.fenci.fencingfplus2.events.player;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EatEvent extends Event {

    private final ItemStack stack;
    private final EntityLivingBase entity;

    public EatEvent(ItemStack stack, EntityLivingBase entity) {
        this.stack = stack;
        this.entity = entity;
    }

    public ItemStack getStack() {
        return stack;
    }

    public EntityLivingBase getPlayer() {
        return entity;
    }
}
