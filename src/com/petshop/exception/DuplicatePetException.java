package com.petshop.exception;

/**
 * Thrown when attempting to register a pet with a duplicate ID or unique microchip identifier.
 */
public class DuplicatePetException extends PetShopException {
    private final String conflictValue;

    public DuplicatePetException(String fieldName, String value) {
        super("A pet with " + fieldName + " '" + value + "' already exists in inventory.", "ERR_DUPLICATE_PET");
        this.conflictValue = value;
    }

    public String getConflictValue() {
        return conflictValue;
    }
}
