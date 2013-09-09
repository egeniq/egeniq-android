package com.egeniq.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * Custom Layout implementing the Checkable interface. <br>
 * Useful for ListViews when using 'setItemChecked'.
 * 
 * Note: Use android:duplicateParentState="true" on the child views to propagate the state, if required.
 * 
 * @author Özcan Kaymak
 */
public class CheckableLayout extends LinearLayout implements Checkable {
    private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };
    private boolean _isChecked = false;
    private OnCheckedChangeListener _onCheckedChangeListener;

    /**
     * Interface definition for a callback to be invoked when the checked state of this View is changed.
     */
    public interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a checkable view has changed.
         * 
         * @param checkableView The view whose state has changed.
         * @param isChecked The new checked state of checkableView.
         */
        public void onCheckedChanged(View checkableView, boolean isChecked);
    }

    /**
     * Constructor.
     */
    public CheckableLayout(Context context) {
        super(context);
    }

    /**
     * Constructor.
     */
    public CheckableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isChecked() {
        return _isChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked != _isChecked) {
            _isChecked = checked;
            refreshDrawableState();

            if (_onCheckedChangeListener != null) {
                _onCheckedChangeListener.onCheckedChanged(this, checked);
            }
        }
    }

    @Override
    public void toggle() {
        setChecked(!_isChecked);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    /**
     * Register a callback to be invoked when the checked state of this view changes.
     * 
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        _onCheckedChangeListener = listener;
    }
}
