package org.fenci.fencingfplus2.manager;

import com.google.common.collect.Lists;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.features.module.modules.chat.*;
import org.fenci.fencingfplus2.features.module.modules.client.*;
import org.fenci.fencingfplus2.features.module.modules.combat.*;
import org.fenci.fencingfplus2.features.module.modules.exploit.*;
import org.fenci.fencingfplus2.features.module.modules.misc.*;
import org.fenci.fencingfplus2.features.module.modules.movement.*;
import org.fenci.fencingfplus2.features.module.modules.player.*;
import org.fenci.fencingfplus2.features.module.modules.render.*;
import org.fenci.fencingfplus2.util.Globals;
import org.fenci.fencingfplus2.util.tracking.TrackerManager;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class ModuleManager implements Globals {
    private final ArrayList<Module> modules;
    Module module2;

    {
        modules = Lists.newArrayList(

                // client
                new ClickGUI(),
                new DataLink(),
                new DiscordRPC(),
                new HUD(),
                new Preferences(),

                // combat
                new Aura(),
                new AutoArmor(),
                new AutoCity(),
                new AutoCrystal(),
                //new AutoCrystalRewrite(),
                //new AutoWeb(),
                new BowAim(),
                new BowSpam(),
                //new Burrow(),
                new Criticals(),
                new FastProjectile(),
                new HoleFiller(),
                new HoleSnap(),
                new Offhand(),
                new PacketMine(),
                new SelfWeb(),
                new StrictTotem(),
                new Surround(),

                // movement
                new AutoJump(),
                new TrackerManager(),
                new AutoWalk(),
                new ElytraFly(),
                //new ElytraFly2b2t(),
                new GroundStrafe(),
                new NoFall(),
                new NoSlow(),
                new ReverseStep(),
                new Speed(),
                new Step(),
                new Sprint(),
                new Velocity(),

                // player
                new AntiAFK(),
                new AntiVoid(),
                new AutoLog(),
                new AutoRespawn(),
                new ChestSwap(),
                new FastPlace(),
                new MiddleClick(),
                new PacketCancel(),
                new ViewLock(),

                //render
                new Animations(),
                //new Chams(),
                new CityESP(),
                new CustomRenderDistance(),
                new ExtraTab(),
                new Fullbright(),
                new HoleESP(),
                new LightningDeath(),
                new LogoutSpots(),
                new Nametags(),
                new NoPlayerClutter(),
                new NoRender(),
                //new PlayerTrails(),
                new PopChams(),
                //new ViewModel(),
                //new VoidESP(),

                //misc
                new AntiLevitation(),
                new AutoKit(),

                new FakePlayer(),
                //new HotbarReplenish(),
                new NickHider(),
                //new NoPause(),
                new PacketLogger(),
                //new PingSpoof(),
                //new StashMover(),
                new Timer(),
                new TotemName(),
                new XCarry(),

                //exploit
                //new AntiWeakness(),
                //new AutoDupe(),
                new Crash(),
                new ChorusControl(),
                //new ChunkLoader(),
                //new ChorusPredict(),
                //new FiveBTP(),

                new HitboxCity(),
                new NoPacketTP(),
                new PearlBait(),

                //chat
                new AntiLog4J(),
                new AutoEZ(),
                new CustomChat(),
                new Notifier(),
                new PopLag(),
                new Spam()

        );
    }

    public ModuleManager() {
        modules.forEach(Module::register);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        int code = Keyboard.getEventKey();
        if (mc.currentScreen == null && code != Keyboard.KEY_NONE && !Keyboard.getEventKeyState()) {
            for (Module module : modules) {
                if (module.getKeybind() == code) {
                    module.toggle(false);
                }
            }
        }
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public Module getModuleByName(String module) {
        for (Module module1 : getModules()) {
            if (module1.getName().equalsIgnoreCase(module)) {
                module2 = module1;
            }
        }
        return module2;
    }
}
