package com.egeniq.widget;

/**
 * Created by josvanegmond on 05/10/15.
 */
public interface IFormattableTextView {

    CharSequence getOriginalText();
    void formatText(Object... format);
}
