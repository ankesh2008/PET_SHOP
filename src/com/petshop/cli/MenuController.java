package com.petshop.cli;

import com.petshop.concurrency.AdoptionQueue;
import com.petshop.concurrency.AdoptionRequest;
import com.petshop.concurrency.HealthCheckScheduler;
import com.petshop.exception.InvalidPetDataException;
import com.petshop.exception.PetShopException;
import com.petshop.model.Bird;
import com.petshop.model.Cat;
import com.petshop.model.Customer;
import com.petshop.model.Dog;
import com.petshop.model.Order;
import com.petshop.model.Pet;
import com.petshop.model.PetType;
import com.petshop.model.comparator.PetPriceComparator;
import com.petshop.service.AnalyticsService;
import com.petshop.service.PetService;
import com.petshop.util.InputValidator;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Interactive Console Presentation Controller (Syllabus Unit 1 & Guided Project).
 * Provides crash-proof menu routing, input validation, and user workflows.
 */
public class MenuController {
    private final PetService petService;
    private final AnalyticsService analyticsService;
    private final AdoptionQueue adoptionQueue;
    private final HealthCheckScheduler healthScheduler;
    private final Scanner scanner;

    public MenuController(PetService petService, AnalyticsService analyticsService,
                          AdoptionQueue adoptionQueue, HealthCheckScheduler healthScheduler) {
        this.petService = petService;
        this.analyticsService = analyticsService;
        this.adoptionQueue = adoptionQueue;
        this.healthScheduler = healthScheduler;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean exit = false;
        while (!exit) {
            printMainMenu();
            int choice = InputValidator.readInt(scanner, "Enter selection (1-9): ", 1, 9);
            System.out.println();

            switch (choice) {
                case 1 -> handleViewCatalog();
                case 2 -> handleRegisterPet();
                case 3 -> handleRegisterCustomer();
                case 4 -> handleAdoptPet();
                case 5 -> handleAsynchronousAdoption();
                case 6 -> analyticsService.printAnalyticsReport();
                case 7 -> handleQueueAndHealthStatus();
                case 8 -> handleSearchPet();
                case 9 -> {
                    System.out.println("Thank you for using Pet Haven CLI Platform. Goodbye!");
                    exit = true;
                }
            }
        }
    }

    private void printMainMenu() {
        System.out.println("==============================================================");
        System.out.println("             PET HAVEN - CLI PET SHOP PLATFORM                ");
        System.out.println("       Course Code: 25CSE0202 | Pure Java (No Framework)      ");
        System.out.println("==============================================================");
        System.out.println("  [1] View Pet Catalog (All / Sorted / Filtered)");
        System.out.println("  [2] Register New Pet (Dog, Cat, Bird)");
        System.out.println("  [3] Register New Customer");
        System.out.println("  [4] Synchronous Adoption Checkout (ACID Transaction)");
        System.out.println("  [5] Asynchronous Adoption Queue (Producer-Consumer Concurrency)");
        System.out.println("  [6] Stream API Analytics & Financial Valuation");
        System.out.println("  [7] Health Scan & Background Worker Status");
        System.out.println("  [8] Search Pet by ID or Microchip");
        System.out.println("  [9] Exit Application");
        System.out.println("==============================================================");
    }

    private void handleViewCatalog() {
        System.out.println("--- Catalog Sort & View Options ---");
        System.out.println("1. Natural Order (by ID)");
        System.out.println("2. Sorted by Price (Lowest to Highest)");
        System.out.println("3. Sorted by Price (Highest to Lowest)");
        System.out.println("4. Sorted by Age");
        int opt = InputValidator.readInt(scanner, "Choose option (1-4): ", 1, 4);

        List<Pet> pets = petService.getAllPets();
        if (pets.isEmpty()) {
            System.out.println("  [i] No pets currently in inventory.");
            return;
        }

        switch (opt) {
            case 2 -> pets.sort(new PetPriceComparator(true));
            case 3 -> pets.sort(new PetPriceComparator(false));
            case 4 -> pets.sort(Comparator.comparingInt(Pet::getAgeMonths));
            default -> pets.sort(Pet::compareTo);
        }

        System.out.println("\n-------------------------------------------------------------------------------------------------------------------");
        pets.forEach(System.out::println);
        System.out.println("-------------------------------------------------------------------------------------------------------------------\n");
    }

