package com.idisfkj.customvieworviewgroup.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.idisfkj.customvieworviewgroup.R;

import java.util.Random;

/**
 * Created by idisfkj on 16/11/12.
 * Email : idisfkj@gmail.com.
 */
public class AuthCodeTextView extends View implements View.OnClickListener {
    private final int DEFAULT_AUTO_NUM = 4;
    private String autoText;
    private int autoSize;
    private int autoNum;
    private Paint mPaint;
    private Paint pointPaint;
    private Paint linePaint;
    private Rect bounds;
    private int width;
    private int height;
    private StringBuilder builder;
    private Random random;
    private int[] colorRes;

    public AuthCodeTextView(Context context) {
        this(context, null);
    }

    public AuthCodeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AuthCodeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs, defStyleAttr);
        initData();
    }

    private void getAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AuthCodeTextView, defStyleAttr, 0);
        autoSize = (int) typedArray.getDimension(R.styleable.AuthCodeTextView_autoSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        autoNum = typedArray.getInteger(R.styleable.AuthCodeTextView_autoNum, DEFAULT_AUTO_NUM);
        typedArray.recycle();
    }

    private void initData() {
        random = new Random();
        updateText();

        mPaint = new Paint();
        mPaint.setTextSize(autoSize);
        bounds = new Rect();
        //获取包含文本的矩形
        mPaint.getTextBounds(autoText, 0, autoText.length(), bounds);
        mPaint.setAntiAlias(true);

        pointPaint = new Paint();
        pointPaint.setColor(getResources().getColor(R.color.autoPoint));
        pointPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);

        this.setOnClickListener(this);
        colorRes = new int[]{R.color.autoBlue, R.color.autoCyan, R.color.autoGreen
                , R.color.autoPurple, R.color.autoRed, R.color.autoYellow};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            //直接获取精确的宽度
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //计算出宽度(文本的宽度+padding的大小)
            width = bounds.width() + getPaddingLeft() + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            //直接获取精确的高度
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //计算出高度(文本的高度+padding的大小)
            height = bounds.height() + getPaddingBottom() + getPaddingTop();
        }
        //设置获取的宽高
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        mPaint.setColor(getResources().getColor(R.color.autoCodeBg));
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mPaint.getTextBounds(autoText, 0, autoText.length(), bounds);
        //绘制文本
        for (int i = 0; i < autoText.length(); i++) {
            mPaint.setColor(getResources().getColor(colorRes[random.nextInt(6)]));
            canvas.drawText(autoText, i, i + 1, getWidth() / 2 - bounds.width() / 2 + i * bounds.width() / autoNum
                    , bounds.height() + random.nextInt(getHeight() - bounds.height())
                    , mPaint);
        }

        //绘制干扰点
        for (int j = 0; j < 250; j++) {
            canvas.drawPoint(random.nextInt(getWidth()), random.nextInt(getHeight()), pointPaint);
        }

        //绘制干扰线
        for (int k = 0; k < 20; k++) {
            int startX = random.nextInt(getWidth());
            int startY = random.nextInt(getHeight());
            int stopX = startX + random.nextInt(getWidth() - startX);
            int stopY = startY + random.nextInt(getHeight() - startY);
            linePaint.setColor(getResources().getColor(colorRes[random.nextInt(6)]));
            canvas.drawLine(startX, startY, stopX, stopY, linePaint);
        }
    }

    @Override
    public void onClick(View v) {
        updateText();
        invalidate();
    }

    /**
     * 更新文本
     */
    private void updateText() {
        builder = new StringBuilder();
        while (builder.length() < autoNum) {
            switch (random.nextInt(3)) {
                case 0:
                    builder.append(random.nextInt(10));
                    break;
                case 1:
                    builder.append((char) (65 + random.nextInt(26)));
                    break;
                case 2:
                    builder.append((char) (97 + random.nextInt(26)));
                    break;
            }
        }
        autoText = builder.toString();
    }

    /**
     * 获取文本
     *
     * @return
     */
    public CharSequence getAutoText() {
        return autoText;
    }
}
