package com.petshop.exception;

/**
 * Thrown when an adoption or purchase is requested for a pet that is already adopted,
 * reserved, or held under medical quarantine.
 */
public class InsufficientStockException extends PetShopException {
    private final int petId;
    private final String currentStatus;

    public InsufficientStockException(int petId, String currentStatus) {
        super("Pet #" + petId + " cannot be adopted. Current status: " + currentStatus + ".", "ERR_INSUFFICIENT_STOCK");
        this.petId = petId;
        this.currentStatus = currentStatus;
    }

    public int getPetId() {
        return petId;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }
}
