package io.msgs.v2;

import io.msgs.v2.helper.EndpointRequestHelper;
import io.msgs.v2.helper.UserRequestHelper;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.client.entity.UrlEncodedFormEntity;

import com.egeniq.BuildConfig;
import com.egeniq.utils.api.APIClient;
import com.egeniq.utils.api.APIException;

/**
 * Msgs client.
 * 
 * All methods are executed synchronously. You are responsible for <br>
 * wrapping the calls in an AsyncTask or something similar.
 */
public class Client {
    private final static String TAG = Client.class.getSimpleName();
    private final static boolean DEBUG = BuildConfig.DEBUG;

    // private final static String NOTIFICATION_TAG = "NotificationManager";
    // private final static String NOTIFICATION_TOKEN_KEY = "notificationToken";

    private final Context _context;
    private final String _serviceBaseURL;
    private final String _apiKey;

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

    //
    // /**
    // * Returns the current notification token.
    // *
    // * @return Notification token.
    // */
    // public String getNotificationToken() {
    // return _context.getSharedPreferences(NOTIFICATION_TAG, Context.MODE_PRIVATE).getString(_apiKey + "." + NOTIFICATION_TOKEN_KEY, null);
    // }

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
        // ingelogd? POST /users/:userToken/endpoints ==> ingelogd == userToken != null ??
        // niet ingelogd? POST /endpoints

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
     * Unregister device.
     */
    public void unregisterDevice() {
        // remove deviceToken from shared pref
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
     * Get UserToken.
     */
    private String _getUserToken() {
        return "user"; // TODO
    }

    /**
     * get EndpointToken.
     */
    private String _getEndpointToken() {
        return "endpoint"; // TODO
    }
}
