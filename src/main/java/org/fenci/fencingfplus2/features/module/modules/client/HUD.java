package org.fenci.fencingfplus2.features.module.modules.client;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.features.module.modules.movement.AutoJump;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.Globals;
import org.fenci.fencingfplus2.util.render.ColorUtil;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HUD extends Module {
    //private static final int TEXT_COLOR = new Color(245, 34, 34).getRGB();

    public static HUD INSTANCE;

    public static final Setting<Boolean> watermark = new Setting<>("Watermark", true);
    public static final Setting<Boolean> arrayList = new Setting<>("ArrayList", true);
    public static final Setting<Boolean> armor = new Setting<>("Armor", true);
    public static final Setting<Boolean> percentage = new Setting<>("ArmorPercentage", false);
    //public static final Setting<Boolean> totemCount = new Setting<>("TotemCount", true);
    public static final Setting<Boolean> coords = new Setting<>("Coords", true);
    public static final Setting<Boolean> copyCoords = new Setting<>("CopyCoords", false);
    public static final Setting<Boolean> direction = new Setting<>("Direction", true);
    public static final Setting<Boolean> jumpLength = new Setting<>("JumpLength", false);
    //public static final Setting<Boolean> fps = new Setting<>("FPS", true);
    //public static final Setting<Boolean> ping = new Setting<>("Ping", true);
//    public static final Setting<Boolean> entityList = new Setting<>("EntityList", false);
//    public static final Setting<Boolean> playersOnly = new Setting<>("PlayersOnly", true);
    public static final Setting<BracketColor> bracketColor = new Setting<>("BracketColor", BracketColor.Black);
    public static final Setting<NameColor> nameColor = new Setting<>("NameColor", NameColor.Red);
    private static final RenderItem itemRender = mc.getRenderItem();
    private String direction1;
    private String nesw;
    private int y;

    public HUD() {
        super("HUD", "Shows the client's HUD.", Category.Client);
        INSTANCE = this;
    }

    private static int TEXT_COLOR() {
        return new Color(ClickGUI.getred.getValue(), ClickGUI.getgreen.getValue(), ClickGUI.getblue.getValue()).getRGB();
    }

    private String getDirection() {
        if (mc.player.getAdjustedHorizontalFacing().getAxisDirection().toString().equals("Towards positive")) {
            direction1 = "+";
        } else if (mc.player.getAdjustedHorizontalFacing().getAxisDirection().toString().equals("Towards negative")) {
            direction1 = "-";
        }
        return direction1;
    }

    private String getAxis() {
        return mc.player.getHorizontalFacing().getAxis().getName().toUpperCase();
    }

    private String getNESW() {
        if (getAxis().equals("X") && getDirection().equals("+")) {
            nesw = "East";
        }
        if (getAxis().equals("X") && getDirection().equals("-")) {
            nesw = "West";
        }
        if (getAxis().equals("Z") && getDirection().equals("+")) {
            nesw = "South";
        }
        if (getAxis().equals("Z") && getDirection().equals("-")) {
            nesw = "North";
        }
        return nesw;
    }

    public String assembleDirection() {
        return getNESW() + " [" + getDirection() + getAxis() + "]";
    }

    public int getDirectionY() {
        if (mc.currentScreen instanceof GuiChat && coords.getValue()) {
            y = 33;
        }
        if (mc.currentScreen instanceof GuiChat && !coords.getValue()) {
            y = 24;
        }
        if (!(mc.currentScreen instanceof GuiChat) && coords.getValue()) {
            y = 19;
        }
        if (!(mc.currentScreen instanceof GuiChat) && !coords.getValue()) {
            y = 10;
        }
        return y;
    }
/*
    private int getPing() {
        return (mc.isSingleplayer() ? -1 : Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.player.getUniqueID()).getResponseTime());
    }
    private float height;
    private float getEntityListHeight() {
        if(!watermark.getValue() && !ping.getValue() && !fps.getValue()) {
            height = 2.0f;
        }
        if(watermark.getValue() && !ping.getValue() && !fps.getValue() || !watermark.getValue() && !ping.getValue() && fps.getValue() || !watermark.getValue() && ping.getValue() && !fps.getValue()) {
            height = 11.0f;
        }
        if(watermark.getValue() && fps.getValue() && !ping.getValue() || watermark.getValue() && !fps.getValue() && ping.getValue() || !watermark.getValue() && fps.getValue() && ping.getValue()) {
            height = 20.0f;
        }
        if(watermark.getValue() && fps.getValue() && ping.getValue()) {
            height = 29.0f;
        }
        return height;
    }
    private boolean isFPSOnTop() {
        return ("Ping: " + getPing()).length() < ("FPS: " + Minecraft.debugFPS).length();
    }
    float fpsHeight;
    float pingHeight;
    private float[] getPingandFPSHeight() {
        if(isFPSOnTop() && !watermark.getValue()) {
            fpsHeight = 2.0f;
        }
        if(isFPSOnTop() && watermark.getValue()) {
            fpsHeight = 11.0f;
        }
        if(!isFPSOnTop() && !watermark.getValue()) {
            pingHeight = 2.0f;
            fpsHeight = 11.0f;
        }
        if(!isFPSOnTop() && watermark.getValue()) {
            pingHeight = 11.0f;
            fpsHeight = 20.0f;
        }
        return new float[] {fpsHeight, pingHeight};
    }
 */

    @Override
    public void onRender2D() {
        GlStateManager.pushMatrix();

        ScaledResolution resolution = new ScaledResolution(Globals.mc);

        if (watermark.getValue()) {
            Globals.mc.fontRenderer.drawStringWithShadow(FencingFPlus2.NAME + " v" + FencingFPlus2.VERSION, 2.0f, 2.0f, TEXT_COLOR());
        }

        if (jumpLength.getValue() && AutoJump.INSTANCE.isOn()) {
            Globals.mc.fontRenderer.drawStringWithShadow(AutoJump.INSTANCE.getClickPos().toString(), 2.0f, 11.0f, TEXT_COLOR());
        }

        if (coords.getValue()) {

            long netherX = (long) (mc.player.posX / 8);
            long netherY = (long) (mc.player.posY);
            long netherZ = (long) (mc.player.posZ / 8);

            long currX = (long) (mc.player.posX);
            long currY = (long) (mc.player.posY);
            long currZ = (long) (mc.player.posZ);

            long netherX2 = (long) (Math.round((netherX) * 10.0) / 10.0);
            long netherY2 = (long) (Math.round((netherY) * 10.0) / 10.0);
            long netherZ2 = (long) (Math.round((netherZ) * 10.0) / 10.0);

            long currX2 = (long) (Math.round((currX) * 10.0) / 10.0);
            long currY2 = (long) (Math.round((currY) * 10.0) / 10.0);
            long currZ2 = (long) (Math.round((currZ) * 10.0) / 10.0);

            if (mc.player.dimension == 0) {
                mc.fontRenderer.drawStringWithShadow("XYZ " + "[" + currX2 + " " + currY2 + " " + currZ2 + "]" + " [" + netherX2 + " " + netherY2 + " " + netherZ2 + "]", 2.0f, mc.currentScreen instanceof GuiChat ? resolution.getScaledHeight() - 24 : resolution.getScaledHeight() - 10, 16777215);
            } else if (mc.player.dimension == -1) {
                mc.fontRenderer.drawStringWithShadow("XYZ " + "[" + currX2 + " " + currY2 + " " + currZ2 + "]" + " [" + currX2 * 8 + " " + currY2 + " " + currZ2 * 8 + "]", 2.0f, mc.currentScreen instanceof GuiChat ? resolution.getScaledHeight() - 24 : resolution.getScaledHeight() - 10, 0xd3443d);
            } else if (mc.player.dimension == 1) {
                mc.fontRenderer.drawStringWithShadow("XYZ " + "[" + currX2 + " " + currY2 + " " + currZ2 + "]", 2.0f, mc.currentScreen instanceof GuiChat ? resolution.getScaledHeight() - 24 : resolution.getScaledHeight() - 10, 0xd65df5);
            }
        }
        //fps and watermark broken
        if (direction.getValue()) {
            if (mc.player.dimension == 0) {
                mc.fontRenderer.drawStringWithShadow(assembleDirection(), 2.0f, resolution.getScaledHeight() - getDirectionY(), 16777215);
            } else if (mc.player.dimension == -1) {
                mc.fontRenderer.drawStringWithShadow(assembleDirection(), 2.0f, resolution.getScaledHeight() - getDirectionY(), 0xd3443d);
            } else if (mc.player.dimension == 1) {
                mc.fontRenderer.drawStringWithShadow(assembleDirection(), 2.0f, resolution.getScaledHeight() - getDirectionY(), 0xd65df5);
            }
        }
/*
        if(fps.getValue() && (mc.player != null || mc.world != null)) {
            Globals.mc.fontRenderer.drawStringWithShadow(("FPS: " + Minecraft.debugFPS), 2.0f, getPingandFPSHeight()[0], TEXT_COLOR());
        }
        if(ping.getValue() && (mc.player != null || mc.world != null)) {
            Globals.mc.fontRenderer.drawStringWithShadow(("Ping: " + getPing()), 2.0f, getPingandFPSHeight()[1], TEXT_COLOR());
        }
        if(entityList.getValue()) {
            if(playersOnly.getValue()) {
                if(!mc.world.playerEntities.isEmpty()) {
                    mc.world.playerEntities.sort(Comparator.comparingInt((entityPlayer) -> -Globals.mc.fontRenderer.getStringWidth(entityPlayer.getName())));
                    float yy = 2.0f;
                    for(EntityPlayer player : mc.world.playerEntities) {
                        String display = player.getName();
                        Globals.mc.fontRenderer.drawStringWithShadow(display, getEntityListHeight(), yy, TEXT_COLOR());
                        yy += Globals.mc.fontRenderer.FONT_HEIGHT + 1.5f;
                    }
                }
            }
            if(!playersOnly.getValue()) {
                if(!mc.world.loadedEntityList.isEmpty()) {
                    mc.world.loadedEntityList.sort(Comparator.comparingInt((entity) -> -Globals.mc.fontRenderer.getStringWidth(entity.getName())));
                    float yy = 2.0f;
                    for(Entity entity : mc.world.loadedEntityList) {
                        String display = entity.getName();
                        Globals.mc.fontRenderer.drawStringWithShadow(display, getEntityListHeight(), yy, TEXT_COLOR());
                        yy += Globals.mc.fontRenderer.FONT_HEIGHT + 1.5f;
                    }
                }
            }
        }
 */

//        if (totemCount.getValue()) {
//            GlStateManager.enableTexture2D();
//            int i = resolution.getScaledWidth() / 2;
//            int y = resolution.getScaledHeight() - 55;
//            ItemStack totemStack = new ItemStack(Items.TOTEM_OF_UNDYING);
//            int x = i - 90 * 20 + 2;
//            GlStateManager.enableDepth();
//            HUD.itemRender.zLevel = 200.0f;
//            itemRender.renderItemAndEffectIntoGUI(totemStack, x, y);
//            itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, totemStack, x, y, "");
//            HUD.itemRender.zLevel = 0.0f;
//            GlStateManager.enableTexture2D();
//            GlStateManager.disableLighting();
//            GlStateManager.disableDepth();
//            GlStateManager.enableDepth();
//            GlStateManager.disableLighting();
//        }

        if (armor.getValue()) {
            GlStateManager.enableTexture2D();
            int i = resolution.getScaledWidth() / 2;
            int iteration = 0;
            int y = resolution.getScaledHeight() - 55;
            for (ItemStack is : mc.player.inventory.armorInventory) {
                ++iteration;
                if (is.isEmpty()) continue;
                int x = i - 90 + (9 - iteration) * 20 + 2;
                GlStateManager.enableDepth();
                HUD.itemRender.zLevel = 200.0f;
                itemRender.renderItemAndEffectIntoGUI(is, x, y);
                itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
                HUD.itemRender.zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                String s = is.getCount() > 1 ? is.getCount() + "" : "";
                mc.fontRenderer.drawStringWithShadow(s, (float) (x + 19 - 2 - mc.fontRenderer.getStringWidth(s)), (float) (y + 9), 0xFFFFFF);
                if (!percentage.getValue()) continue;
                float green = ((float) is.getMaxDamage() - (float) is.getItemDamage()) / (float) is.getMaxDamage();
                float red = 1.0f - green;
                int dmg = 100 - (int) (red * 100.0f);
                mc.fontRenderer.drawStringWithShadow(dmg + "", (float) (x + 8 - mc.fontRenderer.getStringWidth(dmg + "") / 2), (float) (y - 11), ColorUtil.toHex((int) (red * 255.0f), (int) (green * 255.0f), 0));
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }

        if (arrayList.getValue()) {
            List<Module> modules = getFencing().moduleManager.getModules().stream()
                    .filter((module) -> module.isToggled() && module.isDrawn())
                    .collect(Collectors.toList());

            if (!modules.isEmpty()) {
                modules.sort(Comparator.comparingInt((mod) -> -Globals.mc.fontRenderer.getStringWidth(mod.getFullDisplay())));

                float y = 2.0f;
                for (Module module : modules) {
                    String display = module.getFullDisplay();
                    Globals.mc.fontRenderer.drawStringWithShadow(display, resolution.getScaledWidth() - Globals.mc.fontRenderer.getStringWidth(display) - 2.0f, y, TEXT_COLOR());
                    y += Globals.mc.fontRenderer.FONT_HEIGHT + 1.5f;
                }
            }
        }
        GlStateManager.popMatrix();
    }

    public enum BracketColor {
        DarkRed, Red, Gold, Yellow, DarkGreen, Green, Aqua, DarkAqua, DarkBlue, Blue, LightPurple, DarkPurple, White, Gray, DarkGray, Black
    }

    public enum NameColor {
        DarkRed, Red, Gold, Yellow, DarkGreen, Green, Aqua, DarkAqua, DarkBlue, Blue, LightPurple, DarkPurple, White, Gray, DarkGray, Black

    }
}
