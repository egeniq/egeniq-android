package com.egeniq.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.MetricAffectingSpan;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;

import com.egeniq.utils.loader.TypefaceLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josvanegmond on 7/27/15.
 * Generic class for marking up a TextView with multiple MetricAffectingSpan objects.
 */
public class SpannableTextView extends TextView {

    /**
     * Data class for storing Markup data for each span applied to this TextView.
     */
    private class _MarkupData {
        private final String _text;
        private final MetricAffectingSpan[] _spanArray;

        public _MarkupData(String text, MetricAffectingSpan... spanArray) {
            this._text = text;
            this._spanArray = spanArray;
        }

        public MetricAffectingSpan[] getSpans() {
            return this._spanArray;
        }

        public String getText() {
            return this._text;
        }
    }

    private List<_MarkupData> _markupList;
    private String _markupText;

    public SpannableTextView(Context context) {
        super(context);
        this._initialize();
    }

    public SpannableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._initialize();
    }

    private void _initialize() {
        this._markupList = new ArrayList<_MarkupData>();
        this._markupText = "";
    }

    /**
     * Appends a styled text to the TextView. Update() must be called after the final call.
     * This method also obtains any custom font applied to the style via the font attribute.
     *
     * @param style R.style resource to style the text with
     * @param text  Text to append
     */
    public void append(int style, String text) {

        int[] attrs = {com.egeniq.R.attr.font};
        TypedArray styleArray = getContext().obtainStyledAttributes(style, attrs);
        String customFontName = styleArray.getString(0);
        Typeface typeface = TypefaceLoader.get(getContext(), customFontName);

        if (typeface != null) {
            CustomTypefaceSpan customTypefaceSpan = new CustomTypefaceSpan(typeface);
            TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(getContext(), style);
            this._markupList.add(new _MarkupData(text, textAppearanceSpan, customTypefaceSpan));
        } else {
            TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(getContext(), style);
            this._markupList.add(new _MarkupData(text, textAppearanceSpan));
        }

        this._markupText += text;
    }

    /**
     * Updates the TextView with markup text from the _markupText collection.
     * Each text segment is appended to the last.
     */
    public void update() {
        SpannableString text = new SpannableString(this._markupText);

        int start = 0, end = 0;
        for (_MarkupData markupData : this._markupList) {
            end += markupData.getText().length();

            for (MetricAffectingSpan span : markupData.getSpans()) {
                text.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //TODO: make Spanned option customizable
            }
            start = end;
        }

        super.setText(text, BufferType.SPANNABLE);
    }
}
