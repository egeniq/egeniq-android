package com.egeniq.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

/**
 * Created by josvanegmond on 10/08/15.
 * Class that fixes ImageSpan misalignment on the baseline within a TextView.
 * Original solution from: http://stackoverflow.com/a/11039337/4537699
 */
public class InlineImageSpan extends DynamicDrawableSpan {

    private Drawable _drawable;

    public InlineImageSpan(Drawable drawable) {
        super(DynamicDrawableSpan.ALIGN_BASELINE);
        _setDrawable(drawable);
    }

    private void _setDrawable(Drawable drawable) {
        _drawable = drawable;
    }

    @Override
    public Drawable getDrawable() {
        return _drawable;
    }

    //fixes misalignment to the baseline for resized inline images
    @Override
    public void draw(Canvas canvas, CharSequence text,
                     int start, int end, float x,
                     int top, int y, int bottom, Paint paint) {
        canvas.save();

        int transY = bottom - _drawable.getBounds().bottom;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            int textLength = text.length();
            for (int i = 0; i < textLength; i++) {
                if (Character.isLetterOrDigit(text.charAt(i))) {
                    transY -= paint.getFontMetricsInt().descent;
                    break;
                }
            }
        }

        canvas.translate(x, transY);
        _drawable.draw(canvas);
        canvas.restore();
    }
}
