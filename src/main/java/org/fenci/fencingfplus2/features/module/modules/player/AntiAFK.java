package org.fenci.fencingfplus2.features.module.modules.player;

import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.fenci.fencingfplus2.events.client.OptionChangeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.ClientMessage;
import org.fenci.fencingfplus2.util.client.Timer;

import java.util.HashSet;
import java.util.Set;

public class AntiAFK extends Module {
    public static final Setting<Float> timeout = new Setting<>("Delay", 20f, 0, 60);
    public static final Setting<Boolean> stats = new Setting<>("/Stats", true);
    public static final Setting<Boolean> swing = new Setting<>("Swing", false);
    public static final Setting<Boolean> rotate = new Setting<>("Rotate", false);
    public static final Setting<Boolean> afkMessage = new Setting<>("Message", false);
    public static final Setting<Boolean> voicemail = new Setting<>("Voicemail", false);
    public static final Setting<Boolean> autoDetect = new Setting<>("AutoDetectAFK", true);

    public static final Setting<Boolean> RandomMessage = new Setting<>("RandomMessage", true);

    Timer delay = new Timer();
    Timer afkTimer = new Timer();

    public Set<String> voicemailMessages = new HashSet<>();

    public AntiAFK() {
        super("AntiAFK", "Tries to prevent you from getting afk kicked", Category.Player);
    }

    @Override
    public String getDisplayInfo() {
        return String.valueOf(delay.getTimePassed());
    }

    @Override
    public void onUpdate() {
        if (delay.hasReached(timeout.getValue().longValue() * 1000)) {
            if (stats.getValue()) {
                mc.player.sendChatMessage("/stats");
            }
            if (swing.getValue()) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }
            if (rotate.getValue()) {
                mc.player.rotationYaw += 90;
            }
            if (afkMessage.getValue()) {
                if (RandomMessage.getValue()) {
                    //randomly picks 1 message out of 4
                    int random = (int) (Math.random() * 4);
                    switch (random) {
                        case 0:
                            mc.player.sendChatMessage("I am currently afk thanks to FencingF+2! (best client ever)");
                            break;
                        case 1:
                            mc.player.sendChatMessage("not gonna lie ff2 is the best client ever");
                            break;
                        case 2:
                            mc.player.sendChatMessage("I am currently afk thanks to the best client FencingF+2!");
                            break;
                        case 3:
                            mc.player.sendChatMessage("I am currently afk thanks to FencingF+2!");
                            break;
                    }

                } else {
                    mc.player.sendChatMessage("I am currently afk thanks to FencingF+2!");
                }
            }
            delay.reset();
        }
        if (afkTimer.getTimePassed() >= 15000 && autoDetect.getValue()) {
            if (!voicemail.getValue()) voicemail.setValue(true);
        }
        if (afkTimer.getTimePassed() < 15000 && autoDetect.getValue()) {
            if (voicemail.getValue()) voicemail.setValue(false);
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketChat) {
            String text = ((SPacketChat) event.getPacket()).getChatComponent().getUnformattedText();
            if (text.contains("whispers") && voicemail.getValue()) {
                String playerName = text.split(" ")[0];
                voicemailMessages.add(text);
                ClientMessage.sendMessage("New message added to voicemail.");
                mc.player.connection.sendPacket(new CPacketChatMessage("/msg " + playerName + " " + mc.player.getName() + " is afk, your message has been added to the FencingF+2 voicemail!"));
            }
        }
    }

    @SubscribeEvent
    public void onOption(OptionChangeEvent event) {
        if (event.getOption().equals(voicemail) && !voicemail.getValue()) {
            if (voicemailMessages.isEmpty()) {
                ClientMessage.sendMessage("No voicemail messages found.");
            } else {
                ClientMessage.sendMessage("Displaying all voicemail messages: ");
                for (String message : voicemailMessages) {
                    ClientMessage.sendMessage(message);
                }
                voicemailMessages.clear();
            }
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        afkTimer.reset();
    }
}
