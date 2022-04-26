package org.fenci.fencingfplus2.features.module.modules.render;

import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class ViewModel extends Module {

    public static ViewModel INSTANCE;

    public ViewModel() {
        super("ViewModel", "Allows you to change the way things look in your hand", Category.Render);
        INSTANCE = this;
    }

    public static final Setting<Float> sizex = new Setting<>("SizeX", 1f, 0, 2);
    public static final Setting<Float> sizey = new Setting<>("SizeY", 1f, 0, 2);
    public static final Setting<Float> sizez = new Setting<>("SizeZ", 1f, 0, 2);
    public static final Setting<Float> rotationx = new Setting<>("RotationX", 0f, 0, 1);
    public static final Setting<Float> rotationy = new Setting<>("RotationY", 0f, 0, 1);
    public static final Setting<Float> rotationz = new Setting<>("RotationZ", 0f, 0, 1);
    public static final Setting<Float> translationx = new Setting<>("TranslationX", 0f, -2, 2);
    public static final Setting<Float> translationy = new Setting<>("TranslationY", 0f, -2, 2);
    public static final Setting<Float> translationz = new Setting<>("TranslationZ", 0f, -2, 2);

    //TODO: Fix this
}
