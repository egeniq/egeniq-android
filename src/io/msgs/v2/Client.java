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

    private final static String DEVICE_ADDRESS_KEY = "deviceAdress";
    private final static String USER_TOKEN_KEY = "userToken";
    private final static String NOTIFICATION_TAG = "NotificationManager";
    private final static String NOTIFICATION_TOKEN_KEY = "notificationToken";

    private final Context _context;
    private final String _serviceBaseURL;
    private final String _apiKey;
    private final String _deviceType;
    private final String _deviceName;

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
    public Client(Context context, String serviceBaseURL, String apiKey, String deviceType, String deviceName) {
        _context = context;
        _serviceBaseURL = serviceBaseURL;
        _apiKey = apiKey;
        _deviceType = deviceType;
        _deviceName = deviceName;
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
     * 
     * @throws APIException
     */
    public void registerDevice(String deviceId) throws APIException {
        try {
            if (_getEndpointToken() != null && deviceId.equals(_getDeviceAddress())) {
                return;
            }

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("type", _getDeviceType()));
            params.add(new BasicNameValuePair("address", deviceId));
            params.add(new BasicNameValuePair("name", _getDeviceName()));
            HttpEntity entity = new UrlEncodedFormEntity(params);

            if (_getUserToken() != null) {
                JSONObject object = _getAPIClient().post("/users/" + _getUserToken() + "/endpoints", entity, false);
                _setEndpointToken(APIUtils.getString(object, "token", null));
            } else {
                JSONObject object = _getAPIClient().post("/endpoints", entity, false);
                _setEndpointToken(APIUtils.getString(object, "token", null));
                _setDeviceAddress(APIUtils.getString(object, "address", null));
            }
            if (!deviceId.equals(_getDeviceAddress())) {
                JSONObject object = _getAPIClient().post("/endpoints" + _getEndpointToken(), entity, false);
                _setDeviceAddress(APIUtils.getString(object, "address", null));
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
        editor.remove(_apiKey + "." + DEVICE_ADDRESS_KEY);
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
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("externalUserId", userId));

            HttpEntity entity = new UrlEncodedFormEntity(params);
            JSONObject object = _getAPIClient().post("/users", entity, false);
            _setUserToken(APIUtils.getString(object, "token", null));
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
        SharedPreferences.Editor editor = _context.getSharedPreferences(NOTIFICATION_TAG, Context.MODE_PRIVATE).edit();
        editor.remove(_apiKey + "." + USER_TOKEN_KEY);
        editor.commit();
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
     * get EndpointToken.
     */
    private String _getEndpointToken() {
        return _context.getSharedPreferences(NOTIFICATION_TAG, Context.MODE_PRIVATE).getString(_apiKey + "." + USER_TOKEN_KEY, null);
    }

    /**
     * Returns the currently known device token.
     * 
     * @return Device token.
     */
    private String _getDeviceAddress() {
        return _context.getSharedPreferences(NOTIFICATION_TAG, Context.MODE_PRIVATE).getString(_deviceToken + "." + DEVICE_ADDRESS_KEY, null);
    }

    /**
     * Get UserToken.
     */
    private String _getUserToken() {
        return _context.getSharedPreferences(NOTIFICATION_TAG, Context.MODE_PRIVATE).getString(_userToken + "." + USER_TOKEN_KEY, null);
    }

    /**
     * Save EndpointToken to shared preferences.
     */
    private void _setEndpointToken(String endpointToken) {
        SharedPreferences.Editor editor = _context.getSharedPreferences(NOTIFICATION_TAG, Context.MODE_PRIVATE).edit();
        editor.putString(_apiKey + "." + DEVICE_ADDRESS_KEY, endpointToken);
        editor.commit();
    }

    /**
     * Save DeviceAddress to shared preferences.
     */
    private void _setDeviceAddress(String deviceAddress) {
        SharedPreferences.Editor editor = _context.getSharedPreferences(NOTIFICATION_TAG, Context.MODE_PRIVATE).edit();
        editor.putString(_apiKey + "." + DEVICE_ADDRESS_KEY, deviceAddress);
        editor.commit();
    }

    /**
     * Save EndpointToken to shared preferences.
     */
    private void _setUserToken(String userToken) {
        SharedPreferences.Editor editor = _context.getSharedPreferences(NOTIFICATION_TAG, Context.MODE_PRIVATE).edit();
        editor.putString(_apiKey + "." + USER_TOKEN_KEY, userToken);
        editor.commit();
    }

    /**
     * Get device type, e.g. "android-tablet"
     */
    private String _getDeviceType() {
        return _deviceType;
    }

    /**
     * Get device name, e.g. "Samsung Galaxy S4"
     */
    private String _getDeviceName() {
        return _deviceName;
    }

}
