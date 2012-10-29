package com.egeniq.utils.net;

/**
 * API exception.
 */
public class HTTPException extends Exception {

    /**
     * Unique identifier.
     */
    private static final long serialVersionUID = -4389897353157210848L;

    /**
     * Error code.
     */
    private String _code;

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
        this("unknown", "Unknown error", parent);
    }

    /**
     * Constructs an HTTP exception with the given code and message.
     * 
     * @param code
     *            Error code.
     * @param message
     *            Error message.
     */
    public HTTPException(String code, String message) {
        this(code, message, null);
    }

    /**
     * Constructs an HTTP exception with the given code and message.
     * 
     * @param code
     *            Error code.
     * @param message
     *            Error message.
     */
    public HTTPException(String code, String message, Throwable parent) {
        super(message, parent);
        _code = code;
    }

    /**
     * Returns the error code.
     * 
     * @return String err
     */
    public String getCode() {
        return _code;
    }
}
