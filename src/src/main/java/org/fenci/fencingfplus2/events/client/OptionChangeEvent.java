package org.fenci.fencingfplus2.events.client;

import net.minecraftforge.fml.common.eventhandler.Event;
import org.fenci.fencingfplus2.setting.Setting;
//idk what this is for
public class OptionChangeEvent extends Event {
    private final Setting setting;

    public OptionChangeEvent(Setting setting) {
        this.setting = setting;
    }

    public Setting getOption() {
        return setting;
    }
}
