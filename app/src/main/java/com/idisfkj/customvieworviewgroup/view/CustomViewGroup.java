package com.idisfkj.customvieworviewgroup.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by idisfkj on 16/11/13.
 * Email : idisfkj@gmail.com.
 */
public class CustomViewGroup extends ViewGroup {
    public CustomViewGroup(Context context) {
        this(context, null);
    }

    public CustomViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        MarginLayoutParams params;

        int cl;
        int ct;
        int cr;
        int cb;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            params = (MarginLayoutParams) child.getLayoutParams();

            if (i == 0) {
                //左上角
                cl = params.leftMargin;
                ct = params.topMargin;
            } else if (i == 1) {
                //右上角
                cl = getMeasuredWidth() - params.rightMargin - child.getMeasuredWidth();
                ct = params.topMargin;
            } else if (i == 2) {
                //左下角
                cl = params.leftMargin;
                ct = getMeasuredHeight() - params.bottomMargin - child.getMeasuredHeight() - params.topMargin;
            } else {
                //右下角
                cl = getMeasuredWidth() - params.rightMargin - child.getMeasuredWidth();
                ct = getMeasuredHeight() - params.bottomMargin - child.getMeasuredHeight() - params.topMargin;
            }
            cr = cl + child.getMeasuredWidth();
            cb = ct + child.getMeasuredHeight();
            //确定子视图在父视图中放置的位置
            child.layout(cl, ct, cr, cb);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //测量child的宽高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int count = getChildCount();

        int tWidth = 0;
        int bWidth = 0;
        int lHeight = 0;
        int rHeight = 0;
        MarginLayoutParams params;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            params = (MarginLayoutParams) child.getLayoutParams();
            if (i == 0 || i == 1) {
                tWidth += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            }

            if (i == 2 || i == 3) {
                bWidth += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            }

            if (i == 0 || i == 2) {
                lHeight += child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            }

            if (i == 1 || i == 3) {
                rHeight += child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            }
        }

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : Math.max(tWidth, bWidth),
                heightMode == MeasureSpec.EXACTLY ? heightSize : Math.max(lHeight, rHeight));
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        //实现一个MarginLayoutParams使得子view能够使用layout_margin等属性布局
        return new MarginLayoutParams(getContext(), attrs);
    }
}
