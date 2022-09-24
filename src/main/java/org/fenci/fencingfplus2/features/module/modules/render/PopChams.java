package org.fenci.fencingfplus2.features.module.modules.render;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.events.player.TotemPopEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.client.Timer;
import org.fenci.fencingfplus2.util.render.PopChamsUtil;

public class PopChams extends Module {

    public static final Setting<Integer> fillred = new Setting<>("FillRed", 57, 0, 255);
    public static final Setting<Integer> fillgreen = new Setting<>("FillGreen", 236, 0, 255);
    public static final Setting<Integer> fillblue = new Setting<>("FillBlue", 255, 0, 255);
    public static final Setting<Integer> fillalpha = new Setting<>("FillAlpha", 50, 0, 255);
    public static final Setting<Integer> lineRed = new Setting<>("LineRed", 57, 0, 255);
    public static final Setting<Integer> lineGreen = new Setting<>("LineGreen", 236, 0, 255);
    public static final Setting<Integer> lineBlue = new Setting<>("LineBlue", 255, 0, 255);
    public static final Setting<Integer> lineAlpha = new Setting<>("LineAlpha", 50, 0, 255);
    public static final Setting<Integer> fadestart = new Setting<>("FadeStart", 1238, 0, 3000);
    public static final Setting<Double> fadeTime = new Setting<>("FadeTime", 0.4, 0.0, 2.0);
    public static final Setting<Boolean> smallArms = new Setting<>("SmallArms", false);
    public static final Setting<Boolean> follow = new Setting<>("Follow", false);
    public static final Setting<Double> followTime = new Setting<>("FollowTime", 0.4, 0.1, 2);
    public static final Setting<Boolean> self = new Setting<>("Self", false);
    public static PopChams INSTANCE;
    EntityOtherPlayerMP player;
    ModelPlayer playerModel;
    long startTime;
    double alphaFill;
    double alphaLine;
    Timer followTimer = new Timer();

    public PopChams() {
        super("PopChams", "Cool pop render", Category.Render);
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onPop(TotemPopEvent event) {
        followTimer.reset();
        if (self.getValue() || event.getPlayer().getEntityId() != PopChams.mc.player.getEntityId()) {
            if (!follow.getValue()) {
                GameProfile profile = new GameProfile(PopChams.mc.player.getUniqueID(), "");
                this.player = new EntityOtherPlayerMP(PopChams.mc.world, profile);
                this.player.copyLocationAndAnglesFrom(event.getPlayer());
                this.playerModel = new ModelPlayer(0.0f, smallArms.getValue());
                this.startTime = System.currentTimeMillis();
                this.playerModel.bipedHead.showModel = false;
                this.playerModel.bipedBody.showModel = false;
                this.playerModel.bipedLeftArmwear.showModel = false;
                this.playerModel.bipedLeftLegwear.showModel = false;
                this.playerModel.bipedRightArmwear.showModel = false;
                this.playerModel.bipedRightLegwear.showModel = false;
                this.alphaFill = fillalpha.getValue();
                this.alphaLine = lineAlpha.getValue();
                PopChamsUtil popChamsUtil = new PopChamsUtil(this.player, this.playerModel, this.startTime, this.alphaFill, this.alphaLine);
            }
            if (follow.getValue() && followTimer.hasReached(followTime.getValue().longValue() * 1000)) {
                //TODO
            }
        }
    }
}
