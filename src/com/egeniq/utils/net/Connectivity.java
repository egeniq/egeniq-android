package com.egeniq.utils.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Connectivity info.
 */
public class Connectivity {
    private final static String TAG = Connectivity.class.getName();
    private final static boolean DEBUG = false;    
    private final Context _context;
    
    /**
     * Constructor.
     * 
     * @param context Application context.
     */
    public Connectivity(Context context) {
        _context = context; 
    }

    /**
     * Returns whatever we have a mobile connection.
     * 
     * @return mobile connection?
     */
    public boolean isMobileConnected() {
        ConnectivityManager manager = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean result = info != null && info.isConnected();
        if (DEBUG) {
            Log.d(TAG, "Is mobile connected? " + result);
        }
        return result;
    }    
    
    /**
     * Returns whatever we have a wifi connection.
     * 
     * @return wifi connection?
     */    
    public boolean isWifiConnected() {
        ConnectivityManager manager = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean result = info != null && info.isConnected();
        if (DEBUG) {
            Log.d(TAG, "Is wifi connected? " + result);
        }
        return result;
    }
    
    /**
     * Returns whatever we have an internet connection.
     * 
     * @return internet connection?
     */
    public boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        boolean result = info != null && info.isConnected();
        if (DEBUG) {
            Log.d(TAG, "Is connected? " + result);
        }
        return result;
    }
}
