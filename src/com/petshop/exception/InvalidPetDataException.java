package com.petshop.exception;

/**
 * Thrown when data validation fails on pet attributes (e.g., negative price, negative age, blank name).
 */
public class InvalidPetDataException extends PetShopException {
    private final String fieldName;

    public InvalidPetDataException(String fieldName, String reason) {
        super("Validation failed for field '" + fieldName + "': " + reason, "ERR_INVALID_DATA");
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
