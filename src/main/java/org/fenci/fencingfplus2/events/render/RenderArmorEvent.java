package org.fenci.fencingfplus2.events.render;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class RenderArmorEvent extends Event {
    EntityLivingBase entity;
    float limbSwing;
    float limbSwingAmount;
    float partialTicks;
    float ageInTicks;
    float netHeadYaw;
    float headPitch;
    float scale;

    public RenderArmorEvent(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.entity = entity;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.partialTicks = partialTicks;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
        this.scale = scale;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }

    public float getAgeInTicks() {
        return ageInTicks;
    }

    public float getHeadPitch() {
        return headPitch;
    }

    public float getLimbSwing() {
        return limbSwing;
    }

    public float getLimbSwingAmount() {
        return limbSwingAmount;
    }

    public float getNetHeadYaw() {
        return netHeadYaw;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public float getScale() {
        return scale;
    }
}
