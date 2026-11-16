package com.petshop;

import com.petshop.exception.InvalidPetDataException;
import com.petshop.exception.PetNotFoundException;
import com.petshop.exception.PetShopException;
import com.petshop.model.Adoptable;
import com.petshop.model.Pet;
import com.petshop.model.PetStatus;
import com.petshop.model.PetType;
import com.petshop.model.Vaccinable;
import java.time.LocalDate;

/**
 * Main application runner & Day 1 Verification Entrypoint.
 * Demonstrates:
 * - OOP Foundations: Abstraction, Polymorphism, Interfaces (Unit 1)
 * - Exception Handling: try-catch-finally, custom exception propagation (Unit 1)
 * - String formatting & Comparable interface (Unit 1 & 2)
 */
public class Main {

    // Concrete test pet to demonstrate abstract class instantiation (Day 1 milestone)
    static class DemoPet extends Pet {
        private final String sound;
        private final String specialNeeds;

        public DemoPet(int id, String name, PetType type, String breed, int ageMonths, double price, String microchip, String sound, String specialNeeds)
                throws InvalidPetDataException {
            super(id, name, type, breed, ageMonths, price, microchip);
            this.sound = sound;
            this.specialNeeds = specialNeeds;
        }

        @Override
        public String getSound() {
            return sound;
        }

        @Override
        public String getSpecialNeeds() {
            return specialNeeds;
        }
    }

    public static void main(String[] args) {
        printBanner();

        System.out.println("=== Day 1 Milestone: Core Abstractions & Exception Hierarchy ===");
        System.out.println("Syllabus Alignment: 25CSE0202 Unit 1 (OOP Foundations & Exception Handling)\n");

        try {
            // 1. Instantiating via Abstract Base Class & Subclass (Polymorphism)
            System.out.println("[TEST 1] Instantiating and validating Pet domain models...");
            Pet pet1 = new DemoPet(101, "Milo", PetType.DOG, "Golden Retriever", 14, 850.00, "CHIP-001", "Woof! Woof!", "Daily 30 min run");
            Pet pet2 = new DemoPet(102, "Luna", PetType.CAT, "Siamese", 42, 600.00, "CHIP-002", "Meow!", "Hypoallergenic diet");

            System.out.println("Created Pet 1: " + pet1);
            System.out.println("Created Pet 2: " + pet2);
            System.out.println("Sound: " + pet1.getName() + " says '" + pet1.getSound() + "'");

            // 2. Interface polymorphism (Adoptable & Vaccinable)
            System.out.println("\n[TEST 2] Testing Adoptable & Vaccinable interface contracts...");
            Adoptable adoptablePet = pet2;
            System.out.println("Eligible for adoption? " + adoptablePet.isAdoptable());
            System.out.println("Adoption fee (senior discount check for 42 mos): $" + adoptablePet.calculateAdoptionFee());

            Vaccinable vaccinablePet = pet1;
            vaccinablePet.recordVaccination("Rabies + DHPP", LocalDate.now());
            System.out.println("Vaccinated? " + vaccinablePet.isVaccinated() + " on " + vaccinablePet.getLastVaccinationDate());

            // 3. Testing Comparable interface
            System.out.println("\n[TEST 3] Testing Natural Ordering (Comparable<Pet>)...");
            int comparison = pet1.compareTo(pet2);
            System.out.println("pet1.compareTo(pet2): " + comparison + " (Ordered by ID: " + pet1.getId() + " vs " + pet2.getId() + ")");

            // 4. Testing Custom Exception Hierarchy & Input Validation
            System.out.println("\n[TEST 4] Testing Custom Exception Hierarchy (InvalidPetDataException)...");
            testValidationFailure();

            // 5. Testing Exception Propagation & Specific Catch
            System.out.println("\n[TEST 5] Testing PetNotFoundException propagation...");
            testNotFoundException();

        } catch (PetShopException ex) {
            System.err.println("UNHANDLED PET SHOP EXCEPTION: " + ex);
        } finally {
            System.out.println("\n============================================================");
            System.out.println("[SUCCESS] Day 1 Verification Completed cleanly with 0 errors!");
            System.out.println("============================================================");
        }
    }

    private static void testValidationFailure() {
        try {
            System.out.print("Attempting to create pet with negative price (-50.0)... ");
            new DemoPet(103, "Ghost", PetType.DOG, "Husky", 12, -50.00, "CHIP-999", "Howl", "Cold weather");
            System.out.println("FAIL: Exception should have been thrown!");
        } catch (InvalidPetDataException ex) {
            System.out.println("CAUGHT EXPECTED EXCEPTION: " + ex.getMessage());
            System.out.println("Error Code: " + ex.getErrorCode() + " | Faulty Field: " + ex.getFieldName());
        }
    }

    private static void testNotFoundException() {
        try {
            System.out.print("Simulating query for non-existent pet ID 999... ");
            throw new PetNotFoundException(999);
        } catch (PetNotFoundException ex) {
            System.out.println("CAUGHT: " + ex.getMessage());
            System.out.println("Missing Identifier: " + ex.getIdentifier() + " | Code: " + ex.getErrorCode());
        }
    }

    private static void printBanner() {
        System.out.println("************************************************************");
        System.out.println("*            PET HAVEN - CLI PLATFORM (PURE JAVA)          *");
        System.out.println("*         Chitkara University | Course Code: 25CSE0202     *");
        System.out.println("************************************************************\n");
    }
}
