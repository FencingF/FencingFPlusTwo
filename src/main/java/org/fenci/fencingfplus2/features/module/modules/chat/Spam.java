package org.fenci.fencingfplus2.features.module.modules.chat;

import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.ClientMessage;
import org.fenci.fencingfplus2.util.client.Timer;
import org.spongepowered.tools.obfuscation.struct.Message;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

//@author: flixzit
public class Spam extends Module {
    //settngs

    Timer delay = new Timer();

    public static final Setting<Float> timeout = new Setting<>("Delay", 20f, 0, 60);

    //made by flixzit
    public String[] words = new String[] {"you know whats long and hard? not my dick ):", "NOT MY HAIR", "NotFunny.png", "your joking yeah?", "please fuck me!!", "i know someones ip (: my own", "tell me a joke uwu", "Spam!", "trans right > impact", "rusherhack < fencingf+2", "is it funny?", "i own women, as slaves. -Andrew Tate"};

    @Override
    public String getDisplayInfo() {
        return String.valueOf(delay.getTimePassed());
    }

    public Spam() {
        super("Spam", "Allows you to spam a message", Category.Chat);
    }

    Random random = new Random();

    @Override
    public void onUpdate() {
        if (!(mc.player == null || mc.world == null)) {
            //delay
            if (delay.hasReached(timeout.getValue().longValue() * 1000)) {
                mc.player.sendChatMessage(words[random.nextInt(words.length)]);
                delay.reset();
            }
        }
    }
}

