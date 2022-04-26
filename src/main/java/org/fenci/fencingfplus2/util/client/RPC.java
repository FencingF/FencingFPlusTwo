package org.fenci.fencingfplus2.util.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import org.fenci.fencingfplus2.util.Globals;

public class RPC implements Globals {
    private static final String ID = "895540838485073930";

    private static final DiscordRichPresence PRESENCE = new DiscordRichPresence();
    private static final DiscordRPC RPC = DiscordRPC.INSTANCE;

    public static void start() {
        DiscordEventHandlers handler = new DiscordEventHandlers();

        String ip = "Singleplayer";
        if (mc.getCurrentServerData() != null) {
            ip = mc.getCurrentServerData().serverIP;
        }

        handler.disconnected = ((errorCode, message) -> System.out.println("Discord RPC disconnected, errorCode: " + errorCode + ", message: " + message));

        RPC.Discord_Initialize(ID, handler, true, null);

        PRESENCE.startTimestamp = System.currentTimeMillis() / 1000L;
        PRESENCE.details = "Currently duping on " + ip;
        PRESENCE.largeImageKey = "fencingfplusttwoidkanymore";
        PRESENCE.largeImageText = "https://discord.gg/6te3fK3bAu";
        PRESENCE.smallImageKey = "JoeMoma";
        PRESENCE.smallImageText = "Duping";

        RPC.Discord_UpdatePresence(PRESENCE);
    }

    public static void stop() {
        RPC.Discord_Shutdown();
        RPC.Discord_ClearPresence();
    }
}
