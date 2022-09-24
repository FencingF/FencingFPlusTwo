package org.fenci.fencingfplus2.features.module.modules.client;


import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.util.client.RPC;

public class DiscordRPC extends Module {
    public static DiscordRPC INSTANCE;

    public DiscordRPC() {
        super("DiscordRPC", "Makes a Discord Rich Presence", Category.Client);
        INSTANCE = this;
    }

    public void onEnable() {
        RPC.start();
    }

    public void onDisable() {
        RPC.stop();
    }
}
