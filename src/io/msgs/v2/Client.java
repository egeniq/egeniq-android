package io.msgs.v2;

import io.msgs.v2.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import ch.boye.httpclientandroidlib.Header;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.client.entity.UrlEncodedFormEntity;
import ch.boye.httpclientandroidlib.message.BasicHeader;
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
    public Client(Context context, String serviceBaseURL, String apiKey, String deviceType) {
        _context = context;
        _serviceBaseURL = serviceBaseURL;
        _apiKey = apiKey;
        _deviceType = deviceType;
    }

    /**
     * Returns the API client.
     * 
     * @return API client.
     */
    protected APIClient _getAPIClient() {
        if (_apiClient == null) {
            _apiClient = new APIClient(_serviceBaseURL);
        }

        return _apiClient;
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
            String endpointToken = _getPreference(KEY_ENDPOINT_TOKEN);
            String userToken = _getPreference(KEY_USER_TOKEN);

            if (endpointToken != null && address.equals(_getPreference(KEY_ENDPOINT_ADDRESS))) {
                return;
            }

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("type", _getDeviceType()));
            params.add(new BasicNameValuePair("address", address));
            params.add(new BasicNameValuePair("name", Utils.getDeviceName()));
            HttpEntity entity = new UrlEncodedFormEntity(params);
            JSONObject object = null;

            if (endpointToken != null && userToken != null) {
                object = _post("/users/" + userToken + "/endpoints/" + endpointToken, entity, false);
            } else if (endpointToken != null) {
                object = _post("/endpoints" + endpointToken, entity, false);
            } else if (userToken != null) {
                object = _post("/users/" + userToken + "/endpoints/", entity, false);
            } else {
                object = _post("/endpoints", entity, false);
            }

            _setPreference(KEY_ENDPOINT_TOKEN, APIUtils.getString(object, "token", null));
            _setPreference(KEY_ENDPOINT_ADDRESS, address);
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
        _setPreference(KEY_ENDPOINT_ADDRESS, null);
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
            String userToken = _getPreference(KEY_USER_TOKEN);

            if (userToken != null && externalUserId.equals(_getPreference(KEY_EXTERNAL_USER_ID))) {
                return;
            }

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("externalUserId", externalUserId));

            HttpEntity entity = new UrlEncodedFormEntity(params);
            JSONObject object = _post("/users", entity, false);
            _setPreference(KEY_USER_TOKEN, APIUtils.getString(object, "token", null));
            _setPreference(KEY_EXTERNAL_USER_ID, externalUserId);

            String endpointAddress = _getPreference(KEY_ENDPOINT_ADDRESS);
            if (endpointAddress != null) {
                _setPreference(KEY_ENDPOINT_TOKEN, null);
                registerEndpoint(endpointAddress);
            }
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
        _setPreference(KEY_USER_TOKEN, null);
    }

    /**
     * Get user.
     */
    public UserRequestHelper user() {
        return new UserRequestHelper(this, _getPreference(KEY_USER_TOKEN));
    }

    /**
     * Get endpoint.
     */
    public EndpointRequestHelper endpoint() {
        return endpoint(_getPreference(KEY_ENDPOINT_TOKEN));
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
    private String _getPreference(String key) {
        return _context.getSharedPreferences(MSGS_TAG, Context.MODE_PRIVATE).getString(key, null);
    }

    /**
     * Save value in shared preferences. <br>
     * If value is null, key will be removed.
     */
    private void _setPreference(String key, String value) {
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
     * Get Api Header
     */
    private Header[] getApiHeader() {
        return new Header[] { new BasicHeader("X-MsgsIo-APIKey", _apiKey) };
    }

    /**
     * Perform a GET request with the ApiKey header.
     */
    protected JSONObject _get(String location, boolean useSSL) throws APIException {
        return _getAPIClient().get(location, useSSL, getApiHeader());
    }

    /**
     * Perform a POST request with the ApiKey header.
     */
    protected JSONObject _post(String location, HttpEntity entity, boolean useSSL) throws APIException {
        return _getAPIClient().post(location, entity, useSSL, getApiHeader());
    }

    /**
     * Perform a DELETE request with the ApiKey header.
     */
    protected JSONObject _delete(String location, boolean useSSL) throws APIException {
        return _getAPIClient().delete(location, useSSL, getApiHeader());
    }
}
