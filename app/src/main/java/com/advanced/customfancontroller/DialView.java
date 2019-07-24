package com.advanced.customfancontroller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

class DialView extends View {

    private float mWidth;
    private float mHeight;
    private float mRadius;

    private Paint mTextPaint;
    private Paint mDialPaint;

    private static final int SETTING_COUNT = 4;
    private int mCurrentSetting = 0;

    // these two used for efficiency
    private final StringBuffer mLabelStr = new StringBuffer(8);
    private final PointF mPoint = new PointF();

    public DialView(Context context) {
        super(context);
        init();
    }

    public DialView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(40f);

        mDialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDialPaint.setColor(Color.GRAY);

        mCurrentSetting = 0;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentSetting = (mCurrentSetting + 1) % SETTING_COUNT;
                if (mCurrentSetting >= 1) {
                    mDialPaint.setColor(Color.GREEN);
                } else {
                    mDialPaint.setColor(Color.GRAY);
                }
                invalidate(); // Redraw the view.
            }
        });
    }

    // SOS: called when view's size is changed, including 1st time the view is inflated
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        mRadius = (float) (Math.min(mWidth, mHeight) / 2 * 0.8);
    }

    private void computeXYForPosition(int setting, float radius) {
        double startAngle = Math.PI * (9 / 8d);   // Angles are in radians.
        double angle = startAngle + (setting * (Math.PI / 4));
        mPoint.set((float) (radius * Math.cos(angle)) + (mWidth / 2),
                (float) (radius * Math.sin(angle)) + (mHeight / 2));
    }

    // SOS: it seems that drawing is also relative to the view...
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the dial.
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mDialPaint);
        // Draw the numbers/labels
        final float labelRadius = mRadius + 20;
        for (int setting = 0; setting < SETTING_COUNT; setting++) {
            computeXYForPosition(setting, labelRadius);
            mLabelStr.setLength(0);
            mLabelStr.append(setting);
            canvas.drawText(mLabelStr, 0, mLabelStr.length(), mPoint.x, mPoint.y, mTextPaint);
        }
        // Draw the small indicator circle
        final float markerRadius = mRadius - 35;
        computeXYForPosition(mCurrentSetting, markerRadius);
        canvas.drawCircle(mPoint.x, mPoint.y, 20, mTextPaint);
    }
}
