package ca.dgh.rest.demo.controller;

import ca.dgh.rest.demo.exception.DataNotFoundException;
import ca.dgh.rest.demo.model.dto.AbstractDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Controller<T extends AbstractDTO> {
    /**
     * Get all objects available in the persistence layer.
     *
     * @return all objects available in the persistence layer.
     */
    List<T> getAll();

    /**
     * Get an object from the persistence layer that matches the given {@link UUID}.
     *
     * @param id id of the object to retrieve from the persistence layer.
     * @return an object from the persistence layer that matches the given {@link UUID}.
     */
    Optional<T> getById(UUID id);

    /**
     * Create an object in the persistence layer with the information in the given object.
     *
     * @param object object containing the information to be persisted.
     * @return the newly created object.
     */
    T create(T object);

    /**
     * Update an object in the persistence layer with the information in the given object.
     *
     * @param object object containing the information to be updated in the persistence layer.
     * @return the newly updated object.
     */
    T update(T object) throws DataNotFoundException;

    /**
     * Delete an object in the persistence layer identified by the given {@link UUID}
     *
     * @param id object containing the information to be updated in the persistence layer.
     */
    void delete(UUID id) throws DataNotFoundException;
}
