package com.petshop.model.comparator;

import com.petshop.model.Pet;
import java.util.Comparator;

/**
 * Comparator implementation to sort pets by age in months (Syllabus Unit 2).
 */
public class PetAgeComparator implements Comparator<Pet> {
    @Override
    public int compare(Pet p1, Pet p2) {
        if (p1 == null && p2 == null) return 0;
        if (p1 == null) return -1;
        if (p2 == null) return 1;

        int ageComp = Integer.compare(p1.getAgeMonths(), p2.getAgeMonths());
        if (ageComp != 0) return ageComp;
        return Integer.compare(p1.getId(), p2.getId());
    }
}
