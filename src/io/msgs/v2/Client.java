package io.msgs.v2;

import io.msgs.v2.helper.EndpointRequestHelper;
import io.msgs.v2.helper.UserRequestHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.client.entity.UrlEncodedFormEntity;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

import com.egeniq.BuildConfig;
import com.egeniq.utils.api.APIClient;
import com.egeniq.utils.api.APIException;
import com.egeniq.utils.api.APIUtils;

/**
 * Msgs client.
 * 
 * All methods are executed synchronously. You are responsible for <br>
 * wrapping the calls in an AsyncTask or something similar.
 */
public class Client {
    private final static String TAG = Client.class.getSimpleName();
    private final static boolean DEBUG = BuildConfig.DEBUG;

    private final static String DEVICE_TOKEN_KEY = "deviceToken";
    private final static String USER_TOKEN_KEY = "userToken";
    private final static String NOTIFICATION_TAG = "NotificationManager";
    private final static String NOTIFICATION_TOKEN_KEY = "notificationToken";

    private final Context _context;
    private final String _serviceBaseURL;
    private final String _apiKey;

    private APIClient _apiClient;

    private String _deviceToken;
    private String _userToken;

    public final static SimpleDateFormat DATE_FORMAT;
    static {
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * Constructor.
     * 
     * @param context
     * @param serviceBaseURL
     * @param apiKey
     */
    public Client(Context context, String serviceBaseURL, String apiKey) {
        _context = context;
        _serviceBaseURL = serviceBaseURL;
        _apiKey = apiKey;
    }

    /**
     * Returns the API client.
     * 
     * @return API client.
     */
    protected APIClient _getAPIClient() {
        if (_apiClient == null) {
            _apiClient = new APIClient(_serviceBaseURL); // add api key header
        }

        return _apiClient;
    }

    


    /**
     * Returns the Api Key.
     */
    public String getApiKey() {
        return _apiKey;
    }

    /**
     * Register device.
     * 
     * @param deviceId
     * @throws APIException
     */
    public void registerDevice(String deviceId) throws APIException {
        try {
            if (_getEndpointToken() != null && deviceId.equals(_getDeviceToken())) {
                return;
            }

            if (_getUserToken() != null) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userToken", _getUserToken()));
                params.add(new BasicNameValuePair("type", "android-tablet")); // TODO:
                params.add(new BasicNameValuePair("address", _getNotificationToken()));
                params.add(new BasicNameValuePair("name", "My tablet")); //TODO:
                params.add(new BasicNameValuePair("endpointSubscriptionsActive", "1")); //TODO:
                params.add(new BasicNameValuePair("userSubscriptionsActive", "1")); //TODO:
                
                HttpEntity entity = new UrlEncodedFormEntity(params);
                JSONObject object = _getAPIClient().post("/users/" + _getUserToken() + "/endpoints", entity, false);
                
                // Save token
                SharedPreferences.Editor editor = _context.getSharedPreferences(NOTIFICATION_TAG, Context.MODE_PRIVATE).edit();
                editor.putString(_apiKey + "." + DEVICE_TOKEN_KEY, APIUtils.getString(object, "token", null));
                editor.commit();
            } else {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("type", "android-tablet")); // TODO:
                params.add(new BasicNameValuePair("address", _getNotificationToken()));
                params.add(new BasicNameValuePair("name", "My tablet")); //TODO:
                params.add(new BasicNameValuePair("endpointSubscriptionsActive", "1")); //TODO:
                params.add(new BasicNameValuePair("userSubscriptionsActive", "1")); //TODO:
                HttpEntity entity = new UrlEncodedFormEntity(params);
                JSONObject object = _getAPIClient().post("/endpoints", entity, false);
                
                // Save token
                SharedPreferences.Editor editor = _context.getSharedPreferences(NOTIFICATION_TAG, Context.MODE_PRIVATE).edit();
                editor.putString(_apiKey + "." + DEVICE_TOKEN_KEY, APIUtils.getString(object, "token", null));
                editor.commit();
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "Error registering device", e);
            }

            if (!(e instanceof APIException)) {
                e = new APIException(e);
            }

            throw (APIException)e;
        }
    }

    /**
     * Unregister device.
     */
    public void unregisterDevice() {
        SharedPreferences.Editor editor = _context.getSharedPreferences(NOTIFICATION_TAG, Context.MODE_PRIVATE).edit();
        editor.remove(_apiKey + "." + DEVICE_TOKEN_KEY);
        editor.commit();
    }

    /**
     * Register user.
     * 
     * @param userId
     * @throws APIException
     */
    public void registerUser(String userId) throws APIException {
        // POST /users
        // indien device al bekend is (gebruiker was eerst niet ingelogd)
        // ook POST /users/:userToken/endpoints

        try {
            HttpEntity entity = new UrlEncodedFormEntity(null);
            JSONObject object = _getAPIClient().post("/users", entity, false);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "Error registering device", e);
            }

            if (!(e instanceof APIException)) {
                e = new APIException(e);
            }

            throw (APIException)e;
        }
    }

    /**
     * Unregister user.
     */
    public void unregisterUser() {
        // remove usertoken from shared pref
    }

    /**
     * Get user.
     */
    public UserRequestHelper user() {
        return new UserRequestHelper(this, _getUserToken());
    }

    /**
     * Get endpoint.
     */
    public EndpointRequestHelper endpoint() {
        return endpoint(_getEndpointToken());
    }

    /**
     * Get endpoint.
     * 
     * @param endpointToken
     */
    public EndpointRequestHelper endpoint(String endpointToken) {
        return new EndpointRequestHelper(this, endpointToken);
    }

    /**
    * Returns the current notification token.
    *
    * @return Notification token.
    */
    private String _getNotificationToken() {
        return _context.getSharedPreferences(NOTIFICATION_TAG, Context.MODE_PRIVATE).getString(_apiKey + "." + NOTIFICATION_TOKEN_KEY, null);
    }
    
    /**
     * Returns the currently known device token.
     * 
     * @return Device token.
     */
    private String _getDeviceToken() {
        return _context.getSharedPreferences(NOTIFICATION_TAG, Context.MODE_PRIVATE).getString(_deviceToken + "." + DEVICE_TOKEN_KEY, null);
    }

    /**
     * Get UserToken.
     */
    private String _getUserToken() {
        return _context.getSharedPreferences(NOTIFICATION_TAG, Context.MODE_PRIVATE).getString(_userToken + "." + USER_TOKEN_KEY, null);
    }

    /**
     * get EndpointToken.
     */
    private String _getEndpointToken() {
        return "endpoint"; // TODO
    }

}
