package com.petshop.concurrency;

import com.petshop.model.Pet;
import com.petshop.model.PetStatus;
import com.petshop.service.PetService;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Scheduled health and medical monitor service (Syllabus Unit 3: ScheduledExecutorService).
 * Periodically verifies animal status and medical quarantine compliance in the background.
 */
public class HealthCheckScheduler {
    private final PetService petService;
    private final ScheduledExecutorService scheduler;

    public HealthCheckScheduler(PetService petService) {
        this.petService = petService;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "PetHealthMonitor-Thread");
            t.setDaemon(true); // Daemon thread (Syllabus Unit 3: daemon threads)
            return t;
        });
    }

    public void startPeriodicHealthScan(long initialDelaySeconds, long periodSeconds) {
        scheduler.scheduleAtFixedRate(this::performScan, initialDelaySeconds, periodSeconds, TimeUnit.SECONDS);
    }

    public void performScan() {
        try {
            List<Pet> pets = petService.getAllPets();
            long quarantineCount = pets.stream().filter(p -> p.getStatus() == PetStatus.QUARANTINE).count();
            long unvaxCount = pets.stream().filter(p -> !p.isVaccinated()).count();

            System.out.printf("%n  [HEALTH SCAN @ %s] Inventory Check: %d pets | Quarantine: %d | Awaiting Vaccine: %d%n",
                    java.time.LocalTime.now().withNano(0), pets.size(), quarantineCount, unvaxCount);
        } catch (Exception e) {
            System.err.println("  [HEALTH SCAN ERROR] " + e.getMessage());
        }
    }

    public void stop() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(2, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
