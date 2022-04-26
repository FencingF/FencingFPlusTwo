package org.fenci.fencingfplus2.gui.click.components;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import org.fenci.fencingfplus2.util.Globals;

public class Component implements Globals {
    protected final String id;
    protected double x, y;
    protected double width, height;

    public Component(String id, double x, double y, double width, double height) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(int mouseX, int mouseY) {
    }

    public void drawComponent(int mouseX, int mouseY, float partialTicks) {

    }

    public void mouseClicked(int mouseX, int mouseY, int button) {

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    public void keyTyped(char character, int code) {

    }

    public void playClickSound(float pitch) {
        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, pitch));
    }

    public boolean isMouseInBounds(int mouseX, int mouseY) {
        return isMouseInBounds(mouseX, mouseY, x, y, width, height);
    }

    public boolean isMouseInBounds(int mouseX, int mouseY, double x, double y, double w, double h) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isVisible() {
        return true;
    }
}
