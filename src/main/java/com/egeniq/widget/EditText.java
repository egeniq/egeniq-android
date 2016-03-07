package com.egeniq.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.egeniq.R;
import com.egeniq.utils.loader.FontLoader;

/**
 * Custom EditText to enable setting custom fonts
 * 
 * @author Özcan Kaymak
 */
public class EditText extends android.widget.EditText {

    private int _style;

    /**
     * Default constructor
     */
    public EditText(Context context) {
        super(context);
        if (isInEditMode()) {
            return;
        }
    }

    /**
     * Constructor setting the custom font
     */
    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            FontLoader.setCustomFont(this, context, attrs, R.styleable.TextView, R.styleable.TextView_font, _style);
        }
    }

    /**
     * Constructor setting the custom font
     */
    public EditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) {
            FontLoader.setCustomFont(this, context, attrs, R.styleable.TextView, R.styleable.TextView_font, _style);
        }
    }

    /**
     * Get the style from the overridden method
     */
    @Override
    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(tf, style);
        _style = style;
    }
}
