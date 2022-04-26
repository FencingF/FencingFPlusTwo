package org.fenci.fencingfplus2.events.player;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class EventRenderEntity extends Event {
    public final Entity entity;
    public final Type type;

    public EventRenderEntity(Entity e, Type t) {
        entity = e;
        type = t;
    }

    public enum Type {
        TEXTURE, COLOR
    }

    public Entity getEntity() {
        return entity;
    }

    public Type getType() {
        return type;
    }

    public static class Head extends EventRenderEntity {
        public Head(Entity e, Type t) {
            super(e, t);
        }
    }

    public static class Return extends EventRenderEntity {
        public Return(Entity e, Type t) {
            super(e, t);
        }
    }
}
