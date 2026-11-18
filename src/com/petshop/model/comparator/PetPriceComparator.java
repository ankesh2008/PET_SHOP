package com.petshop.model.comparator;

import com.petshop.model.Pet;
import java.util.Comparator;

/**
 * Comparator implementation to sort pets by price.
 * Supports ascending or descending order (Syllabus Unit 2: Comparator interface).
 */
public class PetPriceComparator implements Comparator<Pet> {
    private final boolean ascending;

    public PetPriceComparator() {
        this(true);
    }

    public PetPriceComparator(boolean ascending) {
        this.ascending = ascending;
    }

    @Override
    public int compare(Pet p1, Pet p2) {
        if (p1 == null && p2 == null) return 0;
        if (p1 == null) return ascending ? -1 : 1;
        if (p2 == null) return ascending ? 1 : -1;

        int priceComp = Double.compare(p1.getPrice(), p2.getPrice());
        if (priceComp != 0) {
            return ascending ? priceComp : -priceComp;
        }
        // Secondary sort by ID for deterministic ordering
        return Integer.compare(p1.getId(), p2.getId());
    }
}
