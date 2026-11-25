package com.petshop.model;

import com.petshop.exception.InvalidPetDataException;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Abstract base class for all animals in the Pet Shop.
 * Integrates:
 * - OOP Abstraction, Encapsulation, Inheritance base (Unit 1)
 * - Comparable interface implementation (Unit 2)
 * - Multiple interface contracts: Adoptable, Vaccinable (Unit 1)
 * - java.time integration (Unit 5)
 */
public abstract class Pet implements Comparable<Pet>, Adoptable, Vaccinable {

    // Static counter illustrating class-level state (Unit 1: static keyword)
    private static int nextIdSequence = 1001;

    // Encapsulated member variables
    private final int id;
    private String name;
    private final PetType type;
    private String breed;
    private int ageMonths;
    private double price;
    private PetStatus status;
    private String microchipId;

    // Health and adoption tracking
    private String lastVaccineName;
    private LocalDate lastVaccinationDate;
    private String adoptedBy;

    /**
     * Full parameter constructor with defensive data validation.
     * @throws InvalidPetDataException when input constraints are violated.
     */
    public Pet(int id, String name, PetType type, String breed, int ageMonths, double price, String microchipId)
            throws InvalidPetDataException {
        validateString("name", name);
        validateString("breed", breed);
        validateString("microchipId", microchipId);

        if (ageMonths < 0) {
            throw new InvalidPetDataException("ageMonths", "Age cannot be negative (" + ageMonths + ")");
        }
        if (price < 0.0) {
            throw new InvalidPetDataException("price", "Price cannot be negative (" + price + ")");
        }
        Objects.requireNonNull(type, "PetType cannot be null");

        this.id = (id > 0) ? id : nextIdSequence++;
        this.name = name.trim();
        this.type = type;
        this.breed = breed.trim();
        this.ageMonths = ageMonths;
        this.price = price;
        this.status = PetStatus.AVAILABLE;
        this.microchipId = microchipId.trim().toUpperCase();
        this.lastVaccineName = null;
        this.lastVaccinationDate = null;
        this.adoptedBy = null;
    }

    /**
     * Overloaded constructor auto-generating ID from static sequence.
     */
    public Pet(String name, PetType type, String breed, int ageMonths, double price, String microchipId)
            throws InvalidPetDataException {
        this(nextIdSequence++, name, type, breed, ageMonths, price, microchipId);
    }

    // Static helper method
    public static int peekNextId() {
        return nextIdSequence;
    }

    // Abstract methods to be overridden by concrete pet subclasses (Unit 1: Abstraction & Polymorphism)
    public abstract String getSound();
    public abstract String getSpecialNeeds();

    // --- Adoptable Interface Implementation ---
    @Override
    public boolean isAdoptable() {
        return this.status == PetStatus.AVAILABLE;
    }

    @Override
    public void markAdopted(String adopterName) {
        this.status = PetStatus.ADOPTED;
        this.adoptedBy = adopterName;
    }

    @Override
    public double calculateAdoptionFee() {
        // Base adoption fee: pets over 36 months receive a 15% senior pet discount
        if (this.ageMonths >= 36) {
            return Math.round(this.price * 0.85 * 100.0) / 100.0;
        }
        return this.price;
    }

    // --- Vaccinable Interface Implementation ---
    @Override
    public boolean isVaccinated() {
        return this.lastVaccinationDate != null;
    }

    @Override
    public void recordVaccination(String vaccineName, LocalDate date) {
        this.lastVaccineName = vaccineName;
        this.lastVaccinationDate = (date != null) ? date : LocalDate.now();
    }

    @Override
    public LocalDate getLastVaccinationDate() {
        return this.lastVaccinationDate;
    }

    // --- Comparable Interface Implementation (Unit 2: Sorting) ---
    @Override
    public int compareTo(Pet other) {
        if (other == null) return 1;
        // Natural ordering by ID
        return Integer.compare(this.id, other.id);
    }

    // --- Validation Helper ---
    private static void validateString(String field, String val) throws InvalidPetDataException {
        if (val == null || val.trim().isEmpty()) {
            throw new InvalidPetDataException(field, "Field cannot be null or empty.");
        }
    }

    // --- Getters & Setters with encapsulation ---
    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) throws InvalidPetDataException {
        validateString("name", name);
        this.name = name.trim();
    }
    public PetType getType() { return type; }
    public String getBreed() { return breed; }
    public int getAgeMonths() { return ageMonths; }
    public void setAgeMonths(int ageMonths) throws InvalidPetDataException {
        if (ageMonths < 0) throw new InvalidPetDataException("ageMonths", "Age cannot be negative.");
        this.ageMonths = ageMonths;
    }
    public double getPrice() { return price; }
    public void setPrice(double price) throws InvalidPetDataException {
        if (price < 0.0) throw new InvalidPetDataException("price", "Price cannot be negative.");
        this.price = price;
    }
    public PetStatus getStatus() { return status; }
    public void setStatus(PetStatus status) {
        this.status = Objects.requireNonNull(status, "Status cannot be null");
    }
    public String getMicrochipId() { return microchipId; }
    public String getLastVaccineName() { return lastVaccineName; }
    public String getAdoptedBy() { return adoptedBy; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pet pet)) return false;
        return id == pet.id && Objects.equals(microchipId, pet.microchipId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, microchipId);
    }

    @Override
    public String toString() {
        return String.format("[%d] %-12s | %-6s | %-15s | %2d mos | $%7.2f | %-10s | Chip: %s",
                id, name, type.getDisplayName(), breed, ageMonths, price, status, microchipId);
    }
}
