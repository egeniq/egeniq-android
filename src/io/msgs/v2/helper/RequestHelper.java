package io.msgs.v2.helper;

import io.msgs.v2.Client;
import io.msgs.v2.entity.Subscription;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

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

    protected Client _client;

    private enum Sort {
        CREATED_AT("createdAt"), 
        CHANNEL_CREATED_AT("channel.createdAt"), 
        CHANNEL_CREATED_AT_ASC("channel.createdAt asc"), 
        CHANNEL_CREATED_AT_DESC("channel.createdAt desc"), 
        CHANNEL_UPDATED_AT("channel.updatedAt"), 
        CHANNEL_UPDATED_AT_ASC("channel.updatedAt asc"),
        CHANNEL_UPDATED_AT_DESC("channel.updatedAt desc");

        private String _name;
        
        private Sort(String name) {
            _name = name;
        }

        @Override
        public String toString() {
            return _name;
        }
    }

    /**
     * Get Subscriptions.
     */
    public Subscription[] getSubscriptions(String tags) throws APIException {
        return getSubscriptions(tags, null);
    }

    /**
     * Get Subscriptions.
     */
    public Subscription[] getSubscriptions(String tags, Sort[] sort) throws APIException {
        return getSubscriptions(tags, sort, 0, 0);
    }

    /**
     * Get Subscriptions.
     */
    public Subscription[] getSubscriptions(String tags, Sort[] sort, Integer limit, Integer offset) throws APIException {
        try {
            String notificationToken = _client.getNotificationToken();
            if (notificationToken == null) {
                throw new APIException("not_registered", "Device is not registered");
            }

            JSONArray rawSubscriptions = _client._getAPIClient().getArray("subscriptions/" + _client.getAppId() + "/" + notificationToken);

            ArrayList<Subscription> subscriptions = new ArrayList<Subscription>();
            for (int i = 0; i < rawSubscriptions.length(); i++) {
                JSONObject rawSubscription = rawSubscriptions.getJSONObject(i);

                Subscription subscription = new Subscription();
                subscription.setId(APIUtils.getInt(rawSubscription, "id", 0));
                subscription.setChannelId(APIUtils.getString(rawSubscription, "channelId", ""));

                String rawStartDate = APIUtils.getString(rawSubscription, "dateStart", null);
                Date startDate = rawStartDate == null ? null : Client.DATE_FORMAT.parse(rawStartDate);
                String rawEndDate = APIUtils.getString(rawSubscription, "dateEnd", null);
                Date endDate = rawEndDate == null ? null : Client.DATE_FORMAT.parse(rawEndDate);
                subscription.setDatePeriod(startDate, endDate);

                String rawStartTime = APIUtils.getString(rawSubscription, "timeStart", null);
                Time startTime = rawStartTime == null ? null : new Time(Integer.parseInt(rawStartTime.split(":")[0]), Integer.parseInt(rawStartTime.split(":")[1]));
                String rawEndTime = APIUtils.getString(rawSubscription, "timeEnd", null);
                Time endTime = rawEndTime == null ? null : new Time(Integer.parseInt(rawEndTime.split(":")[0]), Integer.parseInt(rawEndTime.split(":")[1]));
                subscription.setTimePeriod(startTime, endTime);

                subscriptions.add(subscription);
            }

            return subscriptions.toArray(new Subscription[0]);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "Error adding subscription", e);
            }

            if (!(e instanceof APIException)) {
                e = new APIException(e);
            }

            throw (APIException)e;
        }
    }

    /**
     * Subscribe.
     * 
     * @param channelId
     */
    public void subscribe(String channelId) throws APIException {
        subscribe(new Subscription().setChannelId(channelId));
    }

    /**
     * Subscribe.
     * 
     * @param subscription
     */
    public void subscribe(Subscription subscription) throws APIException {
        try {
            String notificationToken = _client.getNotificationToken();
            if (notificationToken == null) {
                throw new APIException("not_registered", "Device is not registered");
            }

            if (DEBUG) {
                Log.d(TAG, "Add subscription for channel " + subscription.getChannelId());
            }

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("appId", _client.getAppId()));
            params.add(new BasicNameValuePair("notificationToken", notificationToken));
            params.add(new BasicNameValuePair("channelId", subscription.getChannelId()));

            if (subscription.getStartDate() != null) {
                params.add(new BasicNameValuePair("dateStart", Client.DATE_FORMAT.format(subscription.getStartDate())));
            }

            if (subscription.getEndDate() != null) {
                params.add(new BasicNameValuePair("dateEnd", Client.DATE_FORMAT.format(subscription.getEndDate())));
            }

            if (subscription.getStartTime() != null) {
                params.add(new BasicNameValuePair("timeStart", String.format("%02d:%02d", subscription.getStartTime().getHours(), subscription.getStartTime().getMinutes())));
            }

            if (subscription.getEndTime() != null) {
                params.add(new BasicNameValuePair("timeEnd", String.format("%02d:%02d", subscription.getEndTime().getHours(), subscription.getEndTime().getMinutes())));
            }

            HttpEntity entity = new UrlEncodedFormEntity(params);
            JSONObject result = _client._getAPIClient().post("subscriptions", entity);
            int id = APIUtils.getInt(result, "id", 0);
            subscription.setId(id);

            if (DEBUG) {
                Log.d(TAG, "Subscription ID: " + id);
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "Error adding subscription", e);
            }

            if (!(e instanceof APIException)) {
                e = new APIException(e);
            }

            throw (APIException)e;
        }
    }

    /**
     * Unsubscribe from all subscriptions for the given channel.
     * 
     * @param channelId Channel ID.
     */
    public void unsubscribe(String channelId) throws APIException {
        String notificationToken = _client.getNotificationToken();
        if (notificationToken == null) {
            throw new APIException("not_registered", "Device is not registered");
        }

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("appId", _client.getApiKey()));
            params.add(new BasicNameValuePair("notificationToken", notificationToken));
            params.add(new BasicNameValuePair("channelId", channelId));

            HttpEntity entity = new UrlEncodedFormEntity(params);
            _client._getAPIClient().post("subscriptions/;delete", entity);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "Error adding subscription", e);
            }

            if (!(e instanceof APIException)) {
                e = new APIException(e);
            }

            throw (APIException)e;
        }
    }

    /**
     * Unsubscribe using the given subscription ID.
     * 
     * @param subscriptionId Subscription ID.
     */
    public void unsubscribe(int subscriptionId) throws APIException {
        String notificationToken = _client.getNotificationToken();
        if (notificationToken == null) {
            throw new APIException("not_registered", "Device is not registered");
        }

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("appId", _client.getAppId()));
            params.add(new BasicNameValuePair("notificationToken", notificationToken));
            params.add(new BasicNameValuePair("subscriptionId", String.valueOf(subscriptionId)));

            HttpEntity entity = new UrlEncodedFormEntity(params);
            _client._getAPIClient().post("subscriptions/;delete", entity);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "Error adding subscription", e);
            }

            if (!(e instanceof APIException)) {
                e = new APIException(e);
            }

            throw (APIException)e;
        }
    }

    /**
     * Unsubscribe the given subscription (used the subscription ID).
     * 
     * @param subscription Subscription ID.
     */
    public void unsubscribe(Subscription subscription) throws APIException {
        if (subscription.getId() != null) {
            unsubscribe(subscription.getId());
        }
    }

    /**
     * Get base path.
     */
    protected abstract String _getBasePath();
}
