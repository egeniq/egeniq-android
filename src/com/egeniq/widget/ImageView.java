package com.egeniq.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Custom ImageView to stretch to the MATCH_PARENT-side
 * while preserving aspect ratio 
 * without needing any specific scale settings
 *
 * @see http://stackoverflow.com/a/12675430/265521
 */
public class ImageView extends android.widget.ImageView {

    public ImageView(Context context) {
        super(context);
    }

    public ImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getDrawable() == null) {
            return;
        }

        final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int w = getDrawable().getIntrinsicWidth() <= 0 ? 1 : getDrawable().getIntrinsicWidth();
        int h = getDrawable().getIntrinsicHeight() <= 0 ? 1 : getDrawable().getIntrinsicHeight();

        float desiredAspect = (float)w / (float)h;
        boolean resizeWidth = widthSpecMode != MeasureSpec.EXACTLY;
        boolean resizeHeight = heightSpecMode != MeasureSpec.EXACTLY;

        if (resizeWidth && !resizeHeight) {
            // Resize the width to the height, maintaining aspect ratio.
            int newWidth = (int)(desiredAspect * (getMeasuredHeight() - getPaddingTop() - getPaddingBottom())) + getPaddingLeft() + getPaddingRight();
            setMeasuredDimension(newWidth, getMeasuredHeight());
        } else if (resizeHeight && !resizeWidth) {
            // Resize the height to the width, maintaining aspect ratio.
            int newHeight = (int)((getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / desiredAspect) + getPaddingTop() + getPaddingBottom();
            setMeasuredDimension(getMeasuredWidth(), newHeight);
        }
    }
}
