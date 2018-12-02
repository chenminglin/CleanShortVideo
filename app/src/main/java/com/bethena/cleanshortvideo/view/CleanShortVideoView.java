package com.bethena.cleanshortvideo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.bethena.cleanshortvideo.R;

/**
 * TODO: document your custom view class.
 */
public class CleanShortVideoView extends View {

    Paint paint;
    int mCenterX;
    int mCenterY;
    private Camera mCamera;
    private Matrix mMatrix;




    public CleanShortVideoView(Context context) {
        super(context);
        init(null, 0);
    }

    public CleanShortVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CleanShortVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CleanShortVideoView, defStyle, 0);

        initPaint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w / 2;
        mCenterY = h / 3 * 2;

    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);


        mCamera = new Camera();
        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LinearGradient lg = new LinearGradient(0, -200, 0, 0, Color.YELLOW, Color.WHITE, Shader.TileMode.CLAMP);
        paint.setShader(lg);
        canvas.translate(mCenterX, mCenterY);

        canvas.save();
        mMatrix.reset();
        mCamera.save();
        mCamera.rotateX(-45);
        mCamera.getMatrix(mMatrix);
        mCamera.restore();
        canvas.concat(mMatrix);

        canvas.drawRect(-100, -200, +100, 0, paint);

        canvas.restore();

    }


}
