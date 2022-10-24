package org.fenci.fencingfplus2.features.module.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.events.client.OptionChangeEvent;
import org.fenci.fencingfplus2.events.network.PacketEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.manager.friend.Friend;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.ClientMessage;
import org.fenci.fencingfplus2.util.client.Timer;
import org.fenci.fencingfplus2.util.player.PlayerUtil;
import org.fenci.fencingfplus2.util.render.ColorUtil;
import org.fenci.fencingfplus2.util.render.PopChamsUtil;

import java.util.*;
import java.util.stream.Collectors;

//idea by flixzit created by FencingF
public class DataLink extends Module {

    public static DataLink INSTANCE;

    public DataLink() {
        super("DataLink", "Allows you to communicate with other connected clients", Category.Client);
        INSTANCE = this;
    }

    public static final Setting<Integer> updateTime = new Setting<>("UpdateTime (ms)", 1500, 500, 10000); //I made the min 500 because otherwise there is a risk of crashing, I would not recommend lowering it further than this
    public static final Setting<Integer> removeTime = new Setting<>("RemoveTime (ms)", 15000, 5000, 20000);
    public static final Setting<Integer> players = new Setting<>("Players", 2, 1, 7);
    public static final Setting<Boolean> debug = new Setting<>("Debug", false);
    public static final Setting<Boolean> inChat = new Setting<>("InChat", false);
    public static final Setting<Boolean> antiNaked = new Setting<>("AntiNaked", false);
    public static final Setting<Boolean> noLeak = new Setting<>("NoBaseLeak", true);
    public static final Setting<Boolean> help = new Setting<>("Help", false);

    //global vars
    public static float yStart = 30f;
    public static float xStart = 2f;

    public static final String name = "Name: ";
    public static final String health = " Health: ";
    public static final String dimension = " Dimension: ";
    public static final String x = " X: ";
    public static final String y = " Y: ";
    public static final String z = " Z: ";

    public int loop = 0;

    @Deprecated
    public String targetName;
    @Deprecated
    public int targetDimension;
    @Deprecated
    public double targetX;
    @Deprecated
    public double targetY;
    @Deprecated
    public double targetZ;
    @Deprecated
    public String targetUUID;

    Timer timer = new Timer();
    Timer chatTimer = new Timer();

    public Set<DataLinkData> dataToDisplay = new HashSet<>();

    @SubscribeEvent
    public void onOptionChangeEvent(OptionChangeEvent event) {
        if (event.getOption().equals(players)) {
            loop = 0;
        }
        if (event.getOption().equals(help) && help.getValue()) {
            ClientMessage.sendMessage("In order to use DataLink, add any player as a friend you would like to communicate with. If there are unfriended players in your area they will be displayed on your friend(s) DataLink GUI(s). The data they receive will be updated every " + updateTime.getValue() + " millisecond unless you are no longer in render distance with anyone, in which case the data will no longer be sent and displayed.");
            help.setValue(false);
        }
    }

    @Override
    public void onUpdate() {
        //sending data
        for (Friend friend : getFencing().friendManager.getFriends()) { //TODO Add delay in between each friend message
            if (!timer.hasReached(updateTime.getValue())) return; //This might not work I have no idea
            if (noLeak.getValue() && mc.player.getDistance(0, 0, 0) > 3000) return;
            if (mc.player.connection.getPlayerInfo(friend.getAlias()) == null) continue;
            //mc.player.sendChatMessage("/msg " + friend.getAlias() + " " + sendEncryptedData());
            if (sendEncryptedData().toString().equals("")) continue;
            if (mc.world.playerEntities.size() > 1) mc.player.connection.sendPacket(new CPacketChatMessage("/msg " + friend.getAlias() + " " + sendEncryptedData()));
            if (debug.getValue()) ClientMessage.sendMessage("/msg " + friend.getAlias() + " " + sendEncryptedData());
            timer.reset();
        }

        //debugging
        if (chatTimer.hasReached(updateTime.getValue())) {
            if (dataToDisplay.size() > 0) {
                for (DataLinkData dataLinkData : dataToDisplay) {
                    if (inChat.getValue()) ClientMessage.sendMessage(dataLinkData.getName() + " is at " + dataLinkData.posX + " " + dataLinkData.posY + " " + dataLinkData.posZ);
                }
            }
        }
        chatTimer.reset();
    }

    public DataLinkData getDataLinkDataFromString(String string) {
        DataLinkData dataLinkData1 = null;
        for (DataLinkData dataLinkData : dataToDisplay) {
            if (dataLinkData.getName().equals(string))
                dataLinkData1 = dataLinkData;
        }
        return dataLinkData1;
    }

