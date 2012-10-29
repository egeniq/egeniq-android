package com.egeniq.utils.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import android.util.Log;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.HttpVersion;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.impl.conn.tsccm.ThreadSafeClientConnManager;
import ch.boye.httpclientandroidlib.params.BasicHttpParams;
import ch.boye.httpclientandroidlib.params.CoreProtocolPNames;
import ch.boye.httpclientandroidlib.params.HttpConnectionParams;
import ch.boye.httpclientandroidlib.params.HttpParams;
import ch.boye.httpclientandroidlib.util.EntityUtils;

/**
 * HTTP client.
 */
public class HTTPClient {
    private final static int DEFAULT_TIMEOUT = 15000;
    
    private int _timeout = DEFAULT_TIMEOUT;

    private static HttpClient _httpClient = null;

    private String _loggingTag = getClass().getName();
    private boolean _loggingEnabled = false;
    
    /**
     * Is logging enabled?
     */
    protected boolean _isLoggingEnabled() {
        return _loggingEnabled;
    }
    
    /**
     * Enable / disable logging.
     */
    protected void _setLoggingEnabled(boolean loggingEnabled) {
        _loggingEnabled = loggingEnabled;
    }    
    
    /**
     * Returns the logging tag.
     */
    protected String _getLoggingTag() {
        return _loggingTag;
    }
    
    /**
     * Sets the logging tag.
     */
    protected void _setLoggingTag(String tag) {
        _loggingTag = tag;
    }
    
    /**
     * Returns the default http request parameters.
     */
    protected HttpParams _getDefaultParams() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, _timeout);
        HttpConnectionParams.setSoTimeout(params, _timeout);
        HttpConnectionParams.setSoKeepalive(params, true);
        return params;
    }
    
    /**
     * Get the currently set timeout.
     */
    public int getTimeout() {
        return _timeout;
    }

    /**
     * Set a new timeout value
     */
    public void setTimeout(int timeout) {
        _timeout = timeout;
    }    
    
    /**
     * Performs a GET request to the given location (which is appended to the base URL) and 
     * returns the result as a string.
     */
    public String fetch(String location) throws HTTPException {
        HttpGet httpGet = new HttpGet(location);
        httpGet.setParams(_getDefaultParams());

        if (_isLoggingEnabled()) {
            Log.d(_getLoggingTag(), "Fetch response: " + httpGet.getURI());
        }

        HttpClient client = _getClient();

        HttpResponse response;
        String responseBody;

        try {
            response = client.execute(httpGet);
            responseBody = _getResponseBody(response);
            
            if (_isLoggingEnabled()) {
                Log.v(_getLoggingTag(), "Response body (" + httpGet.getURI() + "): " + responseBody);
            }
        } catch (ClientProtocolException e) {
            throw new HTTPException();
        } catch (IOException e) {
            throw new HTTPException();
        }

        return responseBody;
    }

    /**
     * Creates a HTTP client object.
     */
    protected synchronized static HttpClient _getClient() {
        if (_httpClient == null) {
            ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager();
            connectionManager.setDefaultMaxPerRoute(10);
            connectionManager.setMaxTotal(100);
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, DEFAULT_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, DEFAULT_TIMEOUT);
            HttpConnectionParams.setSoKeepalive(params, true);
            _httpClient = new DefaultHttpClient(connectionManager, params);
            _httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        }
        
        return _httpClient;
    }

    /**
     * Gets the body of the HttpResponse as a String.
     * 
     * Returns null if response is null or if the body is empty.
     * 
     * @throws IOException
     * @throws IllegalStateException
     */
    protected String _getResponseBody(HttpResponse response) throws IllegalStateException, IOException {
        if (response == null) {
            return null;
        }

        HttpEntity entity = response.getEntity();

        if (entity == null) {
            return null;
        }

        InputStream content = entity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(content, Charset.forName("UTF-8")), 8192);
        StringBuilder builder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        reader.close();
        EntityUtils.consumeQuietly(entity);
        
        return builder.toString();
    }
}
