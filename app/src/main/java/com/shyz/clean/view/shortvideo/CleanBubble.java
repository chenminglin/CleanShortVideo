package com.shyz.clean.view.shortvideo;

import java.util.Random;

public class CleanBubble {
    public String id;

    public int centerX;
    public int centerY;
    public int radius;

    public int alpha;
    public int alphaSeed;

    public int riseDecrement;

    public int leaveDecrement;
    public int leaveSeed;

    public void nextFrame() {
        centerY = centerY + riseDecrement;
        centerX = centerX + leaveDecrement;

        riseDecrement--;

        if (leaveDecrement > 0) {
            leaveDecrement = leaveDecrement + leaveSeed;
        } else {
            leaveDecrement = leaveDecrement - leaveSeed;
        }

        alpha += alphaSeed;
    }

}
