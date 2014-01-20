package io.msgs.v2;

import io.msgs.v2.entity.Endpoint;

import java.util.ArrayList;
import java.util.List;

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
    }

    /**
     * Update endpoint.
     */
    public void update(Endpoint endpoint) throws APIException {
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("endpointToken", endpoint.getToken()));
            params.add(new BasicNameValuePair("address", endpoint.getAddress()));
            params.add(new BasicNameValuePair("endpointSubscriptionsActive", endpoint.isEndpointSubscriptionsActive() ? "1" : "0"));
            params.add(new BasicNameValuePair("userSubscriptionsActive", endpoint.isUserSubscriptionsActive() ? "1" : "0"));
            params.add(new BasicNameValuePair("data", endpoint.getData().toString()));

            HttpEntity entity = new UrlEncodedFormEntity(params);
            _client._post("/endpoints/" + endpoint.getToken(), entity, false);
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
    public void delete(Endpoint endpoint) throws APIException {
        try {
            _client._delete("/endpoints/" + endpoint.getToken(), false);
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
