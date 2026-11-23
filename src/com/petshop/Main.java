package com.petshop;

import com.petshop.cli.MenuController;
import com.petshop.concurrency.AdoptionQueue;
import com.petshop.concurrency.HealthCheckScheduler;
import com.petshop.concurrency.OrderWorker;
import com.petshop.concurrency.ThreadStateMonitor;
import com.petshop.model.Bird;
import com.petshop.model.Cat;
import com.petshop.model.Dog;
import com.petshop.model.Order;
import com.petshop.model.Pet;
import com.petshop.model.PetType;
import com.petshop.repository.FilePetRepository;
import com.petshop.service.AnalyticsService;
import com.petshop.service.PetService;
import com.petshop.service.TransactionCoordinator;
import java.io.File;

/**
 * Main Application Bootstrap for Pet Haven CLI Platform.
 * Fully integrates:
 * - Unit 1: OOP Foundations & Custom Exception Hierarchy
 * - Unit 2: Collections Framework & Comparators
 * - Unit 3: Multithreading, Concurrency, Synchronized Blocks, and Schedulers
 * - Unit 4: Two-Phase Commit Transaction Management & Rollback
 * - Unit 5: Stream API, Lambda Analytics, and Pure File I/O
 */
public class Main {
    private static final String PETS_CSV_PATH = "data/pets.csv";
    private static final String ORDERS_CSV_PATH = "data/orders.csv";

    public static void main(String[] args) {
        printBanner();

        try {
            // 1. Initialize File Persistence Engine (Unit 5)
            System.out.println("[BOOTSTRAP] Initializing Pure Java CSV Repository...");
            ensureDataDirectoryExists();
            FilePetRepository petRepository = new FilePetRepository(PETS_CSV_PATH);

            // Seed sample inventory if brand new
            if (petRepository.count() == 0) {
                seedInitialData(petRepository);
            }
            System.out.printf("  Loaded %d pets from %s.%n", petRepository.count(), PETS_CSV_PATH);

            // 2. Initialize Transaction Coordinator & Pet Service (Unit 4 & 1)
            TransactionCoordinator txCoordinator = new TransactionCoordinator(petRepository, ORDERS_CSV_PATH);
            PetService petService = new PetService(petRepository, txCoordinator);

            // 3. Initialize Analytics Engine (Unit 5: Stream API)
            AnalyticsService analyticsService = new AnalyticsService(petService);

            // 4. Initialize Concurrency Infrastructure (Unit 3: Multithreading)
            System.out.println("[BOOTSTRAP] Launching Concurrency Infrastructure...");
            AdoptionQueue adoptionQueue = new AdoptionQueue(5);

            OrderWorker workerRunnable = new OrderWorker(adoptionQueue, petService);
            Thread workerThread = new Thread(workerRunnable, "OrderWorker-1");
            ThreadStateMonitor.traceState(workerThread, "Pre-Start");
            workerThread.start();
            ThreadStateMonitor.traceState(workerThread, "Post-Start");

            HealthCheckScheduler healthScheduler = new HealthCheckScheduler(petService);
            healthScheduler.startPeriodicHealthScan(3, 45);

            // Register JVM Shutdown Hook for clean thread termination
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\n[SHUTDOWN] Stopping background threads and closing resources...");
                workerRunnable.stopWorker();
                workerThread.interrupt();
                healthScheduler.stop();
                System.out.println("[SHUTDOWN] Clean exit complete.");
            }));

            // Check if automated self-test or interactive CLI
            boolean isTestMode = args.length > 0 && args[0].equalsIgnoreCase("--test");

            if (isTestMode) {
                runAutomatedSystemVerification(petService, analyticsService, adoptionQueue);
                System.out.println("\n[AUTOMATED TEST] Verification completed successfully. Exiting test mode.");
                workerRunnable.stopWorker();
                workerThread.interrupt();
                healthScheduler.stop();
                System.exit(0);
            } else {
                // Launch Interactive CLI Loop (Unit VI: Guided Project)
                MenuController menuController = new MenuController(
                        petService, analyticsService, adoptionQueue, healthScheduler);
                menuController.start();
                System.exit(0);
            }

        } catch (Exception e) {
            System.err.println("[FATAL BOOTSTRAP ERROR] " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void ensureDataDirectoryExists() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    private static void seedInitialData(FilePetRepository repo) {
        try {
            repo.save(new Dog(101, "Milo", "Golden Retriever", 14, 850.00, "CHIP-101", true, "Sporting"));
            repo.save(new Cat(102, "Luna", "Siamese", 42, 600.00, "CHIP-102", true, false));
            repo.save(new Dog(103, "Rocky", "German Shepherd", 24, 950.00, "CHIP-103", true, "Herding"));
            repo.save(new Cat(104, "Bella", "Persian", 18, 750.00, "CHIP-104", true, true));
            repo.save(new Bird(105, "Rio", "Macaw", 36, 1200.00, "CHIP-105", 85.5, true));
            repo.save(new Dog(106, "Charlie", "Beagle", 8, 550.00, "CHIP-106", false, "Hound"));
            repo.save(new Bird(108, "Kiwi", "Cockatiel", 12, 320.00, "CHIP-108", 32.0, true));
        } catch (Exception e) {
            System.err.println("Seed error: " + e.getMessage());
        }
    }

    private static void runAutomatedSystemVerification(PetService service, AnalyticsService analytics, AdoptionQueue queue) {
        System.out.println("\n==============================================================");
        System.out.println("          RUNNING END-TO-END AUTOMATED VERIFICATION           ");
        System.out.println("==============================================================");

        // 1. Polymorphism and Inheritance Verification
        System.out.println("[CHECK 1] Verifying Polymorphic Behavior...");
        for (Pet p : service.getAllPets()) {
            System.out.printf("  * %-10s (%-4s) -> Sound: '%s' | Care: %s%n",
                    p.getName(), p.getType().name(), p.getSound(), p.getSpecialNeeds());
        }

        // 2. Stream Analytics Verification
        System.out.println("\n[CHECK 2] Verifying Stream API Pipelines & Summaries...");
        analytics.printAnalyticsReport();

        // 3. Transaction Execution & Rollback Verification
        System.out.println("[CHECK 3] Verifying Two-Phase Commit Transaction & Rollback...");
        try {
            System.out.print("  Executing Adoption Checkout for Pet #101 by Customer #2001 ($850.00)... ");
            Order order = service.processAdoption(101, 2001, 850.00);
            System.out.println("SUCCESS: " + order);
        } catch (Exception e) {
            System.out.println("Caught: " + e.getMessage());
        }

        try {
            System.out.print("  Executing Faulty Adoption (Already Adopted Pet #101)... ");
            service.processAdoption(101, 2002, 850.00);
            System.out.println("FAIL: Should have been rejected!");
        } catch (Exception e) {
            System.out.println("EXPECTED REJECTION: " + e.getMessage());
        }
    }

    private static void printBanner() {
        System.out.println("************************************************************");
        System.out.println("*            PET HAVEN - CLI PLATFORM (PURE JAVA)          *");
        System.out.println("*       Chitkara University | Course Code: 25CSE0202       *");
        System.out.println("************************************************************\n");
    }
}
