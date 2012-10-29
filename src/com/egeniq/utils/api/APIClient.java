package com.egeniq.utils.api;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import com.egeniq.utils.net.HTTPClient;

import android.util.Log;
import ch.boye.httpclientandroidlib.Header;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpDelete;
import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.client.methods.HttpRequestBase;

/**
 * Simple API client.
 */
public class APIClient extends HTTPClient {
    /**
     * Specifies the expected response type of an API request.
     */
    protected enum ResponseType {
        JSONObject,
        JSONArray
    }
    
    private String _baseURL;
    private String _secureBaseURL;

    /**
     * Constructor.
     */
    public APIClient(String baseURL) {
        this(baseURL, baseURL);
    }
    
    /**
     * Constructor.
     */
    public APIClient(String baseURL, String secureBaseURL) {
        _baseURL = baseURL;
        _secureBaseURL = secureBaseURL;
    }
    
    /**
     * Returns the (secure?) base URL.
     * 
     * @param useSSL When true returns the secure base URL, else the default base URL.
     * 
     * @return base URL
     */
    protected String _getBaseURL(boolean useSSL) {
        return useSSL ? _secureBaseURL : _baseURL;
    }

    /**
     * Sets the base URLs.
     * 
     * @param baseURL
     * @param secureBaseURL
     */
    protected void _setBaseURLs(String baseURL, String secureBaseURL) {
        _baseURL = baseURL;
        _secureBaseURL = secureBaseURL;
    }    
    
    /**
     * Creates the full URL for the given location. 
     * 
     * Prepends the correct base URL (based on the useSSL) option unless the given location
     * itself is already a full HTTP URL.
     * 
     * @param location Location.
     * @param useSSL   Use SSL?
     * 
     * @return Full URL. 
     */
    protected String _getURL(String location, boolean useSSL) {
        if (location == null) {
            return _getBaseURL(useSSL);
        } else if (location.toLowerCase().startsWith("http:") || location.toLowerCase().startsWith("https:")) {
            return location;
        } else {
            return _getBaseURL(useSSL) + "/" + location;
        }
    }
    
    /**
     * Performs a GET request to the given location (which is appended to the base URL) 
     * and returns the result as a JSON object.
     * 
     * Doesn't use SSL.
     * 
     * @param location Location.
     * 
     * @return Object.
     */
    public JSONObject get(String location) throws APIException {
        return get(location, false, null);
    }    
    
    /**
     * Performs a GET request to the given location (which is appended to the base URL) 
     * and returns the result as a JSON object.
     * 
     * @param location Location.
     * @param useSSL   Use SSL when available.
     * 
     * @return Object.
     */
    public JSONObject get(String location, boolean useSSL) throws APIException {
        return get(location, useSSL, null);
    }

    /**
     * Performs a GET request to the given location (which is appended to the base URL) 
     * and returns the result as a JSON object.
     * 
     * @param location Location.
     * @param useSSL   Use SSL when available.
     * @param headers  HTTP headers.
     * 
     * @return Object.
     */
    public JSONObject get(String location, boolean useSSL, Header[] headers) throws APIException {
        HttpGet httpGet = new HttpGet(_getURL(location, useSSL));

        if (headers != null) {
            for (Header header : headers) {
                httpGet.addHeader(header);
            }
        }          
        
        if (_isLoggingEnabled()) {
            Log.d(_getLoggingTag(), "Fetch JSON object: " + httpGet.getURI());
        }
        
        return _executeJSONObjectAPIRequest(httpGet);
    }
    
    /**
     * Performs a GET request to the given location (which is appended to the base URL) 
     * and returns the result as a JSON array.
     * 
     * 
     * @param location Location.
     * 
     * @return List.
     */
    public JSONArray getArray(String location) throws APIException {  
        return getArray(location, false, null);
    }
    
    
    /**
     * Performs a GET request to the given location (which is appended to the base URL) 
     * and returns the result as a JSON array.
     * 
     * 
     * @param location Location.
     * @param useSSL   Use SSL when available?
     * 
     * @return List.
     */
    public JSONArray getArray(String location, boolean useSSL) throws APIException {  
        return getArray(location, useSSL, null);
    }    

    /**
     * Performs a GET request to the given location (which is appended to the base URL) 
     * and returns the result as a JSON array.
     * 
     * 
     * @param location Location.
     * @param useSSL   Use SSL when available?
     * @param headers  HTTP headers.
     * 
     * @return List.
     */
    public JSONArray getArray(String location, boolean useSSL, Header[] headers) throws APIException {
        HttpGet httpGet = new HttpGet(_getURL(location, useSSL));
        
        if (headers != null) {
            for (Header header : headers) {
                httpGet.addHeader(header);
            }
        }        

        if (_isLoggingEnabled()) {
            Log.d(_getLoggingTag(), "Fetch JSON array: " + httpGet.getURI());
        }
        
        return _executeJSONArrayAPIRequest(httpGet);
    }
    
    /**
     * Performs a POST request to the given location (which is appended to the base URL) 
     * and returns the result as a JSON object..
     * 
     * @param location Location.
     * @param entity   Post entity.
     */
    public JSONObject post(String location, HttpEntity entity) throws APIException {
        return post(location, entity, false, null);
    }    
    
