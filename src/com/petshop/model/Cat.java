package com.petshop.model;

import com.petshop.exception.InvalidPetDataException;

/**
 * Concrete Feline pet subclass.
 * Demonstrates polymorphism and domain-specific property encapsulation (Syllabus Unit 1).
 */
public class Cat extends Pet {
    private boolean isIndoor;
    private boolean isDeclawed;

    public Cat(int id, String name, String breed, int ageMonths, double price, String microchipId,
               boolean isIndoor, boolean isDeclawed) throws InvalidPetDataException {
        super(id, name, PetType.CAT, breed, ageMonths, price, microchipId);
        this.isIndoor = isIndoor;
        this.isDeclawed = isDeclawed;
    }

    public Cat(String name, String breed, int ageMonths, double price, String microchipId,
               boolean isIndoor, boolean isDeclawed) throws InvalidPetDataException {
        super(name, PetType.CAT, breed, ageMonths, price, microchipId);
        this.isIndoor = isIndoor;
        this.isDeclawed = isDeclawed;
    }

    @Override
    public String getSound() {
        return "Meow! Purr...";
    }

    @Override
    public String getSpecialNeeds() {
        return (isIndoor ? "Indoor cat: Scratching post & sunny perch" : "Access to secure outdoor enclosure")
                + (isDeclawed ? " (Declawed - gentle litter only)" : " (Claws intact - regular trimming)");
    }

    public boolean isIndoor() {
        return isIndoor;
    }

    public void setIndoor(boolean indoor) {
        isIndoor = indoor;
    }

    public boolean isDeclawed() {
        return isDeclawed;
    }

    public void setDeclawed(boolean declawed) {
        isDeclawed = declawed;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | [Cat: Indoor=%b, Declawed=%b]", isIndoor, isDeclawed);
    }
}
