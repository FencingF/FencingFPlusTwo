package org.fenci.fencingfplus2.features.hud.elements;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.features.hud.HUDElement;
import org.fenci.fencingfplus2.features.module.modules.client.CustomFont;
import org.fenci.fencingfplus2.features.module.modules.client.HUD;
import org.fenci.fencingfplus2.util.render.ColorUtil;

public class Armor extends HUDElement {
    public Armor() {
        super("Armor", 2f, 2f); //TODO FIX DEFAULTS
    }

    private static final RenderItem itemRender = mc.getRenderItem();

    @Override
    public int getWidth() {
        return 16 * 6;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public void onHudRender() {
//        GlStateManager.pushMatrix();
        ScaledResolution resolution = new ScaledResolution(mc);

        GlStateManager.enableTexture2D();
        int i = resolution.getScaledWidth() / 2;
        int iteration = 0;
        int y = resolution.getScaledHeight() + (int) getPosY() - resolution.getScaledHeight();
        for (ItemStack is : mc.player.inventory.armorInventory) {
            ++iteration;
            if (is.isEmpty()) continue;
            int x = i - 90 + (9 - iteration) * 20 + 2 + (int) getPosX() - i;
            GlStateManager.enableDepth();
            itemRender.zLevel = 200.0f;
            itemRender.renderItemAndEffectIntoGUI(is, x, y);
            itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
            itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            String s = is.getCount() > 1 ? is.getCount() + "" : "";
            if (CustomFont.INSTANCE.isOn()) {
                FencingFPlus2.INSTANCE.fontManager.drawStringWithShadow(s, (float) (x + 19 - 2 - FencingFPlus2.INSTANCE.fontManager.getStringWidth(s)), (float) (y + 9), 0xFFFFFF);
            } else {
                mc.fontRenderer.drawStringWithShadow(s, (float) (x + 19 - 2 - mc.fontRenderer.getStringWidth(s)), (float) (y + 9), 0xFFFFFF);
            }
//            if (!percentage.getValue()) continue; TODO: Add settings so I can add the percentage back
//            float green = ((float) is.getMaxDamage() - (float) is.getItemDamage()) / (float) is.getMaxDamage();
//            float red = 1.0f - green;
//            int dmg = 100 - (int) (red * 100.0f);
//            FencingFPlus2.INSTANCE.fontManager.drawStringWithShadow(dmg + "", (float) (x + 8 - FencingFPlus2.INSTANCE.fontManager.getStringWidth(dmg + "") / 2), (float) (y - 11), ColorUtil.toHex((int) (red * 255.0f), (int) (green * 255.0f), 0));
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }
}
