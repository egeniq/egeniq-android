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
        this("unknown", "Unknown error", 0, parent);
    }    

    /**
     * Constructs an unknown API exception.
     */
    public APIException(int responseCode, Throwable parent) {
        this("unknown", "Unknown error", responseCode, parent);
    }    
    
    /**
     * Constructs an API exception with the given code and message.
     * 
     * @param code    Error code.
     * @param message Error message.
     */
    public APIException(String code, String message) {
        this(code, message, 0, null);
    }    
    
    /**
     * Constructs an API exception with the given code and message.
     * 
     * @param code    Error code.
     * @param message Error message.
     */
    public APIException(String code, String message, int responseCode) {
        this(code, message, responseCode, null);
    }
    
    /**
     * Constructs an API exception with the given code and message and responseCode.
     * @param code          Error code.
     * @param message       Error message.
     * @param responseCode  HTTPResponse code.
     * @param parent        Throwable parent.
     */
    public APIException(String code, String message, int responseCode, Throwable parent) {
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
