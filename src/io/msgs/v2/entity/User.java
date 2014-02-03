package io.msgs.v2.entity;

import org.json.JSONObject;

/**
 * User entity.
 */
public class User extends AbstractEntity {
    /**
     * Constructor.
     */
    public User() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param properties
     */
    public User(JSONObject data) {
        super(data);
    }

    /**
     * Get external user identifier.
     */
    public String getExternalUserId() {
        return _getString("externalUserId");
    }

    /**
     * Sets the external user identifier.
     */
    public void setExternalUserId(String externalUserId) {
        _putString("externalUserId", externalUserId);
    }
}
