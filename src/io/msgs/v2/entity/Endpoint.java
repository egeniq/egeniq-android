package io.msgs.v2.entity;

import org.json.JSONObject;

/**
 * Endpoint entity.
 *
 */
public class Endpoint {
    private String _token;
    private String _type;
    private String _address;
    private String _name;
    private boolean _endpointSubscriptionsActive;
    private boolean _userSubscriptionsActive;
    private JSONObject _data;
    
    public Endpoint() {
    }
    
    public Endpoint(String token, String type, String address, String name, boolean endpointSubscriptionsActive, boolean userSubscriptionsActive, JSONObject data) {
        _token = token;
        _type = type;
        _address = address;
        _name = name;
        _endpointSubscriptionsActive = endpointSubscriptionsActive;
        _userSubscriptionsActive = userSubscriptionsActive;
        _data = data;
    }

    /**
     * Get Token.
     */
    public String getToken() {
        return _token;
    }

    /**
     * Set Token.
     */
    public void setToken(String token) {
        _token = token;
    }

    /**
     * Get Type
     */
    public String getType() {
        return _type;
    }

    /**
     * Set Type.
     */
    public void setType(String type) {
        _type = type;
    }

    /**
     * Get Adddress.
     */
    public String getAddress() {
        return _address;
    }

    /**
     * Set Address.
     */
    public void setAddress(String address) {
        _address = address;
    }

    /**
     * Get Name.
     */
    public String getName() {
        return _name;
    }

    /**
     * Set Name.
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Set EndpointSubscriptionsActive.
     */
    public boolean isEndpointSubscriptionsActive() {
        return _endpointSubscriptionsActive;
    }

    /**
     * Get EndpointSubscriptionsActive.
     */
    public void setEndpointSubscriptionsActive(boolean endpointSubscriptionsActive) {
        _endpointSubscriptionsActive = endpointSubscriptionsActive;
    }

    /**
     * Get UserSubscriptionsActive.
     */
    public boolean isUserSubscriptionsActive() {
        return _userSubscriptionsActive;
    }

    /**
     * Set UserSubscriptionsActive.
     */
    public void setUserSubscriptionsActive(boolean userSubscriptionsActive) {
        _userSubscriptionsActive = userSubscriptionsActive;
    }

    /**
     * Get Data.
     */
    public JSONObject getData() {
        return _data;
    }

    /**
     * Set Data.
     */
    public void setData(JSONObject data) {
        _data = data;
    }
}