    private void handleRegisterPet() {
        System.out.println("--- Register New Pet ---");
        System.out.println("Select Type: 1. DOG | 2. CAT | 3. BIRD");
        int typeChoice = InputValidator.readInt(scanner, "Choice (1-3): ", 1, 3);

        String name = InputValidator.readNonEmptyString(scanner, "Enter Name: ");
        String breed = InputValidator.readNonEmptyString(scanner, "Enter Breed: ");
        int age = InputValidator.readInt(scanner, "Enter Age (in months): ", 0, 360);
        double price = InputValidator.readDouble(scanner, "Enter Price ($): ", 0.0, 50000.0);
        String chip = InputValidator.readNonEmptyString(scanner, "Enter Microchip Tag (e.g. CHIP-999): ");

        try {
            Pet newPet;
            if (typeChoice == 1) {
                boolean isTrained = InputValidator.readBoolean(scanner, "Is the dog obedience trained?");
                String group = InputValidator.readNonEmptyString(scanner, "Enter Breed Group (e.g. Herding, Toy): ");
                newPet = new Dog(name, breed, age, price, chip, isTrained, group);
            } else if (typeChoice == 2) {
                boolean isIndoor = InputValidator.readBoolean(scanner, "Is the cat strictly indoor?");
                boolean isDeclawed = InputValidator.readBoolean(scanner, "Is the cat declawed?");
                newPet = new Cat(name, breed, age, price, chip, isIndoor, isDeclawed);
            } else {
                double wingspan = InputValidator.readDouble(scanner, "Enter Wingspan (cm): ", 5.0, 300.0);
                boolean canTalk = InputValidator.readBoolean(scanner, "Can the bird talk/mimic?");
                newPet = new Bird(name, breed, age, price, chip, wingspan, canTalk);
            }

            petService.registerPet(newPet);
            System.out.printf("  [SUCCESS] Successfully registered %s with ID #%d!%n%n", newPet.getName(), newPet.getId());

        } catch (PetShopException e) {
            System.err.println("  [ERROR] Registration failed: " + e.getMessage());
        }
    }

    private void handleRegisterCustomer() {
        System.out.println("--- Register New Customer ---");
        String name = InputValidator.readNonEmptyString(scanner, "Full Name: ");
        String phone = InputValidator.readNonEmptyString(scanner, "10-digit Phone: ");
        String email = InputValidator.readNonEmptyString(scanner, "Email Address: ");

        try {
            Customer c = petService.registerCustomer(name, phone, email);
            System.out.printf("  [SUCCESS] Registered Customer #%d: %s%n%n", c.getCustomerId(), c.getFullName());
        } catch (InvalidPetDataException e) {
            System.err.println("  [ERROR] Customer registration failed: " + e.getMessage());
        }
    }

    private void handleAdoptPet() {
        System.out.println("--- Synchronous Adoption Checkout (ACID Transaction) ---");
        int petId = InputValidator.readInt(scanner, "Enter Pet ID to adopt: ", 1, 99999);
        int custId = InputValidator.readInt(scanner, "Enter Customer ID: ", 1, 99999);
        double payment = InputValidator.readDouble(scanner, "Enter Payment Amount ($): ", 0.0, 100000.0);

        try {
            Order order = petService.processAdoption(petId, custId, payment);
            System.out.println("\n  ==================== ADOPTION RECEIPT ====================");
            System.out.println("  " + order);
            System.out.println("  Transaction committed atomically to disk.");
            System.out.println("  ==========================================================\n");
        } catch (PetShopException e) {
            System.err.println("  [TRANSACTION ABORTED] " + e.getMessage());
        }
    }

    private void handleAsynchronousAdoption() {
        System.out.println("--- Submit Adoption to Concurrent Producer-Consumer Queue ---");
        int petId = InputValidator.readInt(scanner, "Enter Pet ID: ", 1, 99999);
        int custId = InputValidator.readInt(scanner, "Enter Customer ID: ", 1, 99999);
        double payment = InputValidator.readDouble(scanner, "Enter Payment ($): ", 0.0, 100000.0);

        AdoptionRequest request = new AdoptionRequest(petId, custId, payment);
        try {
            adoptionQueue.enqueue(request);
            System.out.println("  [ENQUEUED] Request dispatched to background OrderWorker queue.\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("  [ERROR] Queue submission interrupted.");
        }
    }

    private void handleQueueAndHealthStatus() {
        System.out.println("--- Concurrency & Health Status ---");
        System.out.printf("Adoption Queue Buffer : %d / %d items%n",
                adoptionQueue.size(), adoptionQueue.getCapacity());
        healthScheduler.performScan();
        System.out.println();
    }

    private void handleSearchPet() {
        int petId = InputValidator.readInt(scanner, "Enter Pet ID: ", 1, 99999);
        Optional<Pet> match = petService.getPetById(petId);
        if (match.isPresent()) {
            Pet p = match.get();
            System.out.println("\n  Match Found: " + p);
            System.out.println("  Sound: " + p.getSound());
            System.out.println("  Care: " + p.getSpecialNeeds() + "\n");
        } else {
            System.out.printf("  [!] Pet with ID #%d not found in inventory.%n%n", petId);
        }
    }
}
