package com.petshop.model;

import com.petshop.exception.InvalidPetDataException;

/**
 * Concrete Avian pet subclass.
 */
public class Bird extends Pet {
    private double wingspanCm;
    private boolean canTalk;

    public Bird(int id, String name, String breed, int ageMonths, double price, String microchipId,
                double wingspanCm, boolean canTalk) throws InvalidPetDataException {
        super(id, name, PetType.BIRD, breed, ageMonths, price, microchipId);
        if (wingspanCm < 0) {
            throw new InvalidPetDataException("wingspanCm", "Wingspan cannot be negative (" + wingspanCm + ")");
        }
        this.wingspanCm = wingspanCm;
        this.canTalk = canTalk;
    }

    public Bird(String name, String breed, int ageMonths, double price, String microchipId,
                double wingspanCm, boolean canTalk) throws InvalidPetDataException {
        super(name, PetType.BIRD, breed, ageMonths, price, microchipId);
        if (wingspanCm < 0) {
            throw new InvalidPetDataException("wingspanCm", "Wingspan cannot be negative (" + wingspanCm + ")");
        }
        this.wingspanCm = wingspanCm;
        this.canTalk = canTalk;
    }

    @Override
    public String getSound() {
        return canTalk ? "Hello! Chirp! Pretty bird!" : "Chirp! Tweet! Tweet!";
    }

    @Override
    public String getSpecialNeeds() {
        return String.format("Flight cage (min width %.1f cm) + calcium supplements", wingspanCm * 2.5);
    }

    public double getWingspanCm() {
        return wingspanCm;
    }

    public void setWingspanCm(double wingspanCm) throws InvalidPetDataException {
        if (wingspanCm < 0) throw new InvalidPetDataException("wingspanCm", "Wingspan cannot be negative.");
        this.wingspanCm = wingspanCm;
    }

    public boolean canTalk() {
        return canTalk;
    }

    public void setCanTalk(boolean canTalk) {
        this.canTalk = canTalk;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | [Bird: Wing=%.1fcm, Talks=%b]", wingspanCm, canTalk);
    }
}
