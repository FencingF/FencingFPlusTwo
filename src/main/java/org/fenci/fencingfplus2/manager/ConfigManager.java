package org.fenci.fencingfplus2.manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.EnumConverter;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class ConfigManager {
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
        //loadFriends();
        loadPrefix();
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

    /*
    public void saveFriends() {
        try {
            File friendFile = new File(Covenant_Client.getAbsolutePath(), "Other/" + "Friends/" + "friends.json");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(Covenant.INSTANCE.friendManager.getFriends());
            OutputStreamWriter file;
            file = new OutputStreamWriter(new FileOutputStream(friendFile), StandardCharsets.UTF_8);
            file.write(json);
            file.close();
        } catch (Exception e) {
        }
    }
    public void loadFriends() {
        File friendFile = new File(Covenant_Client.getAbsolutePath(), "Other/" + "Friends/" + "friends.json");
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(String.valueOf(friendFile)));
            Covenant.INSTANCE.friendManager.setFriends(gson.fromJson(reader, new TypeToken<ArrayList<Friend>>(){}.getType()));
            reader.close();
        } catch (Exception e) {
        }
    }
     */

    public void save() {
        try {
            for (Module module : FencingFPlus2.INSTANCE.moduleManager.getModules()) {
                File moduleFile = new File(FencingFPlusTwo.getAbsolutePath(), "Settings/" + module.getCategory() + "/" + module.getName() + ".json");
                moduleFile.getParentFile().mkdirs();
                if (!moduleFile.exists())
                    moduleFile.createNewFile();
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
        } catch (Exception e) {
        }
    }

    public void load() {
        for (Module module : FencingFPlus2.INSTANCE.moduleManager.getModules()) {
            try {
                File moduleFile = new File(FencingFPlusTwo.getAbsolutePath(), "Settings/" + module.getCategory() + "/" + module.getName() + ".json");
                moduleFile.getParentFile().mkdirs();
                if (!moduleFile.exists())
                    moduleFile.createNewFile();
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
    }
}