    //getting data
    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        try {
            if (event.getPacket() instanceof SPacketChat) {
                String text = ((SPacketChat) event.getPacket()).getChatComponent().getUnformattedText();
                if (text.contains("#")) {
                    String finalText = fixText(text);
                    if (finalText.equals("")) return;
                    if (finalText.startsWith("#")) {
                        event.setCanceled(true);
                        for (DataLinkData dataLinkData : decryptData(finalText)) {
                            DataLinkData oldDataLinkData = getDataLinkDataFromString(dataLinkData.getName());
                            if (oldDataLinkData != null) {
                                if (oldDataLinkData.getName().equals(dataLinkData.getName())) {
                                    oldDataLinkData.getTimer().reset();
                                    oldDataLinkData.setDimension(dataLinkData.getDimension());
                                    oldDataLinkData.setPosX(dataLinkData.getPosX());
                                    oldDataLinkData.setPosY(dataLinkData.getPosY());
                                    oldDataLinkData.setPosZ(dataLinkData.getPosZ());
                                    oldDataLinkData.setHealth(dataLinkData.getHealth());
                                }
                            } else {
                                dataToDisplay.add(dataLinkData);
                            }
                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (fullNullCheck()) {
            dataToDisplay.removeIf(dataLinkData -> dataLinkData.getTimer().hasReached(removeTime.getValue())); //double check this cause I changed it from the for loop
        }
    }

    //displaying data on screen
    @Override
    public void onRender2D() {
        try {
            if (dataToDisplay.size() > 0) {
                for (DataLinkData dataLinkData : dataToDisplay.stream().sorted(Comparator.comparingDouble(dataLinkData -> dataLinkData.getDistance(mc.player))).collect(Collectors.toCollection(LinkedHashSet::new))) {
                    if (dataLinkData.getName().equals(mc.player.getName())) continue;
                    if (PlayerUtil.isPlayerInRender(dataLinkData.getName())) continue;
                    if (CustomFont.INSTANCE.isOn()) {
                        FencingFPlus2.INSTANCE.fontManager.drawStringWithShadow(ChatFormatting.WHITE + name + ChatFormatting.GREEN + dataLinkData.getName() + ChatFormatting.WHITE + x + ChatFormatting.GREEN + dataLinkData.getPosX() + ChatFormatting.WHITE + y + ChatFormatting.GREEN + dataLinkData.getPosY() + ChatFormatting.WHITE + z + ChatFormatting.GREEN + dataLinkData.getPosZ() + ChatFormatting.WHITE + health + ChatFormatting.RESET + dataLinkData.getHealth() + ChatFormatting.WHITE + dimension + ColorUtil.getDimentionColor(dataLinkData.getDimension()) + PlayerUtil.getDimention(dataLinkData.getDimension()), xStart, yStart, ColorUtil.getHealthColor(dataLinkData.getHealth()));
                    } else {
                        mc.fontRenderer.drawStringWithShadow(ChatFormatting.WHITE + name + ChatFormatting.GREEN + dataLinkData.getName() + ChatFormatting.WHITE + x + ChatFormatting.GREEN + dataLinkData.getPosX() + ChatFormatting.WHITE + y + ChatFormatting.GREEN + dataLinkData.getPosY() + ChatFormatting.WHITE + z + ChatFormatting.GREEN + dataLinkData.getPosZ() + ChatFormatting.WHITE + health + ChatFormatting.RESET + dataLinkData.getHealth() + ChatFormatting.WHITE + dimension + ColorUtil.getDimentionColor(dataLinkData.getDimension()) + PlayerUtil.getDimention(dataLinkData.getDimension()), xStart, yStart, ColorUtil.getHealthColor(dataLinkData.getHealth()));
                    }
                    yStart += 10;
                }
                yStart = 20f;
            }
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }

    public static String fixText(String text) {
        if (!text.contains(": ")) return "";
        return text.split(": ")[1];
    }

    public StringBuilder sendEncryptedData() {
        StringBuilder data = new StringBuilder();
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player.getName().equals(mc.player.getName())) continue;
            if (PlayerUtil.isWearingArmor(player) && antiNaked.getValue()) continue;
            if (player.isDead) continue;
            if (loop >= players.getValue()) break;
            data.append("#").append(player.getName()).append("&").append(player.getPosition().x).append("&").append(player.getPosition().y).append("&").append(player.getPosition().z).append("&").append((int) PlayerUtil.getPlayerHealth(player)).append("&").append(player.dimension);
            loop++;
        }
        loop = 0;
        return data;
    }

//    public int calcDataLength(EntityPlayer player, StringBuilder builder) {
//        builder.append("/msg 1234567891234567 ").append("#").append(player.getName()).append("&").append(player.getPosition().x).append("&").append(player.getPosition().y).append("&").append(player.getPosition().z).append("&").append(PlayerUtil.getPlayerHealth(player)).append("&").append(player.dimension);
//        return builder.length();
//    }

    //decrypting data
    public static List<DataLinkData> decryptData(String data) {
        List<DataLinkData> playerInfo = new ArrayList<>();
        String[] players = data.split("\\#");
        for (String player : players) {
            if (player.isEmpty()) continue;
            String[] playerData = player.split("&");
            playerInfo.add(new DataLinkData(playerData[0], Integer.parseInt(playerData[1]), Integer.parseInt(playerData[2]), Integer.parseInt(playerData[3]), Float.parseFloat(playerData[4]), Integer.parseInt(playerData[5]), new Timer()));
        }
        return playerInfo;
    }

    //constructing data
    public static class DataLinkData {
        private final String name;
        private int posX;
        private int posY;
        private int posZ;
        private float health;
        private int dimension;
        private Timer timer;

        public DataLinkData(String name, int posX, int posY, int posZ, float health, int dimension, Timer timer) {
            this.name = name;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.health = health;
            this.dimension = dimension;
            this.timer = timer;
        }

        public String getName() {
            return name;
        }

        public int getPosX() {
            return posX;
        }

        public int getPosY() {
            return posY;
        }

        public int getPosZ() {
            return posZ;
        }

        public float getHealth() {
            return health;
        }

        public int getDimension() {
            return dimension;
        }

        public double getDistance(EntityPlayer player) {
            return player.getDistance(posX, posY, posZ);
        }

        public Timer getTimer() {
            return timer;
        }

        public void setPosX(int posX) {
            this.posX = posX;
        }

        public void setPosY(int posY) {
            this.posY = posY;
        }

        public void setPosZ(int posZ) {
            this.posZ = posZ;
        }

        public void setDimension(int dimension) {
            this.dimension = dimension;
        }

        public void setHealth(float health) {
            this.health = health;
        }

        public void setTimer(Timer timer) {
            this.timer = timer;
        }
    }
}


// "too much fucking coding for a simple fucking mod" -GitHub co-pilot