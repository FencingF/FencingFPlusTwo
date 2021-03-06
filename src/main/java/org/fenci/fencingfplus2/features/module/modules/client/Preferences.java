package org.fenci.fencingfplus2.features.module.modules.client;

import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class Preferences extends Module {

    public static Preferences INSTANCE;

    public Preferences() {
        super("Preferences", "Allows you to further modify the client", Category.Client);
        INSTANCE = this;
    }

    public static final Setting<Boolean> msgFriendsOnAdd = new Setting<>("MsgFriendsOnAdd", true);
    public static final Setting<Boolean> msgFriendsonRemove = new Setting<>("MsgFriendsOnRemove", false);
    public static final Setting<Boolean> defaultsResetDrawn = new Setting<>("DefaultsResetDrawn", false);
    public static final Setting<Boolean> defaultsResetKey = new Setting<>("DefaultsResetKey", false);
    public static final Setting<Boolean> logoutSpotsRemoveOnDisable = new Setting<>("LogoutSpotsRemoveOnDisable", true);
    public static final Setting<TokenMethod> tokenMethod = new Setting<>("TokenMethod", TokenMethod.Copy);

    @Override
    public void onEnable() {
        this.toggle(true);
    }

    public enum TokenMethod {
        Copy, Chat
    }
}
