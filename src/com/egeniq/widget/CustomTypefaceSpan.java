package com.egeniq.widget;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 * Created by josvanegmond on 7/27/15.
 * Assisting class for multiple typefaces
 * Original code from http://stackoverflow.com/a/17961854
 */
public class CustomTypefaceSpan extends MetricAffectingSpan {
    private final Typeface _typeface;

    public CustomTypefaceSpan(Typeface typeface) {
        this._typeface = typeface;
    }

    @Override
    public void updateDrawState(final TextPaint drawState) {
        _apply(drawState);
    }

    /**
     * Updates the paint object with font styles and sets the typeface
     */
    @Override
    public void updateMeasureState(final TextPaint paint) {
        _apply(paint);
    }

    private void _apply(final Paint paint) {
        final Typeface oldTypeface = paint.getTypeface();
        final int oldStyle = (oldTypeface != null) ? oldTypeface.getStyle() : 0;
        final int fakeStyle = oldStyle & ~_typeface.getStyle();

        if ((fakeStyle & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fakeStyle & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(_typeface);
    }
}