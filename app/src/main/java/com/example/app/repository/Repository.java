package com.example.app.repository;

import java.util.List;

/**
 * This interface abstracts the default CRUD methods to be implemented by Concrete classes.
 * Implementing classes should pass as parameter the table Entity Type they abstract.
 *
 * @param <T> The Entity Type to be mapped by the repository.
 */
public interface Repository<T> {

    /**
     * Creates a new database record of {@link T} type.
     * @param record the record instance to be added.
     * @return long the database primary key id for the new recorded added.
     */
    long insert(T record);

    /**
     * Finds an existing record of {@link T} type with given Id if exists.
     * @param id int value that represents the record's id.
     * @return the {@link T} instance that represents the record if found.
     */
    T findById(int id);

    /**
     * Returns a list of all available {@link T} records.
     * @return {@link List<T>} instance containing all records found.
     */
    List<T> findAll();

    /**
     * Updates an existing record of {@link T} type.
     * @param record The updated record to be saved.
     * @return boolean True if record was updated false otherwise.
     */
    boolean update(T record);

    /**
     * Deletes an existing record of {@link T} type.
     * @param record The record to be deleted.
     * @return boolean True if record was deleted false otherwise.
     */
    boolean delete(T record);
}
