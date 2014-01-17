package io.msgs.v2.entity;

import org.json.JSONObject;

/**
 * Channel entity.
 *
 */
public class Channel {
    private String _code;
    private String _name;
    private String[] _tags;
    private JSONObject _data;

    /**
     * Constructor.
     */
    public Channel() {
    }

    /**
     * Constructor.
     */
    public Channel(String code, String name, String[] tags, JSONObject data) {
        _code = code;
        _name = name;
        _tags = tags;
        _data = data;
    }

    /**
     * Get code.
     */
    public String getCode() {
        return _code;
    }

    /**
     * Set code.
     */
    public void setCode(String code) {
        _code = code;
    }

    /**
     * Get name.
     */
    public String getName() {
        return _name;
    }

    /**
     * Set name.
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Get tags.
     */
    public String[] getTags() {
        return _tags;
    }

    /**
     * Set tags.
     */
    public void setTags(String[] tags) {
        _tags = tags;
    }

    /**
     * Get data.
     */
    public JSONObject getData() {
        return _data;
    }

    /**
     * Set data.
     */
    public void setData(JSONObject data) {
        _data = data;
    }

}
