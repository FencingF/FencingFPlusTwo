package org.fenci.fencingfplus2.gui.components.button;

import org.fenci.fencingfplus2.gui.components.Component;

public abstract class Button extends Component {
    public Button(String id, double x, double y, double width, double height) {
        super(id, x, y, width, height);
    }

    protected abstract void mouseClickedInFrame(int button);

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseInBounds(mouseX, mouseY)) {
            playClickSound(1.0f);
            mouseClickedInFrame(button);
        }
    }
}
