package io.msgs.v2;

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

    private final static String MSGS_TAG = "Msgs";

    private final static String KEY_ENDPOINT_ADDRESS = "deviceAdress";
    private final static String KEY_ENDPOINT_TOKEN = "endpointToken";
    private final static String KEY_USER_TOKEN = "userToken";
    private final static String KEY_EXTERNAL_USER_ID = "externalUserId";

    private final Context _context;
    private final String _serviceBaseURL;
    private final String _apiKey;
    private final String _deviceType;
    private final String _deviceName;

    private APIClient _apiClient;

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
    public void registerEndpoint(String address) throws APIException {
        try {
            String endpointToken = _getPreferenceForKey(KEY_ENDPOINT_TOKEN);
            String endpointAddress = _getPreferenceForKey(KEY_ENDPOINT_ADDRESS);
            String userToken = _getPreferenceForKey(KEY_USER_TOKEN);

            if (endpointToken != null && address.equals(endpointAddress)) {
                return;
            }

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("type", _getDeviceType()));
            params.add(new BasicNameValuePair("address", address));
            params.add(new BasicNameValuePair("name", _getDeviceName()));
            HttpEntity entity = new UrlEncodedFormEntity(params);
            JSONObject object = null;

            if (endpointToken != null && !address.equals(endpointAddress) && userToken != null) {
                object = _getAPIClient().post("/users/" + userToken + "/endpoints" + endpointToken, entity, false);
            } else if (endpointToken != null && !address.equals(endpointAddress)) {
                object = _getAPIClient().post("/endpoints" + endpointToken, entity, false);
            } else if (userToken != null) {
                object = _getAPIClient().post("/users/" + userToken + "/endpoints", entity, false);
            } else {
                object = _getAPIClient().post("/endpoints", entity, false);
            }

            _setPreferenceKey(KEY_ENDPOINT_TOKEN, APIUtils.getString(object, "token", null));
            _setPreferenceKey(KEY_ENDPOINT_ADDRESS, address);
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
        _setPreferenceKey(KEY_ENDPOINT_ADDRESS, null);
    }

    /**
     * Register user.
     * 
     * @param externalUserId
     * 
     * @throws APIException
     */
    public void registerUser(String externalUserId) throws APIException {
        try {
            String userToken = _getPreferenceForKey(KEY_USER_TOKEN);
            String externalUserToken = _getPreferenceForKey(KEY_EXTERNAL_USER_ID);

            if (userToken != null && externalUserId.equals(externalUserToken)) {
                return;
            }

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("externalUserId", externalUserId));

            HttpEntity entity = new UrlEncodedFormEntity(params);
            JSONObject object = _getAPIClient().post("/users", entity, false);
            _setPreferenceKey(KEY_USER_TOKEN, APIUtils.getString(object, "token", null));
            _setPreferenceKey(KEY_EXTERNAL_USER_ID, externalUserId);

            String endpointAddress = _getPreferenceForKey(KEY_ENDPOINT_ADDRESS);
            _setPreferenceKey(KEY_ENDPOINT_TOKEN, null);

            registerEndpoint(endpointAddress);
            return;
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "Error registering user", e);
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
        _setPreferenceKey(KEY_USER_TOKEN, null);
    }

    /**
     * Get user.
     */
    public UserRequestHelper user() {
        return new UserRequestHelper(this, _getPreferenceForKey(KEY_USER_TOKEN));
    }

    /**
     * Get endpoint.
     */
    public EndpointRequestHelper endpoint() {
        return endpoint(_getPreferenceForKey(KEY_ENDPOINT_TOKEN));
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
     * Get value from shared preferences.
     */
    private String _getPreferenceForKey(String key) {
        return _context.getSharedPreferences(MSGS_TAG, Context.MODE_PRIVATE).getString(key, null);
    }

    /**
     * Save value in shared preferences. <br>
     * If value is null, key will be removed.
     */
    private void _setPreferenceKey(String key, String value) {
        SharedPreferences.Editor editor = _context.getSharedPreferences(MSGS_TAG, Context.MODE_PRIVATE).edit();
        if (value == null) {
            editor.remove(key);
        } else {
            editor.putString(key, value);
        }
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

    /**
     * Perform a GET request with the ApiKey header.
     */
    protected JSONObject _get(String location, boolean useSSL) throws APIException {
        // TODO: insert header.
        // _getAPIClient().get(location, useSSL, headers);
        return null;
    }

    /**
     * Perform a POST request with the ApiKey header.
     */
    protected JSONObject _post(String location, HttpEntity entity, boolean useSSL) throws APIException {
        // TODO: insert header.
        // _getAPIClient().post(location, entity, useSSL, headers);
        return null;
    }

}
