package com.petshop.model;

/**
 * Enumeration representing the lifecycle state of a pet in the shop.
 */
public enum PetStatus {
    AVAILABLE("Available for Adoption"),
    RESERVED("Reserved / Processing"),
    ADOPTED("Adopted"),
    QUARANTINE("Medical Quarantine");

    private final String description;

    PetStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canBeAdopted() {
        return this == AVAILABLE;
    }

    @Override
    public String toString() {
        return description;
    }
}
