package org.fenci.fencingfplus2.util.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreenServerList;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.util.Globals;

public class RPC implements Globals {
    private static final DiscordRichPresence presence = new DiscordRichPresence();
    private static final DiscordRPC rpc = DiscordRPC.INSTANCE;

    public static void start() {
        DiscordEventHandlers handler = new DiscordEventHandlers();
        rpc.Discord_Initialize("991384089586319370", handler, true, null);
        presence.startTimestamp = System.currentTimeMillis() / 1000L;
        presence.largeImageKey = "large";
        presence.largeImageText = FencingFPlus2.VERSION;
        String server = "In Server Menu";
        rpc.Discord_UpdatePresence(presence);
        Thread thread = new Thread(() ->
        {
            while (!Thread.currentThread().isInterrupted()) {
                rpc.Discord_RunCallbacks();
                presence.details = mc.currentScreen instanceof GuiMainMenu ? "In the Main Menu" : mc.currentScreen instanceof GuiMultiplayer ? server : mc.currentScreen instanceof GuiScreenServerList ? server : "Duping " + (mc.getCurrentServerData() != null ? ("on " + mc.getCurrentServerData().serverIP) : "In Singleplayer");
                rpc.Discord_UpdatePresence(presence);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
            }
        }, "RPC-Callback-Handler");
        thread.start();
        handler.disconnected = ((errorCode, message) -> System.out.println("Discord RPC disconnected, errorCode: " + errorCode + ", message: " + message));
    }

    public static void stop() {
        rpc.Discord_Shutdown();
        rpc.Discord_ClearPresence();
    }
}



