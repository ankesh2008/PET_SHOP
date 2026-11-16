package com.petshop.model;

/**
 * Interface defining adoption operations.
 * Demonstrates Java interface contracts and abstraction (Syllabus Unit 1).
 */
public interface Adoptable {
    /**
     * Checks if the entity is currently eligible for adoption.
     * @return true if adoptable, false otherwise.
     */
    boolean isAdoptable();

    /**
     * Completes the adoption lifecycle for a customer.
     * @param adopterName Name of the customer adopting this pet.
     */
    void markAdopted(String adopterName);

    /**
     * Calculates adoption fee including any health clearance or age discounts.
     * @return final calculated fee.
     */
    double calculateAdoptionFee();
}
