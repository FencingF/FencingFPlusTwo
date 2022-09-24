package org.fenci.fencingfplus2;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fenci.fencingfplus2.features.module.modules.chat.Notifier;
import org.fenci.fencingfplus2.features.module.modules.client.DiscordRPC;
import org.fenci.fencingfplus2.features.module.modules.client.HUD;
import org.fenci.fencingfplus2.manager.*;
import org.fenci.fencingfplus2.manager.friend.FriendManager;
import org.lwjgl.opengl.Display;

import java.io.IOException;

@Mod(modid = FencingFPlus2.ID, name = FencingFPlus2.NAME, version = FencingFPlus2.VERSION)
public class FencingFPlus2 {
    public static final String NAME = "FencingF+2";
    public static final String ID = "fencingf+2";
    public static final String VERSION = "2.4.2";
    public static final Logger LOGGER = LogManager.getLogger("FencingFPlus2");

    @Mod.Instance
    public static FencingFPlus2 INSTANCE;

    public ModuleManager moduleManager;
    public CommandManager commandManager;
    public ConfigManager configManager;
    public FriendManager friendManager;
    public KitManager kitManager;
    public TickManager tickManager;
    public TotemPopManager popManager;
    public TrackManager trackerManager;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("Setting up FencingF+2 {}...", VERSION);


        MinecraftForge.EVENT_BUS.register(EventManager.getInstance());

        moduleManager = new ModuleManager();
        MinecraftForge.EVENT_BUS.register(moduleManager);

        commandManager = new CommandManager();
        MinecraftForge.EVENT_BUS.register(commandManager);

        kitManager = new KitManager();

        tickManager = new TickManager();
        MinecraftForge.EVENT_BUS.register(tickManager);

        friendManager = new FriendManager();

        popManager = new TotemPopManager();
        MinecraftForge.EVENT_BUS.register(popManager);

        configManager = new ConfigManager();

        trackerManager = new TrackManager();

        configManager.prepare();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) throws IOException {
        if (!configManager.hasRan()) {
            HUD.INSTANCE.toggle(true);
            DiscordRPC.INSTANCE.toggle(true);
        }
    }

    @Mod.EventHandler
    public void load(FMLLoadCompleteEvent event) {
        LOGGER.info("FML has loaded FencingF+2 v{}...", VERSION);
        Display.setTitle(NAME + " v" + VERSION);
        //TODO : ADD ICON TO DISPLAY CAN'T BE CUMMED

        String icon = String.valueOf(new ResourceLocation(ID, "textures/gui/logo.png"));


    }
}

