package org.fenci.fencingfplus2.manager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.fenci.fencingfplus2.features.hud.HUDElement;
import org.fenci.fencingfplus2.util.player.PlayerUtil;
import org.json.simple.parser.JSONParser;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.features.module.modules.misc.FakePlayer;
import org.fenci.fencingfplus2.manager.friend.Friend;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.Globals;
import org.fenci.fencingfplus2.util.client.EnumConverter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ConfigManager implements Globals {
    public File FencingFPlusTwo;
    public File Settings;
    public File Other;
    public File Friends;

    public ConfigManager() {
        this.FencingFPlusTwo = new File("FencingF+2");
        if (!this.FencingFPlusTwo.exists()) {
            this.FencingFPlusTwo.mkdirs();
        }

        this.Settings = new File("FencingF+2" + File.separator + "Settings");
        if (!this.Settings.exists()) {
            this.Settings.mkdirs();
        }
        this.Other = new File("FencingF+2" + File.separator + "Other");
        if (Other.exists()) {
            this.Other.mkdirs();
        }
        this.Friends = new File("FencingF+2" + File.separator + "Other" + File.separator + "Friends");
        if (Friends.exists()) {
            this.Friends.mkdirs();
        }
        load();
        //loadFriendsFromJSON();
        loadFriends();
        loadPrefix();
    }

    public void prepare() {
        Runtime.getRuntime().addShutdownHook(new SaveThread());
        if (mc.hasCrashed) {
            save();
            savePrefix();
            saveFriends();
            FakePlayer.INSTANCE.setToggled(false);
        }
    }

    public void savePrefix() {
        try {
            File friendFile = new File(FencingFPlusTwo.getAbsolutePath(), "Other/" + "Prefix" + ".json");
            friendFile.mkdirs();
            if (!friendFile.exists()) {
                friendFile.createNewFile();
            }
            JsonObject object = new JsonObject();
            object.addProperty("prefix", CommandManager.prefix);
            FileWriter fileWriter = new FileWriter(friendFile);
            fileWriter.write(object.toString());
            fileWriter.flush();
            fileWriter.close();

        } catch (Exception e) {
        }
    }

    public void loadPrefix() {
        try {
            File prefixFile = new File(FencingFPlusTwo.getAbsolutePath(), "Other/" + "Prefix" + ".json");
            prefixFile.getParentFile().mkdirs();
            if (!prefixFile.exists()) prefixFile.createNewFile();
            String content = Files.readAllLines(prefixFile.toPath()).stream().collect(Collectors.joining());
            JsonObject object = new JsonParser().parse(content).getAsJsonObject();
            String prefix = object.get("prefix").getAsString();
            CommandManager.setPrefix(prefix);

        } catch (Exception e) {
        }

    }

    public void saveKits() {
        try {

        } catch (Exception ignored) {}
    }

    public void saveFriends() {
        JSONObject jo = new JSONObject();

        String friends = buildFriendString().toString();

        jo.put("FriendNames", friends);
           /* if (preset.presetDir != null) {
                jo.put("Presets", preset.presetDir);
            }*/
        try {
            FileWriter file = new FileWriter("FencingF+2/Other/SavedFriends.json");
            file.write(jo.toJSONString());
            file.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public StringBuilder buildFriendString() {
        StringBuilder sb = new StringBuilder();
        for (Friend friend : getFencing().friendManager.getFriends()) {
            if (friend.getAlias().equals("") || friend.getAlias().equals(" ")) continue;
            sb.append(friend.getAlias()).append("&");
        }
        return sb;
    }

    public List<String> parseFriendString(String data) {
        return Arrays.asList(data.split("&"));
    }

    public void loadFriends() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("FencingF+2/Other/SavedFriends.json"));
            String line;
            StringBuilder sbuilderObj = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sbuilderObj.append(line);
            }

            JsonObject gsonObj = new Gson().fromJson(sbuilderObj.toString(), JsonObject.class);

            String friendNames = gsonObj.get("FriendNames").getAsString();
            FencingFPlus2.LOGGER.info("Player String " + friendNames);

            for (String friend : parseFriendString(friendNames)) {
                UUID uuid = PlayerUtil.getUUIDFromName(friend);
                FencingFPlus2.LOGGER.info("Player UUID, and Name " + uuid + " " + friend);
                Friend friendToAdd = new Friend(uuid, friend);
                FencingFPlus2.INSTANCE.friendManager.addNoEvent(friendToAdd);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        try {
            for (Module module : FencingFPlus2.INSTANCE.moduleManager.getModules()) {
                File moduleFile = new File(FencingFPlusTwo.getAbsolutePath(), "Settings/" + module.getCategory() + "/" + module.getName() + ".json");
                moduleFile.getParentFile().mkdirs();
                if (!moduleFile.exists()) moduleFile.createNewFile();
                JsonObject object = new JsonObject();
                object.addProperty("bind", Keyboard.getKeyName(module.getKeybind()));
                object.addProperty("enabled", module.isOn());
                for (Setting value : module.getSettings()) {
                    if (value.isBoolean()) object.addProperty(value.getName(), (Boolean) value.getValue());
                    if (value.isNumber()) object.addProperty(value.getName(), (Number) value.getValue());
                    if (value.isEnum()) object.addProperty(value.getName(), ((Enum) value.getValue()).name());
                }
                FileWriter fileWriter = new FileWriter(moduleFile);
                fileWriter.write(object.toString());
                fileWriter.flush();
                fileWriter.close();
            }
            for (HUDElement element : FencingFPlus2.INSTANCE.hudElementManager.getElements()) {
                File elementFile = new File(FencingFPlusTwo.getAbsolutePath(), "Settings/HUD/" + element.getName() + ".json");
                elementFile.getParentFile().mkdirs();
                if (!elementFile.exists()) elementFile.createNewFile();
                JsonObject object = new JsonObject();
                object.addProperty("x", element.getPosX());
                object.addProperty("y", element.getPosY());
                object.addProperty("enabled", element.isEnabled());
                FileWriter fileWriter = new FileWriter(elementFile);
                fileWriter.write(object.toString());
                fileWriter.flush();
                fileWriter.close();
            }
        } catch (Exception e) {
        }
    }

    public void load() {
        for (Module module : FencingFPlus2.INSTANCE.moduleManager.getModules()) {
            try {
                File moduleFile = new File(FencingFPlusTwo.getAbsolutePath(), "Settings/" + module.getCategory() + "/" + module.getName() + ".json");
                moduleFile.getParentFile().mkdirs();
                if (!moduleFile.exists()) moduleFile.createNewFile();
                String content = Files.readAllLines(moduleFile.toPath()).stream().collect(Collectors.joining());
                JsonObject object = new JsonParser().parse(content).getAsJsonObject();
                int bind = Keyboard.getKeyIndex(object.get("bind").getAsString());
                module.setKeybind(bind);
                module.setToggled(object.get("enabled").getAsBoolean());
                for (Setting value : module.getSettings()) {
                    JsonElement element = object.get(value.getName());
                    if (value.isBoolean()) value.setValue(element.getAsBoolean());
                    if (value.isNumber()) {
                        if (value.getValue() instanceof Integer)
                            value.setValue(element.getAsNumber().intValue());
                        if (value.getValue() instanceof Double)
                            value.setValue(element.getAsNumber().doubleValue());
                        if (value.getValue() instanceof Float)
                            value.setValue(element.getAsNumber().floatValue());
                        if (value.getValue() instanceof Long)
                            value.setValue(element.getAsNumber().longValue());
                        if (value.getValue() instanceof Byte)
                            value.setValue(element.getAsNumber().byteValue());
                        if (value.getValue() instanceof Short)
                            value.setValue(element.getAsNumber().shortValue());
                    }
                    if (value.isEnum()) {
                        EnumConverter converter = new EnumConverter(((Enum) value.getValue()).getClass());
                        value.setValue(converter.doBackward(element));
                    }
                }
            } catch (Exception e) {
            }
        }
        for (HUDElement element : FencingFPlus2.INSTANCE.hudElementManager.getElements()) {
            try {
                File elementFile = new File(FencingFPlusTwo.getAbsolutePath(), "Settings/HUD/" + element.getName() + ".json");
                elementFile.getParentFile().mkdirs();
                if (!elementFile.exists()) elementFile.createNewFile();
                String content = Files.readAllLines(elementFile.toPath()).stream().collect(Collectors.joining());
                JsonObject object = new JsonParser().parse(content).getAsJsonObject();
                element.setPosX(object.get("x").getAsInt());
                element.setPosY(object.get("y").getAsInt());
                element.setToggled(object.get("enabled").getAsBoolean());
            } catch (Exception e) {
            }
        }
    }

    public boolean hasRan() throws IOException {
        String FILE_NAME = "FencingF+2/Other/HasRan.txt";
        if (new File(FILE_NAME).isFile()) {
            return true;
        } else {
            Path newFilePath = Paths.get(FILE_NAME);
            Files.createFile(newFilePath);
            return false;
        }
    }

    public static class SaveThread extends Thread {
        @Override
        public void run() {
            FencingFPlus2.INSTANCE.configManager.save();
            FencingFPlus2.INSTANCE.configManager.savePrefix();
            FencingFPlus2.INSTANCE.configManager.saveFriends();
            //FencingFPlus2.INSTANCE.configManager.saveFriendsToJSON();
            FakePlayer.INSTANCE.setToggled(false);
        }
    }
}
