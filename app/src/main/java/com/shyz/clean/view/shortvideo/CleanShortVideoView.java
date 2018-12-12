package com.shyz.clean.view.shortvideo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.bethena.cleanshortvideo.R;
import com.bethena.cleanshortvideo.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * TODO: document your custom view class.
 */
public class CleanShortVideoView extends View {

    final String TAG = getClass().getSimpleName();

    Random random = new Random(System.currentTimeMillis());

    final int ANIM_STATUS_STOP = 0;
    final int ANIM_STATUS_SCANING = 1;
    final int ANIM_STATUS_SCAN_STOPING = 2;
    final int ANIM_STATUS_CLEANING = 3;
    final int ANIM_STATUS_CLEAN_STOPING = 4;
    int mAnimStatus;

    int mBottomToCenterDistance;
    int mCenterX;
    int mCenterY;

    private Camera mCamera;
    private Matrix mMatrix;
    private int mMatrixY;


    Paint mRectPaint;
    final int RECT_COLOR1 = 0x00FFFFFF;
    final int RECT_COLOR2 = 0x22FFFFFF;
    int mRectHeight;
    Path mRectPath;

    Paint mLine1Paint;
    Paint mLine2Paint;


    int mOvalWidthRadius;
    int mOvalHeightRadius;
    final int LINE_COLOR1 = 0x00FFFFFF;
    final int LINE_COLOR2 = 0x66FFFFFF;

    Paint mOvalPaint1;
    final int OVAL_COLOR1 = 0xFF5CD783;
    final int OVAL_COLOR1_1 = 0xFF0EC456;
    RectF mOval1;
    Paint mOvalPaint2;
    final int OVAL_COLOR2 = 0xFF16BD59;
    RectF mOval2;
    int mOvalDistance;

    Paint mRipplePaint;
    final int RIPPLE_COLOR1 = Color.parseColor("#FF000000");
    RectF mRippleOval1;
    final int RIPPLE_COLOR2 = 0xFFFFFFFF;
    RectF mRippleOval2;
    CleanRipple ripple;
    final int MAX_RIPPLE_ALPHA = 100;

    Paint mTextPaint;
    final int TEXT_COLOR = 0xFFFFFFFF;

    String[] mSourceCleanAppNames;
    final int MAX_APP_NAME_SHOW_COUNT = 5;
    List<CleanAppNameText> mAppNames = new ArrayList<>(MAX_APP_NAME_SHOW_COUNT);

    int[] textSizes = new int[3];

    final int MAX_TEXT_ALPHA = 140;
    final int MIN_TEXT_ALPHA = 102;

    int mTextInitMaxX;
    int mTextStartY;
    int mTextEndY;

    int mAppnameMaxFallDecrement;
    int mMinFallDecrement;

    Paint mBubblePaint;
    final int BUBBLE_COLOR = 0xFFFFFFFF;

    final int MAX_BUBBLE_COUNT = 5;
    int[] bubbleRadiuses = new int[3];
    List<CleanScanBubble> mBubbles = new ArrayList<>(MAX_BUBBLE_COUNT);
    final int MAX_BUBBLE_ALPHA = 102;
    final int MIN_BUBBLE_ALPHA = 20;

    int mBubbleMaxFallDecrement;

    final int MAX_CLEAN_BUBBLE_COUNT = 20;
    List<CleanBubble> mCleanBubbles = new ArrayList<>(MAX_CLEAN_BUBBLE_COUNT);


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

    public void setbottomToCenterDistance(int bottomToCenterDistance) {
        this.mBottomToCenterDistance = bottomToCenterDistance;
        mCenterY = getHeight() - mBottomToCenterDistance;
        postInvalidate();
    }

