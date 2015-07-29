package com.egeniq.utils.loader;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;

/**
 * Created by josvanegmond on 7/27/15.
 *
 * Class caching Typefaces to reduce loading time
 * Original code from https://code.google.com/p/android/issues/detail?id=9904
 */
public class TypefaceLoader {
    private static final String TAG = "Typefaces";
    private static final String ASSET_PATH = "shared/fonts/";

    private static final Hashtable<String, Typeface> CACHE = new Hashtable<String, Typeface>();

    /**
     * Returns typeface from assets folder
     *
     * @param context  context object to obtain assets from
     * @param fontName path to the typeface font
     * @return Typeface object, or null if it can't be created from assets
     */
    public static Typeface get(Context context, String fontName) {
        synchronized (CACHE) {
            if (!CACHE.containsKey(fontName)) {
                try {
                    Typeface t = Typeface.createFromAsset(context.getAssets(), ASSET_PATH + fontName);
                    CACHE.put(fontName, t);
                } catch (Exception e) {
                    Log.e(TAG, "Could not get typeface '" + fontName
                            + "' because " + e.getMessage());
                    return null;
                }
            }
            return CACHE.get(fontName);
        }
    }
}