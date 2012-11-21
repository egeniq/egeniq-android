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
     * Constructs an unknown API exception.
     */
    public APIException() {
        this(null);
    }
    
    /**
     * Constructs an unknown API exception.
     */
    public APIException(Throwable parent) {
        this("unknown", "Unknown error", parent);
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
        super(message, parent);
        _code = code;
    }

    /**
     * Returns the error code.
     * 
     * @return Error code.
     */
    public String getCode() {
        return _code;
    }   
}
