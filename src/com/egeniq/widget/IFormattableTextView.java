package com.egeniq.widget;

/**
 * Created by josvanegmond on 05/10/15.
 */
public interface IFormattableTextView {

    /**
     * Returns the original unformatted CharSequence of the TextView widget.
     */
    CharSequence getOriginalText();

    /**
     * Formats the text by the specified varargs, and applies it to the TextView widget.
     * @param text the text to format and apply to the TextView widget. Can be null to use the original set text.
     * @param format the format to apply to the original text, as specified by String.format().
     */
    void formatNewText(String text, Object... format);

    /**
     * Formats the original widget text by the specified varargs, and applies it to the TextView widget.
     * @param format the format to apply to the original text, as specified by String.format().
     */
    void formatOriginalText(Object... format);
}
