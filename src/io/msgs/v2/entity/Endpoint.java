package io.msgs.v2.entity;

import org.json.JSONObject;

/**
 * Endpoint entity.
 */
public class Endpoint extends AbstractEntity {
    /**
     * Constructor.
     */
    public Endpoint() {
        super();
        setEndpointSubscriptionsActive(true);
        setUserSubscriptionsActive(true);
    }

    /**
     * Constructor.
     * 
     * @param data
     */
    public Endpoint(JSONObject data) {
        super(data);
    }

    /**
     * Get Token.
     */
    public String getToken() {
        return _getString("token");
    }

    /**
     * Set Token.
     */
    public void setToken(String token) {
        _putString("token", token);
    }

    /**
     * Get Type
     */
    public String getType() {
        return _getString("type");
    }

    /**
     * Set Type.
     */
    public void setType(String type) {
        _putString("type", type);
    }

    /**
     * Get Adddress.
     */
    public String getAddress() {
        return _getString("address");
    }

    /**
     * Set Address.
     */
    public void setAddress(String address) {
        _putString("address", address);
    }

    /**
     * Get Name.
     */
    public String getName() {
        return _getString("name");
    }

    /**
     * Set Name.
     */
    public void setName(String name) {
        _putString("name", name);
    }

    /**
     * Set EndpointSubscriptionsActive.
     */
    public Boolean getEndpointSubscriptionsActive() {
        return _getBoolean("endpointSubscriptionsActive");
    }

    /**
     * Get EndpointSubscriptionsActive.
     */
    public void setEndpointSubscriptionsActive(Boolean endpointSubscriptionsActive) {
        _putBoolean("endpointSubscriptionsActive", endpointSubscriptionsActive);
    }

    /**
     * Get UserSubscriptionsActive.
     */
    public Boolean getUserSubscriptionsActive() {
        return _getBoolean("userSubscriptionsActive");
    }

    /**
     * Set UserSubscriptionsActive.
     */
    public void setUserSubscriptionsActive(Boolean userSubscriptionsActive) {
        _putBoolean("userSubscriptionsActive", userSubscriptionsActive);
    }

    /**
     * Get Data.
     */
    public JSONObject getData() {
        return _getObject("data");
    }

    /**
     * Set Data.
     */
    public void setData(JSONObject data) {
        _putObject("data", data);
    }
}
