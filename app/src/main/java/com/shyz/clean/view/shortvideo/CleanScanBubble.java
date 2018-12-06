package com.shyz.clean.view.shortvideo;

import android.util.Log;

public class CleanScanBubble {
    final String TAG = getClass().getSimpleName();

    public String id;

    public int centerX;
    public int centerY;
    public int endX;
    public int alpha;

    public int radius;

    //下掉速度
    public int fallDecrement;
    //靠拢速度
    public int closeUpDecrement;

    //下一帧
    public void nextFrame() {
        centerY = centerY + fallDecrement;
        if (centerX > endX) {
            if (centerX - closeUpDecrement <= endX) {
                centerX = endX;
            } else {
                centerX = centerX - closeUpDecrement;
            }
        } else if (centerX < endX) {
            if (centerX + closeUpDecrement >= endX) {
                centerX = endX;
            } else {
                centerX = centerX + closeUpDecrement;
            }
        }
    }

    public void nextAlpha(int maxY, int maxAlpha, int minAlpha) {
        int deltaY = maxY - centerY;
        float p = deltaY / Float.valueOf(maxY);
//        Log.d(TAG,"p = "+p);
        int deltaAlpha = maxAlpha - minAlpha;
        alpha = (int) (maxAlpha - deltaAlpha * p);
    }

}
