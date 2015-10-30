package com.egeniq.widget;

import android.content.Context;
import android.content.res.TypedArray;
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

        _init(context, null);
    }

    /**
     * Constructor setting the custom font
     */
    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            FontLoader.setCustomFont(this, context, attrs, R.styleable.TextView, R.styleable.TextView_font, _style);
        }

        _init(context, attrs);
    }

    /**
     * Constructor setting the custom font
     */
    public TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            FontLoader.setCustomFont(this, context, attrs, R.styleable.TextView, R.styleable.TextView_font, _style);
        }

        _init(context, attrs);
    }

    private void _init(Context context, AttributeSet attrs) {
        _originalText = getText();

        //if unformatted text is available, show that text initially
        if(context != null && attrs != null) {
            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TextView);
            String preformatText = styledAttrs.getString(R.styleable.TextView_preformat_text);
            setText(preformatText);
            styledAttrs.recycle();
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

    @Override
    public CharSequence getOriginalText() {
        return _originalText;
    }

    @Override
    public void formatOriginalText(Object... format) {
        setText((_originalText != null) ? String.format(_originalText.toString(), format) : "");
    }

    @Override
    public void formatNewText(String text, Object... format) {
        _originalText = text;
        formatOriginalText(format);
    }
}
