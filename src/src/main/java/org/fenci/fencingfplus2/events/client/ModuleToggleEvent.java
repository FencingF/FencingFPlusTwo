package org.fenci.fencingfplus2.events.client;

import net.minecraftforge.fml.common.eventhandler.Event;
import org.fenci.fencingfplus2.features.module.Module;

// ModuleToggleEvent is a class that extends Event and is used to handle the event of the ModuleToggle command and other modules.
public class ModuleToggleEvent extends Event {
    private final Module module;

    public ModuleToggleEvent(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return module;
    }
}