    /**
     * Performs a POST request to the given location (which is appended to the base URL) 
     * and returns the result as a JSON object..
     * 
     * @param location Location.
     * @param entity   Post entity.
     * @param useSSL   Use SSL when available.
     */
    public JSONObject post(String location, HttpEntity entity, boolean useSSL) throws APIException {
        return post(location, entity, useSSL, null);
    }

    /**
     * Performs a POST request to the given location (which is appended to the base URL) 
     * and returns the result as a JSON object..
     * 
     * @param location Location.
     * @param entity   Post entity.
     * @param useSSL   Use SSL when available.
     * @param headers  Headers.
     * 
     * @return Object.
     */
    public JSONObject post(String location, HttpEntity entity, boolean useSSL, Header[] headers) throws APIException {
        HttpPost httpPost = new HttpPost(_getURL(location, useSSL));

        if (headers != null) {
            for (Header header : headers) {
                httpPost.addHeader(header);
            }
        }

        if (entity != null) {
            httpPost.setEntity(entity);
        }

        if (_isLoggingEnabled()) {
            Log.d(_getLoggingTag(), "Post: " + httpPost.getURI());
        }
        
        return _executeJSONObjectAPIRequest(httpPost);
    }
    
    /**
     * Performs a DELETE request to the given location (which is appended to the base URL) 
     * and returns the result as a JSON object.
     * 
     * @param location Location.
     * 
     * @return Object.
     */
    public JSONObject delete(String location) throws APIException {
        return delete(location, false, null);
    }
    
    /**
     * Performs a DELETE request to the given location (which is appended to the base URL) 
     * and returns the result as a JSON object.
     * 
     * @param location Location.
     * @param useSSL   Use SSL when available.
     * 
     * @return Object.
     */
    public JSONObject delete(String location, boolean useSSL) throws APIException {
        return delete(location, useSSL, null);
    }    

    /**
     * Performs a DELETE request to the given location (which is appended to the base URL) 
     * and returns the result as a JSON object.
     * 
     * @param location Location.
     * @param useSSL   Use SSL when available.
     * @param headers  Headers.
     * 
     * @return Object.
     */
    public JSONObject delete(String location, boolean useSSL, Header[] headers) throws APIException {
        HttpDelete httpDelete = new HttpDelete(_getURL(location, useSSL));

        if (headers != null) {
            for (Header header : headers) {
                httpDelete.addHeader(header);
            }
        }

        if (_isLoggingEnabled()) {
            Log.d(_getLoggingTag(), "Delete: " + httpDelete.getURI());
        }
        
        return _executeJSONObjectAPIRequest(httpDelete);
    }

    /**
     * Executes an API request that has been fully configured with an expected JSONObject response.
     *
     * Handles response processing and error handling in a uniform way.
     */
    private JSONObject _executeJSONObjectAPIRequest(HttpRequestBase httpRequest) throws APIException {
        return (JSONObject)_executeAPIRequest(ResponseType.JSONObject, httpRequest);
    }

    /**
     * Executes an API request that has been fully configured with an expected JSONArray response.
     *
     * Handles response processing and error handling in a uniform way.
     */
    private JSONArray _executeJSONArrayAPIRequest(HttpRequestBase httpRequest) throws APIException {
        return (JSONArray)_executeAPIRequest(ResponseType.JSONArray, httpRequest);
    }

    /**
     * Executes an API request that has been fully configured. An expected response type must be specified.
     *
     * Handles response processing and error handling in a uniform way.
     */
    protected Object _executeAPIRequest(ResponseType responseType, HttpRequestBase httpRequest) throws APIException {
        try {
            httpRequest.setParams(_getDefaultParams());
            
            HttpClient client = _getClient();
            HttpResponse response = null;
            
            try {
                response = client.execute(httpRequest);
            } catch (ConnectException ex) {
                // close idle connections to make sure the connect exception isn't
                // caused by a stuck connection and try again
                client.getConnectionManager().closeIdleConnections(0, TimeUnit.MILLISECONDS);
                response = client.execute(httpRequest);
            }
            
            String responseBody = _getResponseBody(response);

            if (_isLoggingEnabled()) {
                Log.v(_getLoggingTag(), "Response body: " + responseBody);
            }
            
            if (response.getStatusLine().getStatusCode() >= 400) {
                JSONObject object = new JSONObject(responseBody);
                throw new APIException(object.getString("code"), object.getString("message"));
            } else {
                if (responseBody == null || responseBody.trim().length() == 0) {
                    return null;
                }

                switch (responseType) {
                    case JSONArray:
                        return new JSONArray(responseBody);
    
                    case JSONObject:
                        return new JSONObject(responseBody);
    
                    default:
                        return null;
                }
            }
        } catch (APIException e) {
            // Re-throw APIExceptions.
            throw e;
        } catch (Exception e) {
            if (_isLoggingEnabled()) {
                Log.e(_getLoggingTag(), "Unexpected error", e);
            }
            
            throw new APIException(e);
        }
    }  
}
