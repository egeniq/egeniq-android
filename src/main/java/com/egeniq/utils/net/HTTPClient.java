package com.egeniq.utils.net;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;


/**
 * Simple HTTP client.
 */
public class HTTPClient extends AbstractHTTPClient {
    /**
     * Constructor.
     */
    public HTTPClient(String baseURL) {
        super(baseURL);
    }

    /**
     * Constructor.
     */    
    public HTTPClient(String baseURL, String secureBaseURL) {
        super(baseURL, secureBaseURL);
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
    public String get(String location) throws HTTPException {
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
    public String get(String location, boolean useSSL) throws HTTPException {
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
    public String get(String location, boolean useSSL, Header[] headers) throws HTTPException {
        HttpGet httpGet = new HttpGet(_getURL(location, useSSL));

        if (headers != null) {
            for (Header header : headers) {
                httpGet.addHeader(header);
            }
        }          
        
        if (_isLoggingEnabled()) {
            Log.d(_getLoggingTag(), "Fetch JSON object: " + httpGet.getURI());
        }
        
        return _executeAPIRequest(httpGet);
    }
    
    /**
     * Performs a POST request to the given location (which is appended to the base URL) 
     * and returns the result as a JSON object..
     * 
     * @param location Location.
     * @param entity   Post entity.
     */
    public String post(String location, HttpEntity entity) throws HTTPException {
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
    public String post(String location, HttpEntity entity, boolean useSSL) throws HTTPException {
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
    public String post(String location, HttpEntity entity, boolean useSSL, Header[] headers) throws HTTPException {
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
        
        return _executeAPIRequest(httpPost);
    }
    
    /**
     * Performs a DELETE request to the given location (which is appended to the base URL) 
     * and returns the result as a JSON object.
     * 
     * @param location Location.
     * 
     * @return Object.
     */
    public String delete(String location) throws HTTPException {
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
    public String delete(String location, boolean useSSL) throws HTTPException {
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
    public String delete(String location, boolean useSSL, Header[] headers) throws HTTPException {
        HttpDelete httpDelete = new HttpDelete(_getURL(location, useSSL));

        if (headers != null) {
            for (Header header : headers) {
                httpDelete.addHeader(header);
            }
        }

        if (_isLoggingEnabled()) {
            Log.d(_getLoggingTag(), "Delete: " + httpDelete.getURI());
        }
        
        return _executeAPIRequest(httpDelete);
    }

    /**
     * Executes an API request that has been fully configured. An expected response type must be specified.
     *
     * Handles response processing and error handling in a uniform way.
     */
    protected String _executeAPIRequest(HttpRequestBase httpRequest) throws HTTPException {
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
                throw new HTTPException(response.getStatusLine().getStatusCode(), responseBody);
            } else {
                if (responseBody == null || responseBody.trim().length() == 0) {
                    return null;
                } else {
                    return responseBody;
                }
            }
        } catch (HTTPException e) {
            // Re-throw HTTPExceptions.
            throw e;
        } catch (Exception e) {
            if (_isLoggingEnabled()) {
                Log.e(_getLoggingTag(), "Unexpected error", e);
            }
            
            throw new HTTPException(e);
        }
    }  
}
