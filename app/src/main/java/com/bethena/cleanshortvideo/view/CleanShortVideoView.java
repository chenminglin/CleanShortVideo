package com.bethena.cleanshortvideo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.bethena.cleanshortvideo.R;

/**
 * TODO: document your custom view class.
 */
public class CleanShortVideoView extends View {


    int mCenterX;
    int mCenterY;
    private Camera mCamera;
    private Matrix mMatrix;


    Paint mRectPaint;
    final int RECT_COLOR1 = 0x00FFFFFF;
    final int RECT_COLOR2 = 0x22FFFFFF;


    int mOvalWidthRaduis;
    int mOvalHeightRaduis;

    Paint mOvalPaint;
    final int OVAL_COLOR1 = 0xFF5CD783;
    RectF mOval1;
    final int OVAL_COLOR2 = 0xFF0EC456;
    RectF mOval2;

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

        a.recycle();

        mCamera = new Camera();
        mMatrix = new Matrix();

        initPaint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w / 2;
        mCenterY = h / 3 * 2;

        LinearGradient lg = new LinearGradient(0, -mCenterY, 0, 0, RECT_COLOR1, RECT_COLOR2, Shader.TileMode.CLAMP);
        mRectPaint.setShader(lg);

        mOvalWidthRaduis = w / 4;
        mOvalHeightRaduis = h / 10;

        mOval1 = new RectF();
        mOval1.set(-mOvalWidthRaduis, -mOvalHeightRaduis - 10, mOvalWidthRaduis, mOvalHeightRaduis-10);
        mOval2 = new RectF();
        mOval2.set(-mOvalWidthRaduis, -mOvalHeightRaduis , mOvalWidthRaduis, mOvalHeightRaduis );
//

    }

    private void initPaint() {
        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStyle(Paint.Style.FILL);

        mOvalPaint = new Paint();
        mOvalPaint.setAntiAlias(true);
        mOvalPaint.setStyle(Paint.Style.FILL);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mCenterX, mCenterY);


        canvas.save();
        mMatrix.reset();
        mCamera.save();
        mCamera.rotateX(-45);
        mCamera.getMatrix(mMatrix);
        mCamera.restore();
        canvas.concat(mMatrix);
        canvas.drawRect(-mOvalWidthRaduis, -mCenterY, +mOvalWidthRaduis, 0, mRectPaint);
        canvas.restore();

        canvas.save();
        mOvalPaint.setColor(OVAL_COLOR2);
        canvas.drawOval(mOval2, mOvalPaint);

        mOvalPaint.setColor(OVAL_COLOR1);
        canvas.drawOval(mOval1, mOvalPaint);

        canvas.restore();




    }


}
