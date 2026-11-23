package com.petshop.service;

import com.petshop.model.Pet;
import com.petshop.model.PetStatus;
import com.petshop.model.PetType;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Functional Programming & Stream Analytics Engine (Syllabus Unit 5).
 * Implements:
 * - Functional Interfaces: Predicate, Function, Consumer
 * - Stream pipelines: filter, map, sorted, distinct, limit, reduce
 * - Collectors: groupingBy, partitioningBy, summarizingDouble
 * - Performance comparison: Stream vs Conventional Imperative Loop
 */
public class AnalyticsService {
    private final PetService petService;

    public AnalyticsService(PetService petService) {
        this.petService = petService;
    }

    /**
     * Filters pets using a dynamic Predicate functional interface.
     */
    public List<Pet> filterPets(Predicate<Pet> criteria) {
        return petService.getAllPets().stream()
                .filter(criteria)
                .toList();
    }

    /**
     * Groups pet count by species using Collectors.groupingBy().
     */
    public Map<PetType, Long> getPetCountByType() {
        return petService.getAllPets().stream()
                .collect(Collectors.groupingBy(Pet::getType, Collectors.counting()));
    }

    /**
     * Partitions inventory into Adoptable vs Non-Adoptable using Collectors.partitioningBy().
     */
    public Map<Boolean, List<Pet>> partitionByAdoptability() {
        return petService.getAllPets().stream()
                .collect(Collectors.partitioningBy(Pet::isAdoptable));
    }

    /**
     * Computes full inventory valuation and price summary statistics using Collectors.summarizingDouble().
     */
    public DoubleSummaryStatistics getPriceStatistics() {
        return petService.getAllPets().stream()
                .collect(Collectors.summarizingDouble(Pet::getPrice));
    }

    /**
     * Retrieves top N highest priced pets using stream intermediate operations: sorted, limit.
     */
    public List<Pet> getTopPricedPets(int limit) {
        return petService.getAllPets().stream()
                .sorted(Comparator.comparingDouble(Pet::getPrice).reversed())
                .limit(limit)
                .toList();
    }

    /**
     * Compares Stream filter+sum vs Conventional for-loop (Syllabus Lab 52-55 requirement).
     */
    public void compareStreamWithConventionalLoop() {
        List<Pet> pets = petService.getAllPets();

        // 1. Stream pipeline calculation
        long startStream = System.nanoTime();
        double streamTotal = pets.stream()
                .filter(p -> p.getStatus() == PetStatus.AVAILABLE)
                .mapToDouble(Pet::getPrice)
                .sum();
        long durationStream = System.nanoTime() - startStream;

        // 2. Conventional imperative loop
        long startLoop = System.nanoTime();
        double loopTotal = 0.0;
        for (Pet p : pets) {
            if (p.getStatus() == PetStatus.AVAILABLE) {
                loopTotal += p.getPrice();
            }
        }
        long durationLoop = System.nanoTime() - startLoop;

        System.out.printf("%n=== Stream vs Conventional Loop Verification ===%n");
        System.out.printf("  Stream API Result     : $%.2f (Evaluated in %d ns)%n", streamTotal, durationStream);
        System.out.printf("  Conventional Loop     : $%.2f (Evaluated in %d ns)%n", loopTotal, durationLoop);
        System.out.printf("  Correctness Matches   : %b%n", Math.abs(streamTotal - loopTotal) < 0.001);
    }

    /**
     * Formats and prints comprehensive analytical report to console.
     */
    public void printAnalyticsReport() {
        System.out.println("\n==============================================================");
        System.out.println("              PET HAVEN - INVENTORY ANALYTICS REPORT          ");
        System.out.println("==============================================================");

        // 1. Summary Statistics
        DoubleSummaryStatistics stats = getPriceStatistics();
        System.out.printf("Total Registered Pets   : %d%n", stats.getCount());
        System.out.printf("Total Inventory Value   : $%.2f%n", stats.getSum());
        System.out.printf("Average Pet Price       : $%.2f%n", stats.getAverage());
        System.out.printf("Price Range (Min - Max) : $%.2f - $%.2f%n", stats.getMin(), stats.getMax());

        // 2. Grouping by Species
        System.out.println("\n--- Distribution by Species (Collectors.groupingBy) ---");
        getPetCountByType().forEach((type, count) ->
                System.out.printf("  * %-8s : %d pet(s)%n", type.getDisplayName(), count));

        // 3. Partitioning by Adoptability
        System.out.println("\n--- Adoptability Partition (Collectors.partitioningBy) ---");
        Map<Boolean, List<Pet>> partitioned = partitionByAdoptability();
        System.out.printf("  * Available for Adoption : %d%n", partitioned.get(true).size());
        System.out.printf("  * Reserved / Quarantine  : %d%n", partitioned.get(false).size());

        // 4. Top 3 Highest Valued Pets
        System.out.println("\n--- Top 3 Highest Valued Animals ---");
        getTopPricedPets(3).forEach(p ->
                System.out.printf("  * %-12s (%-5s) - $%.2f | %s%n",
                        p.getName(), p.getType().getDisplayName(), p.getPrice(), p.getBreed()));

        // 5. Stream comparison
        compareStreamWithConventionalLoop();
        System.out.println("==============================================================\n");
    }
}
