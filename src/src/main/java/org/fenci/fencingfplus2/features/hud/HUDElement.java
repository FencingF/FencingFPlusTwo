package org.fenci.fencingfplus2.features.hud;

import net.minecraftforge.common.MinecraftForge;
import org.fenci.fencingfplus2.gui.components.other.HUDOption;
import org.fenci.fencingfplus2.setting.Configurable;
import org.fenci.fencingfplus2.util.Globals;

public abstract class HUDElement extends Configurable implements Globals {
    private final String name;
    private float posX;
    private float posY;

    private boolean enabled = false;

    public HUDElement(String name, double posX, double posY) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setToggled(boolean enabled) {
        this.enabled = enabled;
    }

    public void onEnable() {
        this.onHudRender(); //this is to avoid having to turn the module on and back off to get the element to render
    }

    public void onUpdate() {}

    public void onDisable() {}

    public abstract void onHudRender();

    public void onRender3D() {}


    public int getWidth() {
        return 30;
    }

    public int getHeight() {
        return 30;
    }

    public void toggle() {
        this.enabled = !this.enabled;
        if (enabled) {
            MinecraftForge.EVENT_BUS.register(this);
            onEnable();
            HUDOption.init();
        } else {
            MinecraftForge.EVENT_BUS.unregister(this);
            onDisable();
            HUDOption.init();
        }
    }
}