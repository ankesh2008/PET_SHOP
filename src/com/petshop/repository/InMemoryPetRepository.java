package com.petshop.repository;

import com.petshop.exception.DuplicatePetException;
import com.petshop.exception.PetNotFoundException;
import com.petshop.exception.PetShopException;
import com.petshop.model.Customer;
import com.petshop.model.Pet;
import com.petshop.model.comparator.PetPriceComparator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

/**
 * In-Memory Collections-based Pet Repository.
 * Combines:
 * - HashMap for O(1) ID lookups (Unit 2)
 * - HashSet to enforce unique Microchip IDs (Unit 2)
 * - TreeSet for sorted price views (Unit 2)
 * - PriorityQueue for customer loyalty waitlists (Unit 2)
 * - Iterator traversal (Unit 2)
 */
public class InMemoryPetRepository implements Repository<Pet, Integer> {
    private final Map<Integer, Pet> petMap = new HashMap<>();
    private final Set<String> registeredMicrochips = new HashSet<>();
    private final TreeSet<Pet> priceSortedSet = new TreeSet<>(new PetPriceComparator());
    private final PriorityQueue<Customer> adoptionWaitlist = new PriorityQueue<>();

    @Override
    public Pet save(Pet pet) throws PetShopException {
        if (pet == null) {
            throw new PetShopException("Cannot save null pet", "ERR_NULL_ENTITY");
        }

        // Check microchip uniqueness for new entries or changed microchips
        Pet existing = petMap.get(pet.getId());
        if (existing == null) {
            if (registeredMicrochips.contains(pet.getMicrochipId())) {
                throw new DuplicatePetException("microchip", pet.getMicrochipId());
            }
            registeredMicrochips.add(pet.getMicrochipId());
        } else if (!existing.getMicrochipId().equalsIgnoreCase(pet.getMicrochipId())) {
            registeredMicrochips.remove(existing.getMicrochipId());
            if (registeredMicrochips.contains(pet.getMicrochipId())) {
                throw new DuplicatePetException("microchip", pet.getMicrochipId());
            }
            registeredMicrochips.add(pet.getMicrochipId());
            priceSortedSet.remove(existing);
        } else {
            priceSortedSet.remove(existing);
        }

        petMap.put(pet.getId(), pet);
        priceSortedSet.add(pet);
        return pet;
    }

    @Override
    public Optional<Pet> findById(Integer id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(petMap.get(id));
    }

    @Override
    public List<Pet> findAll() {
        return new ArrayList<>(petMap.values());
    }

    @Override
    public boolean deleteById(Integer id) throws PetShopException {
        Pet removed = petMap.remove(id);
        if (removed == null) {
            throw new PetNotFoundException(id);
        }
        registeredMicrochips.remove(removed.getMicrochipId());
        priceSortedSet.remove(removed);
        return true;
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && petMap.containsKey(id);
    }

    @Override
    public long count() {
        return petMap.size();
    }

    public List<Pet> getPetsSortedByPrice() {
        return new ArrayList<>(priceSortedSet);
    }

    public void addToWaitlist(Customer customer) {
        adoptionWaitlist.offer(customer);
    }

    public Customer pollNextWaitlistCustomer() {
        return adoptionWaitlist.poll();
    }

    public List<Customer> getWaitlistSnapshot() {
        return new ArrayList<>(adoptionWaitlist);
    }

    public Iterator<Pet> iterator() {
        return new ArrayList<>(petMap.values()).iterator();
    }
}
