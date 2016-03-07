package com.egeniq.utils.net;

import android.annotation.SuppressLint;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * HTTP client.
 */
public abstract class AbstractHTTPClient {
    private final static int DEFAULT_TIMEOUT = 15000;

    private int _timeout = DEFAULT_TIMEOUT;

    private static HttpClient _httpClient = null;

    private String _loggingTag = getClass().getName();
    private boolean _loggingEnabled = false;

    private String _baseURL;
    private String _secureBaseURL;

    /**
     * Constructor.
     */
    public AbstractHTTPClient(String baseURL) {
        this(baseURL, baseURL);
    }

    /**
     * Constructor.
     */
    public AbstractHTTPClient(String baseURL, String secureBaseURL) {
        _baseURL = baseURL;
        _secureBaseURL = secureBaseURL;
    }

    /**
     * Returns the (secure?) base URL.
     *
     * @param useSSL When true returns the secure base URL, else the default base URL.
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
     * <p/>
     * Prepends the correct base URL (based on the useSSL) option unless the given location
     * itself is already a full HTTP URL.
     *
     * @param location Location.
     * @param useSSL   Use SSL?
     * @return Full URL.
     */
    @SuppressLint("DefaultLocale")
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
     * Creates a HTTP client object.
     */
    protected synchronized static HttpClient _getClient() {
        if (_httpClient == null) {
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            ClientConnectionManager mgr = defaultHttpClient.getConnectionManager();

            HttpParams params = defaultHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, DEFAULT_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, DEFAULT_TIMEOUT);

            ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(10));
            ConnManagerParams.setMaxTotalConnections(params, 100);

            _httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()), params);
            _httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        }

        return _httpClient;
    }

    /**
     * Gets the body of the HttpResponse as a String.
     * <p/>
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
        try {
            _consume(entity);
        } catch (final IOException ignore) {
        }

        return builder.toString();
    }

    /**
     * Ensures that the entity content is fully consumed and the content stream, if exists, is closed.
     * This used to be in EntityUtils, but was deprecated.
     *
     * @param entity the entity to consume.
     * @throws IOException if an error occurs reading the input stream.
     */
    private static void _consume(final HttpEntity entity) throws IOException {
        if (entity == null) {
            return;
        }
        if (entity.isStreaming()) {
            final InputStream instream = entity.getContent();
            if (instream != null) {
                instream.close();
            }
        }
    }
}
