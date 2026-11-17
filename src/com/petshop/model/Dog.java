package com.petshop.model;

import com.petshop.exception.InvalidPetDataException;

/**
 * Concrete Canine pet subclass.
 * Demonstrates inheritance, constructor chaining via super(), and abstract method overriding (Syllabus Unit 1).
 */
public class Dog extends Pet {
    private boolean isTrained;
    private String breedGroup; // e.g. Herding, Toy, Working, Hound

    public Dog(int id, String name, String breed, int ageMonths, double price, String microchipId,
               boolean isTrained, String breedGroup) throws InvalidPetDataException {
        super(id, name, PetType.DOG, breed, ageMonths, price, microchipId);
        this.isTrained = isTrained;
        this.breedGroup = (breedGroup != null && !breedGroup.trim().isEmpty()) ? breedGroup.trim() : "General";
    }

    public Dog(String name, String breed, int ageMonths, double price, String microchipId,
               boolean isTrained, String breedGroup) throws InvalidPetDataException {
        super(name, PetType.DOG, breed, ageMonths, price, microchipId);
        this.isTrained = isTrained;
        this.breedGroup = (breedGroup != null && !breedGroup.trim().isEmpty()) ? breedGroup.trim() : "General";
    }

    @Override
    public String getSound() {
        return "Woof! Woof!";
    }

    @Override
    public String getSpecialNeeds() {
        return (isTrained ? "Standard 30-min daily walk" : "Requires obedience training & 45-min daily exercise")
                + " | Group: " + breedGroup;
    }

    public boolean isTrained() {
        return isTrained;
    }

    public void setTrained(boolean trained) {
        isTrained = trained;
    }

    public String getBreedGroup() {
        return breedGroup;
    }

    public void setBreedGroup(String breedGroup) {
        this.breedGroup = breedGroup;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | [Dog: %s, Trained=%b]", breedGroup, isTrained);
    }
}
