package com.petshop.exception;

/**
 * Thrown when a pet lookup by ID, microchip, or criteria fails to find a match.
 */
public class PetNotFoundException extends PetShopException {
    private final String identifier;

    public PetNotFoundException(int petId) {
        super("Pet with ID #" + petId + " was not found in the inventory.", "ERR_PET_NOT_FOUND");
        this.identifier = String.valueOf(petId);
    }

    public PetNotFoundException(String criteria) {
        super("No pet matching criteria '" + criteria + "' was found.", "ERR_PET_NOT_FOUND");
        this.identifier = criteria;
    }

    public String getIdentifier() {
        return identifier;
    }
}
