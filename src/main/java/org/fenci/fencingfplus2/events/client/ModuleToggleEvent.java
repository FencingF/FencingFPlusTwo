package org.fenci.fencingfplus2.events.client;

import net.minecraftforge.fml.common.eventhandler.Event;
import org.fenci.fencingfplus2.features.module.Module;

public class ModuleToggleEvent extends Event {
    private final Module module;

    public ModuleToggleEvent(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return module;
    }
}
