package org.fenci.fencingfplus2.manager;

import com.google.common.collect.Lists;
import org.fenci.fencingfplus2.features.hud.HUDElement;
import org.fenci.fencingfplus2.features.hud.elements.*;
import org.fenci.fencingfplus2.util.Globals;

import java.util.ArrayList;

public class HUDElementManager implements Globals {

    private final ArrayList<HUDElement> elements;

    {
        elements = Lists.newArrayList(
            new Armor(),
            new org.fenci.fencingfplus2.features.hud.elements.ArrayList(),
            new Coordinates(),
            new CopyCoords(),
            new Direction(),
            new FPS(),
            new Watermark()
        );
    }

    public HUDElementManager() {
        elements.forEach(HUDElement::register);
    }

    public ArrayList<HUDElement> getElements() {
        return elements;
    }
}
