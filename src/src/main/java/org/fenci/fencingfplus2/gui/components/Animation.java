package org.fenci.fencingfplus2.gui.components;

import org.fenci.fencingfplus2.util.client.Timer;

public class Animation {
    private final Timer timer = new Timer();
    private float progress = 0.0f;
    private float progression = 1.0f;
    private float max = 100.0f;
    private long delay = 0L;

    public Animation(float progression, float max, long delay) {
        this.progression = progression;
        this.max = max;
        this.delay = delay;
    }

    public void progress(boolean reverse) {
        if (timer.hasReached(delay)) {
            timer.reset();

            if (reverse) {
                progress -= progression;
            } else {
                progress += progression;
            }
        }

        if (Math.abs(progress) > Math.abs(max)) {
            progression = reverse ? -max : max;
        }
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void setProgression(float progression) {
        this.progression = progression;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
}
