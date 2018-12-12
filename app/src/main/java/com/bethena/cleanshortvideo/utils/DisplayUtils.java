package com.bethena.cleanshortvideo.utils;

import android.content.Context;

public class DisplayUtils {

    public static int dp2px(float dp, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
