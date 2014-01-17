package io.msgs.v2;

import io.msgs.v2.entity.Channel;
import io.msgs.v2.entity.Subscription;
import io.msgs.v2.entity.SubscriptionList;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.client.entity.UrlEncodedFormEntity;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

import com.egeniq.BuildConfig;
import com.egeniq.utils.api.APIException;
import com.egeniq.utils.api.APIUtils;

/**
 * Base RequestHelper
 */
public abstract class RequestHelper {
    private final static String TAG = RequestHelper.class.getSimpleName();
    private final static boolean DEBUG = BuildConfig.DEBUG;

    private Client _client;
    private String _basePath;

    private enum Sort {
        // @formatter:off
        CREATED_AT("createdAt"), 
        CHANNEL_CREATED_AT("channel.createdAt"), 
        CHANNEL_CREATED_AT_ASC("channel.createdAt asc"), 
        CHANNEL_CREATED_AT_DESC("channel.createdAt desc"), 
        CHANNEL_UPDATED_AT("channel.updatedAt"), 
        CHANNEL_UPDATED_AT_ASC("channel.updatedAt asc"), 
        CHANNEL_UPDATED_AT_DESC("channel.updatedAt desc");
        // @formatter:off

        private String _name;

        private Sort(String name) {
            _name = name;
        }

        @Override
        public String toString() {
            return _name;
        }
    }

    public RequestHelper(Client client, String basePath) {
        _client = client;
        _basePath = basePath;
    }

    /**
     * Get Subscriptions.
     * 
     * @param tags      Array of tags.
     * @param sort      Optional. Pass <b>null</b> to use default value.
     * @param limit     Optional. Pass <b>null</b> to use default value.
     * @param offset    Optional. Pass <b>null</b> to use default value.
     */
    public SubscriptionList getSubscriptions(String[] tags, Sort[] sort, Integer limit, Integer offset) throws APIException {
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tags", TextUtils.join(",", tags)));
            if (limit != null) {
                params.add(new BasicNameValuePair("limit", String.valueOf(limit)));
            }
            if (offset != null) {
                params.add(new BasicNameValuePair("offset", String.valueOf(offset)));
            }
            if (sort != null) {
                params.add(new BasicNameValuePair("sort", TextUtils.join(",", sort)));
            }

            HttpEntity entity = new UrlEncodedFormEntity(params);
            JSONObject object = _client._post(_getBasePath() + "/subscriptions", entity, false);

            return _parseSubscriptionList(object);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "Error getting subscriptions for user or endpoint", e);
            }

            if (!(e instanceof APIException)) {
                e = new APIException(e);
            }

            throw (APIException)e;
        }
    }

    /**
     * Get subscription.
     * 
     * @param channelCode
     */
    public Subscription getSubscription(String channelCode) throws APIException {
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("channelCode", channelCode));

            HttpEntity entity = new UrlEncodedFormEntity(params);
            JSONObject object = _client._post(_getBasePath() + "/subscriptions", entity, false);

            return _parseSubscription(object);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, String.format("Error getting subscription on channel '%s' for user or endpoint", channelCode), e);
            }

            if (!(e instanceof APIException)) {
                e = new APIException(e);
            }

            throw (APIException)e;
        }
    }

    /**
     * Returns if subscription is available.
     */
    public boolean isSubscribed(String channelCode) throws APIException {
        return getSubscription(channelCode) != null;
    }

    /**
     * Subscribe.
     * 
     * @param channelCode
     */
    public Subscription subscribe(String channelCode) throws APIException {
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("channelCode", channelCode));

            HttpEntity entity = new UrlEncodedFormEntity(params);
            JSONObject object = _client._post(_getBasePath() + "/subscriptions", entity, false);
            
            return _parseSubscription(object);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "Error subscribing user or endpoint", e);
            }

            if (!(e instanceof APIException)) {
                e = new APIException(e);
            }

            throw (APIException)e;
        }
    }

    /**
     * Unsubscribe.
     * 
     * @param channelCode
     */
    public void unsubscribe(String channelCode) throws APIException {
        try {
            _client._delete(_getBasePath() + "/subscriptions/" + channelCode, false);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "Error unsubscribing user or endpoint", e);
            }

            if (!(e instanceof APIException)) {
                e = new APIException(e);
            }

            throw (APIException)e;
        }
    }

    /**
     * Get base path.
     */
    private String _getBasePath() {
        return _basePath;
    }

    /**
     * Parse SubscriptionList.
     */
    private SubscriptionList _parseSubscriptionList(JSONObject object) {
        SubscriptionList subscriptionList = new SubscriptionList();
        try {
            subscriptionList.setTotal(APIUtils.getInt(object, "total", 0));
            subscriptionList.setCount(APIUtils.getInt(object, "count", 0));
            subscriptionList.setSubscriptions(_parseSubscriptions(object.getJSONArray("items")));
        } catch (JSONException e) {
            if (DEBUG) {
                Log.e(TAG, "Error parsing subscriptionlist", e);
            }
        }

        return subscriptionList;
    }

    /**
     * Parse Subscriptions.
     */
    private Subscription[] _parseSubscriptions(JSONArray array) {
        List<Subscription> subscriptions = new ArrayList<Subscription>();
        for (int i = 0; i < array.length(); i++) {
            try {
                Subscription subscription = new Subscription();
                subscription.setChannel(_parseChannel(APIUtils.getObject(array.getJSONObject(i), "channel", null)));
                subscriptions.add(subscription);
            } catch (JSONException e) {
                if (DEBUG) {
                    Log.e(TAG, "Error parsing subscription", e);
                }
            }
        }

        return subscriptions.toArray(new Subscription[0]);
    }
    
    /**
     * Parse Subscription.
     */
    private Subscription _parseSubscription(JSONObject object) {
        Subscription subscription = new Subscription();
        subscription.setChannel(_parseChannel(APIUtils.getObject(object, "channel", null)));

        return subscription;
    }

    /**
     * Parse Channel.
     */
    private Channel _parseChannel(JSONObject object) {
        Channel channel = new Channel();
        channel.setCode(APIUtils.getString(object, "code", null));
        channel.setName(APIUtils.getString(object, "name", null));
        channel.setTags(APIUtils.getStringArray(object, "tags", null));
        channel.setData(APIUtils.getObject(object, "data", null));

        return channel;
    }
}
