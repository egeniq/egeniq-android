package com.egeniq.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.MetricAffectingSpan;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;

import com.egeniq.utils.loader.FontLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josvanegmond on 7/27/15.
 * Generic widget for marking up a TextView with multiple MetricAffectingSpan objects.
 * This widget supports inline styling parts of the text with custom fonts and styles, as well as inserting icons.
 * Recommend extending from this class and implement specific needs.
 *
 * Usage:
 * 1) set(...) or append(...)
 * 2) update()
 */
public class SpannableTextView extends TextView {

    private List<MarkupData> _markupList;
    private String _markupText;

    public SpannableTextView(Context context) {
        super(context);
        clear();
    }

    public SpannableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        clear();
    }

    /**
     * Clears the content of the SpannableTextView.
     */
    public void clear() {
        _markupList = new ArrayList<MarkupData>();
        _markupText = "";
    }

    /**
     * Overwrites the current content of this SpannableTextView with new content. Update() must be called to update the TextView visually.
     *
     * @param text Text to set
     */
    public void set(String text) {
        clear();
        append(text);
    }

    /**
     * Appends a text to the SpannableTextView with default style. Update() must be called to update the TextView visually.
     * This method also obtains any custom font applied to the style via the font attribute.
     *
     * @param text Text to append
     */
    public void append(String text) {
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(getContext(), android.R.style.TextAppearance);
        _markupList.add(new MarkupData(text, textAppearanceSpan));
        _markupText += text;
    }

    /**
     * Overwrites the current content of this SpannableTextView with new content. Update() must be called to update the TextView visually.
     *
     * @param style R.style resource to style the text with
     * @param text  Text to set
     */
    public void set(int style, String text) {
        clear();
        append(style, text);
    }

    /**
     * Appends a styled text to the SpannableTextView. Update() must be called to update the TextView visually.
     * This method also obtains any custom font applied to the style via the font attribute.
     *
     * @param style R.style resource to style the text with
     * @param text  Text to append
     */
    public void append(int style, String text) {

        int[] attrs = {com.egeniq.R.attr.font};
        TypedArray styleArray = getContext().obtainStyledAttributes(style, attrs);
        String customFontName = styleArray.getString(0);
        Typeface typeface = FontLoader.getFont(getContext(), customFontName);

        if (typeface != null) {
            CustomTypefaceSpan customTypefaceSpan = new CustomTypefaceSpan(typeface);
            TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(getContext(), style);
            _markupList.add(new MarkupData(text, textAppearanceSpan, customTypefaceSpan));
        } else {
            TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(getContext(), style);
            _markupList.add(new MarkupData(text, textAppearanceSpan));
        }

        _markupText += text;
    }

    /**
     * Overwrites the current content of this SpannableTextView with new content. Update() must be called to update the TextView visually.
     *
     * @param span the DynamicDrawableSpan to be applied
     */
    public void set(DynamicDrawableSpan span) {
        clear();
        append(span);
    }

    /**
     * Appends a DynamicDrawableSpan to the SpannableTextView. Update() must be called to update the TextView visually.
     *
     * @param span
     */
    public void append(DynamicDrawableSpan span) {
        _markupList.add(new MarkupData(" ", span)); //The space will be styled with an icon
        _markupText += " ";
    }

    /**
     * Overwrites the current content of this SpannableTextView with new content. Update() must be called to update the TextView visually.
     *
     * @param span the MetricAffectingSpan to be set
     * @param text the characters to be spanned.
     *             When no actual text is styled, insert the amount of characters equal to the amount of items being applied by the span.
     *             Example:
     *              An Image takes one character so insert one blank space: append(new ImageSpan(...), " ");
     */
    public void set(MetricAffectingSpan span, String text) {
        clear();
        append(span, text);
    }

    /**
     * Generic method to append a MetricAffectingSpan to the SpannableTextView. Update() must be called to update the TextView visually.
     *
     * @param span the MetricAffectingSpan to be appended
     * @param text the characters to be spanned.
     *             When no actual text is styled, insert the amount of characters equal to the amount of items being applied by the span.
     *             Example:
     *              An Image takes one character so insert one blank space: append(new ImageSpan(...), " ");
     */
    public void append(MetricAffectingSpan span, String text) {
        _markupList.add(new MarkupData(text, span));
        _markupText += text;
    }

    /**
     * Updates the TextView with the appended Spans.
     */
    public void update() {
        SpannableString text = new SpannableString(this._markupText);

        int start = 0, end = 0;
        for (MarkupData markupData : _markupList) {
            end += markupData.getText().length();

            for (MetricAffectingSpan span : markupData.getSpans()) {
                text.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //TODO: make Spanned option customizable
            }
            start = end;
        }

        super.setText(text, BufferType.SPANNABLE);
    }

    /**
     * Data class for storing Markup data for each span applied to this TextView.
     */
    private class MarkupData {
        private final String _text;
        private final MetricAffectingSpan[] _spanArray;

        public MarkupData(String text, MetricAffectingSpan... spanArray) {
            _text = text;
            _spanArray = spanArray;
        }

        public MetricAffectingSpan[] getSpans() {
            return _spanArray;
        }

        public String getText() {
            return _text;
        }
    }
}
