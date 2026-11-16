package com.petshop.model;

/**
 * Enumeration representing different biological types/species of pets.
 * Aligned with syllabus: Enums, switch-expressions, encapsulation.
 */
public enum PetType {
    DOG("Dog", "Canine"),
    CAT("Cat", "Feline"),
    BIRD("Bird", "Avian"),
    FISH("Fish", "Aquatic"),
    RABBIT("Rabbit", "Lagomorph");

    private final String displayName;
    private final String family;

    PetType(String displayName, String family) {
        this.displayName = displayName;
        this.family = family;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFamily() {
        return family;
    }

    @Override
    public String toString() {
        return displayName + " (" + family + ")";
    }
}
