package io.msgs.gcm;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.egeniq.BuildConfig;

/**
 * Notification manager.
 */
public class NotificationManager {
    private final String TAG = NotificationManager.class.getSimpleName();
    private final boolean DEBUG = BuildConfig.DEBUG;
    
    private final String NOTIFICATION_TOKEN_KEY = "notificationToken";
    
    private final Context _context;
    private final String _serviceBaseURL;
    
    /**
     * Constructor.
     * 
     * @param context
     * @param serviceBaseURL
     */
    public NotificationManager(Context context, String serviceBaseURL) {
        _context = context;
        _serviceBaseURL = serviceBaseURL;
    }
    
    /**
     * Register device.
     * 
     * @param registrationId
     */
    public void registerDevice(final String registrationId) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    String notificationToken = _getNotificationToken();
                    
                    HttpPost httpPost = new HttpPost(_serviceBaseURL + "/register");
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("deviceFamily", "android"));
                    nameValuePairs.add(new BasicNameValuePair("deviceToken", registrationId));
                    if (notificationToken != null) {
                        nameValuePairs.add(new BasicNameValuePair("notificationToken", notificationToken));
                    }

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));

                    if (DEBUG) {
                        Log.d(TAG, "Send device registration request to " + httpPost.getURI());
                        Log.d(TAG, "Registration ID: " + registrationId);
                    }                    
                    
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    notificationToken = EntityUtils.toString(httpResponse.getEntity());
                    
                    if (DEBUG) {
                        Log.d(TAG, "Notification token: " + notificationToken);
                    }

                    if (!"ERROR".equals(notificationToken)) {
                        _setNotificationToken(notificationToken);
                    }
                } catch (Exception e) {
                    if (DEBUG) {
                        Log.e(TAG, "Error registering device for notifications", e);
                    }
                }

                return null;
            }
        }.execute();         
    }
    
    /**
     * Unregister device.
     * 
     * @param registrationId
     */
    public void unregisterDevice(String registrationId) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    String notificationToken = _getNotificationToken();
                    if (notificationToken == null) {
                        return null;
                    }
                    
                    _setNotificationToken(null);
                    
                    HttpPost httpPost = new HttpPost(_serviceBaseURL + "/unregister");
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("deviceFamily", "android"));
                    nameValuePairs.add(new BasicNameValuePair("notificationToken", notificationToken));

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));

                    if (DEBUG) {
                        Log.d(TAG, "Send device unregister request to " + httpPost.getURI());
                    }                    
                    
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    httpClient.execute(httpPost);
                } catch (Exception e) {
                    if (DEBUG) {
                        Log.e(TAG, "Error unregistering device for notifications", e);
                    }
                }

                return null;
            }
        }.execute();           
    }
    
    /**
     * Returns the current notification token.
     * 
     * @return Notification token.
     */
    private String _getNotificationToken() {
        return _context.getSharedPreferences(TAG, Context.MODE_PRIVATE).getString(NOTIFICATION_TOKEN_KEY, null);
    }
    
    /**
     * Sets the new notificaction token.
     * 
     * @param notificationToken
     */
    private void _setNotificationToken(String notificationToken) {
        SharedPreferences.Editor editor =  _context.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();        
        editor.putString(NOTIFICATION_TOKEN_KEY, notificationToken);
        editor.commit();
    }
}
