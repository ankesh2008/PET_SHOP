package com.petshop.repository;

import com.petshop.exception.PetShopException;
import java.util.List;
import java.util.Optional;

/**
 * Generic Repository Interface demonstrating Type Safety & Introductory Generics (Syllabus Unit 2 & 5).
 * @param <T> Entity type
 * @param <ID> Unique identifier type
 */
public interface Repository<T, ID> {
    /**
     * Persists or updates the entity in the storage.
     */
    T save(T entity) throws PetShopException;

    /**
     * Looks up entity by unique ID.
     */
    Optional<T> findById(ID id);

    /**
     * Returns all active entities.
     */
    List<T> findAll();

    /**
     * Deletes entity by ID.
     */
    boolean deleteById(ID id) throws PetShopException;

    /**
     * Checks if entity exists by ID.
     */
    boolean existsById(ID id);

    /**
     * Returns total count of entities.
     */
    long count();
}
