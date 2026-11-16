package com.petshop.model;

import java.time.LocalDate;

/**
 * Interface defining health and vaccination operations for living pets.
 * Demonstrates abstraction, interface contracts, and java.time (Syllabus Unit 1 & 5).
 */
public interface Vaccinable {
    /**
     * Checks if the pet has active, up-to-date vaccinations.
     * @return true if vaccinated.
     */
    boolean isVaccinated();

    /**
     * Records a new vaccination entry with timestamp.
     * @param vaccineName Name of the administered vaccine.
     * @param date Date of administration.
     */
    void recordVaccination(String vaccineName, LocalDate date);

    /**
     * Retrieves the most recent vaccination date.
     * @return LocalDate of last vaccination or null if not yet vaccinated.
     */
    LocalDate getLastVaccinationDate();
}
