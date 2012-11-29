package com.egeniq.widget;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * View stub for controllers.
 */
public class ControllerViewStub extends View {
    /**
     * Listener that is called when the stub is replaced by the real controller view.
     */
    public interface OnReplaceListener {
        public void onReplace(ControllerViewStub stub, View replacement);
    }
    
    private ArrayList<OnReplaceListener> _listeners = new ArrayList<OnReplaceListener>();

    /**
     * Constructor.
     * 
     * @param context
     */
    public ControllerViewStub(Context context) {
        super(context);
    }
    
    /**
     * Set the replacement listener.
     * 
     * @param listener
     */
    public void addOnReplaceListener(OnReplaceListener listener) {
        _listeners.add(listener);
    }
    
    /**
     * Remove the replacement listener.
     * 
     * @param listener
     */
    public void removeOnReplaceListener(OnReplaceListener listener) {
        _listeners.remove(listener);
    }
    
    /**
     * Replace view.
     * 
     * @param replacement Replacement view.
     */
    public void replace(View replacement) {
        replacement.setVisibility(getVisibility());
        
        if (getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup)getParent();
            int index = parent.indexOfChild(this);
            ViewGroup.LayoutParams params = getLayoutParams();
            parent.removeViewInLayout(this);
            parent.addView(replacement, index, params);   
        }        
        
        for (OnReplaceListener listener : _listeners) {
            listener.onReplace(this, replacement);
        }
    }
}