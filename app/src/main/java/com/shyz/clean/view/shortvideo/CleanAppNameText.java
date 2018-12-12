package com.shyz.clean.view.shortvideo;

import android.util.Log;

public class CleanAppNameText {
    final String TAG = getClass().getSimpleName();

    public String id;

    public String text;
    public int x;
    public int y;
    public int pathY;
    public int endX;

    public int textSize;
    public int alpha;

    //下掉速度
    public int fallDecrement;
    //靠拢速度
    public int closeUpDecrement;


    //下一帧
    public void nextFrame() {
        y = y + fallDecrement;
        if (x > endX) {
            if (x - closeUpDecrement <= endX) {
                x = endX;
            } else {
                x = x - closeUpDecrement;
            }
        } else if (x < endX) {
            if (x + closeUpDecrement >= endX) {
                x = endX;
            } else {
                x = x + closeUpDecrement;
            }
        }
    }

    public void nextAlpha(int maxY, int maxAlpha, int minAlpha) {
        int deltaY = maxY - y;
        float p = deltaY / Float.valueOf(maxY);
//        Log.d(TAG,"p = "+p);
        int deltaAlpha = maxAlpha - minAlpha;
        alpha = (int) (maxAlpha - deltaAlpha * p);
    }

    @Override
    public String toString() {
        return "CleanAppNameText{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", endX=" + endX +
                ", textSize=" + textSize +
                ", alpha=" + alpha +
                ", fallDecrement=" + fallDecrement +
                ", closeUpDecrement=" + closeUpDecrement +
                '}';
    }
}
