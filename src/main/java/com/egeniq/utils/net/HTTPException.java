package com.egeniq.utils.net;

/**
 * HTTP exception.
 */
public class HTTPException extends Exception {
    /**
     * Unique ID.
     */
    private static final long serialVersionUID = -6148048533520350702L;
    
    /**
     * Error code.
     */
    private final int _code;

    /**
     * Constructs an unknown HTTP exception.
     */
    public HTTPException() {
        this(null);
    }

    /**
     * Constructs an unknown HTTP exception.
     */
    public HTTPException(Throwable parent) {
        this(500, "Unknown error", parent);
    }

    /**
     * Constructs an HTTP exception with the given code and message.
     * 
     * @param code
     *            Error code.
     * @param message
     *            Error message.
     */
    public HTTPException(int code, String message) {
        this(code, message, null);
    }

    /**
     * Constructs an HTTP exception with the given code and message.
     * 
     * @param code Error code.
     * @param message Error message.
     */
    public HTTPException(int code, String message, Throwable parent) {
        super(message, parent);
        _code = code;
    }

    /**
     * Returns the error code.
     * 
     * @return Error code.
     */
    public int getCode() {
        return _code;
    }
}
