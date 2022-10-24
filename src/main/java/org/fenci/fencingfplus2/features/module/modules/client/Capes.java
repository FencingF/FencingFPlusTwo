package org.fenci.fencingfplus2.features.module.modules.client;

import org.fenci.fencingfplus2.features.module.Module;

public class Capes extends Module {

    public static Capes INSTANCE;

    public Capes() {
        super("Capes", "Turns on capes", Category.Client);
        INSTANCE = this;
    }
}
