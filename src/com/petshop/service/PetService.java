package com.petshop.service;

import com.petshop.exception.InsufficientStockException;
import com.petshop.exception.InvalidPetDataException;
import com.petshop.exception.PetNotFoundException;
import com.petshop.exception.PetShopException;
import com.petshop.model.Customer;
import com.petshop.model.Order;
import com.petshop.model.Pet;
import com.petshop.repository.FilePetRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Layer coordinating domain logic, thread-safe adoption, and transactional operations (Unit 1, 3, 4).
 */
public class PetService {
    private final FilePetRepository petRepository;
    private final TransactionCoordinator transactionCoordinator;
    private final Map<Integer, Customer> customerRegistry = new HashMap<>();

    // Lock object for inventory critical sections (Syllabus Unit 3: Intrinsic Locks)
    private final Object inventoryLock = new Object();

    public PetService(FilePetRepository petRepository, TransactionCoordinator transactionCoordinator) {
        this.petRepository = petRepository;
        this.transactionCoordinator = transactionCoordinator;
        seedDefaultCustomers();
    }

    private void seedDefaultCustomers() {
        try {
            Customer c1 = new Customer(2001, "Alice Walker", "9876543210", "alice@example.com", 2);
            Customer c2 = new Customer(2002, "Bob Martin", "9812345678", "bob@example.com", 0);
            customerRegistry.put(c1.getCustomerId(), c1);
            customerRegistry.put(c2.getCustomerId(), c2);
        } catch (InvalidPetDataException e) {
            // Seed defaults are valid
        }
    }

    public Pet registerPet(Pet pet) throws PetShopException {
        synchronized (inventoryLock) {
            return petRepository.save(pet);
        }
    }

    public Optional<Pet> getPetById(int id) {
        synchronized (inventoryLock) {
            return petRepository.findById(id);
        }
    }

    public List<Pet> getAllPets() {
        synchronized (inventoryLock) {
            return petRepository.findAll();
        }
    }

    public Customer registerCustomer(String name, String phone, String email) throws InvalidPetDataException {
        Customer customer = new Customer(name, phone, email);
        customerRegistry.put(customer.getCustomerId(), customer);
        return customer;
    }

    public Optional<Customer> getCustomerById(int id) {
        return Optional.ofNullable(customerRegistry.get(id));
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customerRegistry.values());
    }

    /**
     * Adopts a pet thread-safely inside a critical section protected by inventoryLock.
     */
    public Order processAdoption(int petId, int customerId, double payment) throws PetShopException {
        synchronized (inventoryLock) {
            Pet pet = petRepository.findById(petId)
                    .orElseThrow(() -> new PetNotFoundException(petId));

            Customer customer = customerRegistry.get(customerId);
            if (customer == null) {
                throw new PetShopException("Customer #" + customerId + " is not registered.", "ERR_CUST_NOT_FOUND");
            }

            if (!pet.isAdoptable()) {
                throw new InsufficientStockException(petId, pet.getStatus().name());
            }

            double requiredFee = pet.calculateAdoptionFee();
            if (payment < requiredFee) {
                throw new InvalidPetDataException("payment",
                        String.format("Insufficient payment: $%.2f provided, $%.2f required.", payment, requiredFee));
            }

            String txId = "TX-" + System.currentTimeMillis();
            Order order = transactionCoordinator.executeAdoptionTransaction(txId, pet, customerId, payment);

            customer.incrementAdoptionCount();
            return order;
        }
    }
}
