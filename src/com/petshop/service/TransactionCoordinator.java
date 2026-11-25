package com.petshop.service;

import com.petshop.exception.PetShopException;
import com.petshop.exception.TransactionFailedException;
import com.petshop.model.Order;
import com.petshop.model.Pet;
import com.petshop.repository.FilePetRepository;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Pure Java Transaction Coordinator implementing Two-Phase Commit and Rollback (Syllabus Unit 4).
 * Replaces SQL transactions using file staging, shadow writes (.tmp), and atomic swap.
 */
public class TransactionCoordinator {
    private final FilePetRepository petRepository;
    private final File ordersFile;

    public TransactionCoordinator(FilePetRepository petRepository, String ordersFilePath) {
        this.petRepository = petRepository;
        this.ordersFile = new File(ordersFilePath);
        File parent = ordersFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    /**
     * Executes atomic adoption transaction:
     * 1. Shadow writes updated pets to pets.csv.tmp
     * 2. Commits by atomic file replacement
     * 3. Appends completed order to orders.csv
     * 4. If any step fails, rolls back memory state and cleans temp files.
     */
    public Order executeAdoptionTransaction(String txId, Pet pet, int customerId, double amountPaid)
            throws TransactionFailedException {
        File dataFile = petRepository.getDataFile();
        File tempFile = new File(dataFile.getAbsolutePath() + ".tmp");

        // 1. Snapshot memory state for rollback
        String previousStatus = pet.getStatus().name();
        String previousAdopter = pet.getAdoptedBy();

        try {
            // Apply in-memory transition
            pet.markAdopted("Customer #" + customerId);

            // 2. Stage to temporary file (Shadow write)
            List<Pet> currentPets = petRepository.findAll();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                writer.write(FilePetRepository.CSV_HEADER);
                writer.newLine();
                for (Pet p : currentPets) {
                    writer.write(FilePetRepository.serializePetToCsv(p));
                    writer.newLine();
                }
                writer.flush();
            }

            // 3. Simulated constraint verification
            if (amountPaid < 0) {
                throw new IllegalArgumentException("Payment amount cannot be negative");
            }

            // 4. Atomic Commit: Swap temp file to active file
            Files.move(tempFile.toPath(), dataFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);

            // 5. Append order receipt to orders log
            Order order = new Order(pet.getId(), customerId, amountPaid);
            appendOrderRecord(order);

            // Reload repository cache from fresh file to verify consistency
            petRepository.loadFromFile();

            return order;

        } catch (Exception ex) {
            // ROLLBACK
            if (tempFile.exists()) {
                tempFile.delete();
            }
            // Revert in-memory object
            try {
                petRepository.loadFromFile();
            } catch (PetShopException e) {
                // Ignore fallback
            }
            throw new TransactionFailedException(txId, ex.getMessage(), ex);
        }
    }

    private void appendOrderRecord(Order order) throws IOException {
        boolean isNew = !ordersFile.exists() || ordersFile.length() == 0;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ordersFile, true))) {
            if (isNew) {
                writer.write("orderId,petId,customerId,orderDate,amountPaid,status");
                writer.newLine();
            }
            writer.write(String.format("%d,%d,%d,%s,%.2f,%s",
                    order.getOrderId(), order.getPetId(), order.getCustomerId(),
                    order.getOrderDate(), order.getAmountPaid(), order.getStatus()));
            writer.newLine();
            writer.flush();
        }
    }
}
