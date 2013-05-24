package com.egeniq.utils.api;

/**
 * API exception.
 */
public class APIException extends Exception {
    /**
     * Unique identifier. 
     */
    private static final long serialVersionUID = 6047699912655048829L;
    
    /**
     * Error code.
     */
    private final String _code;

    /**
     * HTTPResponse code
     */
    private final int _responseCode;
    
    /**
     * Constructs an unknown API exception.
     */
    public APIException() {
        this(null);
    }
    
    /**
     * Constructs an unknown API exception.
     */
    public APIException(Throwable parent) {
        this(parent, 0);
    }    
    
    /**
     * construct an unknown API Exception.
     */
    public APIException(Throwable parent, int responseCode) {
        this("unknown", "Unknown error", parent, responseCode);
    }
    
    /**
     * Constructs an API exception with the given code and message.
     * 
     * @param code    Error code.
     * @param message Error message.
     */
    public APIException(String code, String message) {
        this(code, message, null);
    }    
    
    /**
     * Constructs an API exception with the given code and message.
     * 
     * @param code    Error code.
     * @param message Error message.
     */
    public APIException(String code, String message, Throwable parent) {
        this(code, message, parent, 0);
    }
    
    /**
     * Constructs an API exception with the given code and message and responseCode.
     * @param code          Error code.
     * @param message       Error message.
     * @param parent        Throwable parent.
     * @param responseCode  HTTPResponse code.
     */
    public APIException(String code, String message, Throwable parent, int responseCode) {
        super(message, parent);
        _code = code;
        _responseCode = responseCode;
    }

    /**
     * Returns the error code.
     * 
     * @return Error code.
     */
    public String getCode() {
        return _code;
    }   
    
    /**
     * Returns the http response code
     * 
     * @return Response code
     */
    public int getResponseCode() {
        return _responseCode;
    }
}