    private void init(AttributeSet attrs, int defStyle) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CleanShortVideoView, defStyle, 0);

        mBottomToCenterDistance = a.getDimensionPixelOffset(R.styleable.CleanShortVideoView_bottom_to_center_distance, 10);

        a.recycle();

        mSourceCleanAppNames = getContext().getResources().getStringArray(R.array.clean_short_video_support_app_name);

        textSizes[0] = getContext().getResources().getDimensionPixelSize(R.dimen.clean_short_video_appname_textsize1);
        textSizes[1] = getContext().getResources().getDimensionPixelSize(R.dimen.clean_short_video_appname_textsize2);
        textSizes[2] = getContext().getResources().getDimensionPixelSize(R.dimen.clean_short_video_appname_textsize3);

        mAppnameMaxFallDecrement = getContext().getResources().getDimensionPixelOffset(R.dimen.clean_short_video_appname_max_fall_decrement);
        mMinFallDecrement = getContext().getResources().getDimensionPixelOffset(R.dimen.clean_short_video_min_fall_decrement);

        bubbleRadiuses[0] = getContext().getResources().getDimensionPixelSize(R.dimen.clean_short_video_bubble_radius1);
        bubbleRadiuses[1] = getContext().getResources().getDimensionPixelSize(R.dimen.clean_short_video_bubble_radius2);
        bubbleRadiuses[2] = getContext().getResources().getDimensionPixelSize(R.dimen.clean_short_video_bubble_radius3);


        mBubbleMaxFallDecrement = getContext().getResources().getDimensionPixelOffset(R.dimen.clean_short_video_bubble_max_fall_decrement);

        mCamera = new Camera();
        mMatrix = new Matrix();

        initPaint();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w / 2;
        mCenterY = h - mBottomToCenterDistance;

        mRectHeight = mCenterY / 3 * 2;

        LinearGradient lg = new LinearGradient(0, -mRectHeight, 0, 0, RECT_COLOR1, RECT_COLOR2, Shader.TileMode.CLAMP);
        mRectPaint.setShader(lg);


        mOvalWidthRadius = w / 4;
        mOvalHeightRadius = h / 15;

        mRectPath = new Path();
        mRectPath.moveTo(-mOvalWidthRadius, 0);
        mRectPath.lineTo(mOvalWidthRadius, 0);
        int increaseWidht = mOvalWidthRadius / 2;
        mRectPath.lineTo(mOvalWidthRadius + increaseWidht, -mRectHeight);
        mRectPath.lineTo(-mOvalWidthRadius - increaseWidht, -mRectHeight);
        mRectPath.close();


        LinearGradient line1Lg = new LinearGradient(mOvalWidthRadius, 0, (mOvalWidthRadius + increaseWidht / 2), -mRectHeight / 2, LINE_COLOR1, LINE_COLOR2, Shader.TileMode.MIRROR);
        mLine1Paint.setShader(line1Lg);
        LinearGradient line2Lg = new LinearGradient(-mOvalWidthRadius,0, -(mOvalWidthRadius + increaseWidht / 2), -mRectHeight / 2, LINE_COLOR1, LINE_COLOR2, Shader.TileMode.MIRROR);
        mLine2Paint.setShader(line2Lg);

        mOvalDistance = dp2px(3.5f);

        LinearGradient lg2 = new LinearGradient(0, -mOvalHeightRadius, 0, mOvalHeightRadius, OVAL_COLOR1, OVAL_COLOR1_1, Shader.TileMode.CLAMP);
        mOvalPaint1.setShader(lg2);

        mOval1 = new RectF();
        mOval1.set(-mOvalWidthRadius, -mOvalHeightRadius, mOvalWidthRadius, mOvalHeightRadius);
        mOval2 = new RectF();
        mOval2.set(-mOvalWidthRadius, -mOvalHeightRadius, mOvalWidthRadius, mOvalHeightRadius);
//

        mRippleOval1 = new RectF();
//        mRippleOval1.set(-mOvalWidthRadius + 200, -mOvalHeightRadius + 20, mOvalWidthRadius - 200, mOvalHeightRadius - 20);

        mRippleOval2 = new RectF();
