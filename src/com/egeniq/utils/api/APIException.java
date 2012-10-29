package com.egeniq.utils.api;


/**
 * API exception.
 */
public class APIException extends HTTPException {
    /**
     * Unique identifier.
     */
    private final static long serialVersionUID = -882638141537688701L;

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
        super(code, message, parent);
    }
    
}
