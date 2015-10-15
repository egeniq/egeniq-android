package com.egeniq.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.egeniq.R;
import com.egeniq.utils.loader.FontLoader;

/**
 * Custom TextView to enable setting custom fonts
 *
 * @author Özcan Kaymak
 */
public class TextView extends android.widget.TextView implements IFormattableTextView {

    private int _style;
    private CharSequence _originalText;

    /**
     * Default constructor
     */
    public TextView(Context context) {
        super(context);
        if (isInEditMode()) {
            return;
        }

        _init();
    }

    /**
     * Constructor setting the custom font
     */
    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            FontLoader.setCustomFont(this, context, attrs, R.styleable.TextView, R.styleable.TextView_font, _style);
        }

        _init();
    }

    /**
     * Constructor setting the custom font
     */
    public TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            FontLoader.setCustomFont(this, context, attrs, R.styleable.TextView, R.styleable.TextView_font, _style);
        }

        _init();
    }

    /**
     * Get the style from the overridden method
     */
    @Override
    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(tf, style);
        _style = style;
    }

    @Override
    public CharSequence getOriginalText() {
        return _originalText;
    }

    private void _init() {
        _originalText = getText();
    }

    @Override
    public void formatText(Object... format) {
        setText(String.format(getOriginalText().toString(), format));
    }
}