//        mRippleOval2.set(-mOvalWidthRadius - 120, -mOvalHeightRadius - 25, mOvalWidthRadius + 120, mOvalHeightRadius + 25);

        provideRipple();

        mTextInitMaxX = w / 3;
        mTextStartY = -mCenterY;
        mTextEndY = 0;
    }

    private int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void provideRipple() {
        ripple = new CleanRipple();
        ripple.outerHalfHeight = mOvalHeightRadius;
        ripple.outerHalfWidth = mOvalWidthRadius;
        ripple.innerHalfHeight = mOvalHeightRadius - dp2px(10);
        ripple.innerHalfWidth = mOvalWidthRadius - dp2px(30);
        ripple.alpha = MAX_RIPPLE_ALPHA;
        ripple.verticalDecrement = dp2px(0.5f);
        ripple.horizotalDecrement = dp2px(2);
    }

    private void initPaint() {
        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStyle(Paint.Style.FILL);

        mLine1Paint = new Paint();
        mLine1Paint.setAntiAlias(true);
        mLine1Paint.setStyle(Paint.Style.FILL);
        mLine1Paint.setStrokeWidth(DisplayUtils.dp2px(2f, getContext()));

        mLine2Paint = new Paint();
        mLine2Paint.setAntiAlias(true);
        mLine2Paint.setStyle(Paint.Style.FILL);
        mLine2Paint.setStrokeWidth(DisplayUtils.dp2px(2f, getContext()));

        mOvalPaint1 = new Paint();
        mOvalPaint1.setAntiAlias(true);
        mOvalPaint1.setStyle(Paint.Style.FILL);

        mOvalPaint2 = new Paint();
        mOvalPaint2.setAntiAlias(true);
        mOvalPaint2.setStyle(Paint.Style.FILL);

        mRipplePaint = new Paint();
        mRipplePaint.setAntiAlias(true);
        mRipplePaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(TEXT_COLOR);

        mBubblePaint = new Paint();
        mBubblePaint.setAntiAlias(true);
        mBubblePaint.setStyle(Paint.Style.FILL);
        mBubblePaint.setColor(BUBBLE_COLOR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mCenterX, mCenterY);

        canvas.save();
        canvas.translate(0, mOvalDistance);
        if (mAnimStatus == ANIM_STATUS_SCANING || mAnimStatus == ANIM_STATUS_SCAN_STOPING) {
            mRipplePaint.setXfermode(null);
            mRipplePaint.setColor(RIPPLE_COLOR2);
            mRipplePaint.setAlpha(ripple.alpha);
            mRippleOval2.set(-ripple.outerHalfWidth, -ripple.outerHalfHeight, ripple.outerHalfWidth, ripple.outerHalfHeight);
            canvas.drawOval(mRippleOval2, mRipplePaint);

            mRipplePaint.setColor(RIPPLE_COLOR1);
            mRipplePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            mRippleOval1.set(-ripple.innerHalfWidth, -ripple.innerHalfHeight, ripple.innerHalfWidth, ripple.innerHalfHeight);
            canvas.drawOval(mRippleOval1, mRipplePaint);

            ripple.nextFrame();
            ripple.nextAlpha(MAX_RIPPLE_ALPHA, mCenterX, mOvalWidthRadius);
            if (ripple.alpha <= 0) {
                provideRipple();
            }
        }
        canvas.restore();

        canvas.save();
//        mMatrix.reset();
//        mCamera.save();
//        mCamera.rotateX(degress);
//
//        mCamera.getMatrix(mMatrix);
//        mCamera.restore();
//
//        mMatrix.preTranslate(0, mMatrixY);
//        mMatrix.postTranslate(0, mMatrixY);
//
//        canvas.concat(mMatrix);

        canvas.drawPath(mRectPath, mRectPaint);

        canvas.drawLine(mOvalWidthRadius, 0, mOvalWidthRadius + mOvalWidthRadius / 2, -mRectHeight, mLine1Paint);
        canvas.drawLine(-mOvalWidthRadius, 0, -mOvalWidthRadius - mOvalWidthRadius / 2, -mRectHeight, mLine2Paint);

        canvas.restore();

        canvas.save();
        mOvalPaint2.setColor(OVAL_COLOR2);
        canvas.translate(0, mOvalDistance);
        canvas.drawOval(mOval2, mOvalPaint2);
        canvas.restore();

        canvas.save();
        canvas.drawOval(mOval1, mOvalPaint1);
        canvas.restore();


        if (mAnimStatus == ANIM_STATUS_SCANING) {
            provideAppNames();
            drawAppNames(canvas);

            provideBubbles();
            drawBubbles(canvas);
        } else if (mAnimStatus == ANIM_STATUS_SCAN_STOPING) {
            drawAppNames(canvas);
            drawBubbles(canvas);
            if (mAppNames.size() == 0 & mBubbles.size() == 0) {
                mAnimStatus = ANIM_STATUS_STOP;
                clearAnim();
            }
        } else if (mAnimStatus == ANIM_STATUS_CLEANING) {
            provideCleanBubbles();
            drawCleanBubbles(canvas);
        } else if (mAnimStatus == ANIM_STATUS_CLEAN_STOPING) {
            drawCleanBubbles(canvas);
            if (mCleanBubbles.size() == 0) {
                mAnimStatus = ANIM_STATUS_STOP;
                clearAnim();
            }
        }
    }

    private long startAnimTime;
    private boolean isRandomMaxCleanBubbleCount;

    void provideCleanBubbles() {
//        int maxBubbleCount = random.nextInt(MAX_CLEAN_BUBBLE_COUNT + 1) + 1;
//        if (isRandomMaxCleanBubbleCount) {
//            maxBubbleCount = MAX_CLEAN_BUBBLE_COUNT;
//        }
//
//        if (maxBubbleCount == MAX_CLEAN_BUBBLE_COUNT) {
//            isRandomMaxCleanBubbleCount = true;
//        }
        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - startAnimTime;
        int n = (int) (deltaTime / 100);
        int maxBubbleCount;
        if (5 - n > 0) {
            maxBubbleCount = MAX_CLEAN_BUBBLE_COUNT / (5 - n);
        } else {
            maxBubbleCount = MAX_CLEAN_BUBBLE_COUNT;
        }

        Log.d(TAG, "maxBubbleCount = " + maxBubbleCount);
        while (mCleanBubbles.size() < maxBubbleCount) {
            CleanBubble cleanBubble = new CleanBubble();
            cleanBubble.id = UUID.randomUUID().toString();
            cleanBubble.centerX = random.nextInt(mOvalWidthRadius * 2) - mOvalWidthRadius;
            cleanBubble.centerY = 0;
            int index = random.nextInt(3);
            cleanBubble.radius = bubbleRadiuses[index];
            cleanBubble.alpha = random.nextInt(50) + 1;
            cleanBubble.alphaSeed = random.nextInt(5) + 1;
            cleanBubble.riseDecrement = -(random.nextInt(mBubbleMaxFallDecrement) + mMinFallDecrement);
//            if (cleanBubble.centerX >= 0) {
//                cleanBubble.leaveDecrement = random.nextInt(5) ;
//            } else {
//                cleanBubble.leaveDecrement = random.nextInt(5) - 4;
//            }
            cleanBubble.leaveDecrement = random.nextInt(9) - 4;
            cleanBubble.leaveSeed = random.nextInt(1) + 1;
            mCleanBubbles.add(cleanBubble);
        }
    }

    void drawCleanBubbles(Canvas canvas) {
        int n = 0;
        for (; n < mCleanBubbles.size(); n++) {
            CleanBubble bubble = mCleanBubbles.get(n);
            mBubblePaint.setAlpha(bubble.alpha);
            canvas.drawCircle(bubble.centerX, bubble.centerY, bubble.radius, mBubblePaint);
            bubble.nextFrame();
            if (bubble.centerY <= -mCenterY || bubble.centerX >= mCenterX || bubble.centerX <= -mCenterX) {
                mCleanBubbles.remove(bubble);
                n--;
            }
        }
    }

    void provideBubbles() {
        while (mBubbles.size() < MAX_BUBBLE_COUNT) {
            CleanScanBubble bubble = new CleanScanBubble();
            bubble.centerX = random.nextInt(mTextInitMaxX * 2) - mTextInitMaxX;
            bubble.centerY = mTextStartY;
            int index = random.nextInt(3);
            bubble.radius = bubbleRadiuses[index];
            bubble.alpha = MAX_BUBBLE_ALPHA;
            bubble.endX = random.nextInt(mOvalWidthRadius * 2) - mOvalWidthRadius;
            bubble.id = UUID.randomUUID().toString();
            bubble.fallDecrement = random.nextInt(mBubbleMaxFallDecrement) + mMinFallDecrement;
            bubble.closeUpDecrement = random.nextInt(5) + 5;
            mBubbles.add(bubble);
        }
    }

    void drawBubbles(Canvas canvas) {
        int n = 0;
        for (; n < mBubbles.size(); n++) {
            CleanScanBubble bubble = mBubbles.get(n);
            mBubblePaint.setAlpha(bubble.alpha);
            canvas.drawCircle(bubble.centerX, bubble.centerY, bubble.radius, mBubblePaint);
            bubble.nextFrame();
            bubble.nextAlpha(mTextStartY, MAX_BUBBLE_ALPHA, MIN_BUBBLE_ALPHA);
            if (bubble.centerY >= 0) {
                mBubbles.remove(bubble);
                n--;
            }
        }
    }

    void provideAppNames() {
        while (mAppNames.size() < MAX_APP_NAME_SHOW_COUNT) {
            CleanAppNameText text = new CleanAppNameText();
            text.text = mSourceCleanAppNames[random.nextInt(mSourceCleanAppNames.length)];
            int textSizeIndex = random.nextInt(3);
            text.textSize = textSizes[textSizeIndex];
            text.x = random.nextInt(mTextInitMaxX * 2) - mTextInitMaxX;
            text.y = mTextStartY;
            text.pathY = random.nextInt(20) - 10;
            text.fallDecrement = random.nextInt(mAppnameMaxFallDecrement) + mMinFallDecrement;
            if (text.x > getWidth() / 4 || text.x < -getWidth() / 4) {
                text.closeUpDecrement = random.nextInt(5) + 30;
            } else {
                text.closeUpDecrement = random.nextInt(5) + 5;
            }
            text.closeUpDecrement = random.nextInt(5) + 5;
            mTextPaint.setTextSize(text.textSize);
            int textWidth = (int) mTextPaint.measureText(text.text);

            int entX = (random.nextInt(mOvalWidthRadius * 2) - mOvalWidthRadius);

            if (entX > 0) {
                text.endX = (random.nextInt(mOvalWidthRadius * 2) - mOvalWidthRadius) - textWidth;
            }

            if (text.endX < -mOvalWidthRadius) {
                text.endX = -mOvalWidthRadius;
            }

            if (text.endX + textWidth > mOvalWidthRadius) {
                text.endX = mOvalWidthRadius - textWidth;
            }


//            if (text.endX >= mOvalWidthRadius) {
//                Log.d(TAG, "---------------text.endX = " + text.endX);
//            }
            text.alpha = MAX_TEXT_ALPHA;
            text.id = UUID.randomUUID().toString();
            mAppNames.add(text);
        }
    }

    void drawAppNames(Canvas canvas) {
        int n = 0;
//        Log.d(TAG, "mAppNames.size = " + mAppNames.size());
        for (; n < mAppNames.size(); n++) {
            CleanAppNameText text = mAppNames.get(n);
            mTextPaint.setTextSize(text.textSize);

            mTextPaint.setAlpha(text.alpha);
//            canvas.drawText(text.text, text.x, text.y, mTextPaint);
            canvas.save();
            canvas.translate(text.x, text.y);
            Path path = new Path();
            path.lineTo(mTextPaint.measureText(text.text), text.pathY);

            canvas.drawPath(path, mTextPaint);
            canvas.drawTextOnPath(text.text, path, 0, 0, mTextPaint);
            canvas.restore();

            text.nextFrame();
            text.nextAlpha(mTextStartY, MAX_TEXT_ALPHA, MIN_TEXT_ALPHA);
//            if (n == 0) {
//                Log.d(TAG, "text = " + text);
//            }

            if (text.y >= 0) {
                mAppNames.remove(text);
                n--;
            }
        }
    }

    ValueAnimator valueAnimator;
    private int mMaxProgress = 100;

    public void startScanAnim() {
        if (mAnimStatus == ANIM_STATUS_SCANING) {
            return;
        }
        mAnimStatus = ANIM_STATUS_SCANING;
        valueAnimator = ValueAnimator.ofInt(1, mMaxProgress);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setDuration(10000);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                postInvalidate();
            }
        });
        valueAnimator.start();
    }

    private void clearAnim() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }


    public void startCleanAnim() {
        if (mAnimStatus == ANIM_STATUS_CLEANING) {
            return;
        }
        startAnimTime = System.currentTimeMillis();
        mAnimStatus = ANIM_STATUS_CLEANING;
        valueAnimator = ValueAnimator.ofInt(1, mMaxProgress);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setDuration(10000);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d(TAG, String.valueOf((int) animation.getAnimatedValue()));
                postInvalidate();
            }
        });
        valueAnimator.start();
    }

    public void stopAnim() {
        if (mAnimStatus == ANIM_STATUS_SCANING) {
            mAnimStatus = ANIM_STATUS_SCAN_STOPING;
        } else if (mAnimStatus == ANIM_STATUS_CLEANING) {
            mAnimStatus = ANIM_STATUS_CLEAN_STOPING;
        }
    }

    public void stopAnimForce() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }

        mBubbles.clear();
        mAppNames.clear();
        mCleanBubbles.clear();

        mAnimStatus = ANIM_STATUS_STOP;
        postInvalidate();
    }

    public void destroy() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }

    public void setProgress(int progress) {
        mMaxProgress = progress;
    }

    int degress;

    public void setDegress(int degress) {
        this.degress = degress;
        postInvalidate();
    }

    public void setMatrixY(int matrixY) {
        this.mMatrixY = matrixY;
        postInvalidate();
    }
}
