package io.msgs.v2.entity;

import org.json.JSONObject;

/**
 * Channel entity.
 * 
 */
public class Channel extends AbstractEntity {
    /**
     * Constructor.
     */
    public Channel() {
        super();
    }

    /**
     * Constructor.
     */
    public Channel(JSONObject data) {
        super(data);
    }

    /**
     * Get code.
     */
    public String getCode() {
        return _getString("code");
    }

    /**
     * Set code.
     */
    public void setCode(String code) {
        _putString("code", code);
    }

    /**
     * Get name.
     */
    public String getName() {
        return _getString("name");
    }

    /**
     * Set name.
     */
    public void setName(String name) {
        _putString("name", name);
    }

    /**
     * Get tags.
     */
    public String[] getTags() {
        return _getStringArray("tags");
    }

    /**
     * Set tags.
     */
    public void setTags(String[] tags) {
        _putStringArray("tags", tags);
    }

    /**
     * Get data.
     */
    public JSONObject getData() {
        return _getObject("data");
    }

    /**
     * Set data.
     */
    public void setData(JSONObject data) {
        _putObject("data", data);
    }
}
