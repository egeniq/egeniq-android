package io.msgs.v2;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.util.Log;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.client.entity.UrlEncodedFormEntity;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

import com.egeniq.BuildConfig;
import com.egeniq.utils.api.APIException;

/**
 * Request Helper for Endpoint.
 * 
 */
public class EndpointRequestHelper extends RequestHelper {
    private final static String TAG = EndpointRequestHelper.class.getSimpleName();
    private final static boolean DEBUG = BuildConfig.DEBUG;

    /**
     * Constructor
     */
    public EndpointRequestHelper(Client client, String endpointToken) {
        super(client, "/endpoints/" + endpointToken);
        _endpointToken = endpointToken;
    }

    /**
     * Constructor
     */
    public EndpointRequestHelper(Client client, String userToken, String endpointToken) {
        super(client, "users" + userToken + "/endpoints/" + endpointToken);
        _userToken = userToken;
        _endpointToken = endpointToken;
    }

    /**
     * Update endpoint.
     */
    public void update(String address, Boolean endpointSubscriptionsActive, Boolean userSubscriptionsActive, JSONObject data) throws APIException {
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("endpointToken", _endpointToken));
            if (address != null) {
                params.add(new BasicNameValuePair("address", address));
            }
            if (endpointSubscriptionsActive != null) {
                params.add(new BasicNameValuePair("endpointSubscriptionsActive", endpointSubscriptionsActive ? "1" : "0"));    
            }
            if (userSubscriptionsActive != null) {
                params.add(new BasicNameValuePair("userSubscriptionsActive", userSubscriptionsActive ? "1" : "0"));    
            }
            if (data != null) {
                params.add(new BasicNameValuePair("data", data.toString()));
            }

            HttpEntity entity = new UrlEncodedFormEntity(params);
            _client._post("/endpoints/" + _endpointToken, entity, false);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "Error updating endpoint", e);
            }

            if (!(e instanceof APIException)) {
                e = new APIException(e);
            }

            throw (APIException)e;
        }
    }

    /**
     * Delete endpoint.
     */
    public void delete() throws APIException {
        try {
            _client._delete("/endpoints/" + _endpointToken, false);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "Error deleting endpoint", e);
            }

            if (!(e instanceof APIException)) {
                e = new APIException(e);
            }

            throw (APIException)e;
        }
    }

}
