package com.shyz.clean.view.shortvideo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class CleanShortVideoSlideView extends LinearLayout {
    final String TAG = getClass().getSimpleName();

    public CleanShortVideoSlideView(Context context) {
        super(context);
    }

    public CleanShortVideoSlideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CleanShortVideoSlideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent");


        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent " + event.getAction());
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN ");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE ");
                return true;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "ACTION_CANCEL ");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP ");
                break;
        }
        return super.onTouchEvent(event);
    }
}
