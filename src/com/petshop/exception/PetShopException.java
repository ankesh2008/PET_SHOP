package com.petshop.exception;

/**
 * Root checked exception for all business domain errors in the Pet Shop application.
 * Demonstrates exception hierarchy, constructor overloading, and super calls (Syllabus Unit 1).
 */
public class PetShopException extends Exception {
    private final String errorCode;

    public PetShopException(String message) {
        super(message);
        this.errorCode = "ERR_GENERIC";
    }

    public PetShopException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public PetShopException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "ERR_INTERNAL";
    }

    public PetShopException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "[" + errorCode + "] " + getMessage();
    }
}
