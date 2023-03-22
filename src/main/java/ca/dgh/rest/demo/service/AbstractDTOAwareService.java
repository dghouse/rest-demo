package ca.dgh.rest.demo.service;

import ca.dgh.rest.demo.configuration.CachingConfiguration;
import ca.dgh.rest.demo.exception.DataNotFoundException;
import ca.dgh.rest.demo.model.AbstractEntity;
import ca.dgh.rest.demo.model.dto.AbstractDTO;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static javax.management.timer.Timer.ONE_MINUTE;

public abstract class AbstractDTOAwareService<S extends AbstractEntity, T extends AbstractDTO> {
    /**
     * The repository that will be used in this class to interact with the persistence layer.
     */
    JpaRepository<S, UUID> repository;

    /**
     * The model mapper that will be used to convert between the S ({@link AbstractEntity}) and
     * T ({@link AbstractDTO}) objects.
     */
    ModelMapper modelMapper;

    /**
     * The DTO class type, used by the {@link ModelMapper}
     */
    Class<T> dtoClassType;

    /**
     * The entity class type, used by the {@link ModelMapper}
     */
    Class<S> entityClassType;

    /**
     * This one argument constructor accepts a repository that will be used to work with the persistence layer.
     *
     * @param repository      the object that will provide an interface to the persistence layer.
     * @param modelMapper     the model mapper that will be used to convert between the S ({@link AbstractEntity}) and
     *                        T ({@link AbstractDTO}) objects.
     * @param entityClassType the DTO class type, used by the {@link ModelMapper}
     * @param dtoClassType    the entity class type, used by the {@link ModelMapper}
     */
    protected AbstractDTOAwareService(JpaRepository<S, UUID> repository, ModelMapper modelMapper,
                                      Class<S> entityClassType,
                                      Class<T> dtoClassType) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.dtoClassType = dtoClassType;
        this.entityClassType = entityClassType;
    }

    /**
     * Get all objects that are available in the persistence layer for the generic type.
     *
     * @return all objects of the generic type in the persistence layer.
     */
    @Cacheable(cacheResolver = CachingConfiguration.CACHE_RESOLVER_NAME)
    public List<T> getAll() {
        return this.repository.findAll().stream()
                .map(o -> modelMapper.map(o, dtoClassType)).toList();
    }

    /**
     * Get an object from the persistence layer based on the given id.
     *
     * @param id the ID of the target object.
     * @return an {@link Optional} of our generic type.
     */
    @Cacheable(cacheResolver = CachingConfiguration.CACHE_RESOLVER_NAME)
    public Optional<T> getById(UUID id) {
        Optional<S> result = this.repository.findById(id);
        if (result.isPresent()) {
            return Optional.of(this.modelMapper.map(result, dtoClassType));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Create an object in the persistence layer.
     *
     * @param newObject the new object to persist.
     * @return the newly persisted object.
     */
    @CacheEvict(cacheResolver = CachingConfiguration.CACHE_RESOLVER_NAME, allEntries = true)
    public T create(T newObject) {
        S mappedEntity = repository.save(modelMapper.map(newObject, entityClassType));
        return modelMapper.map(mappedEntity, dtoClassType);
    }

    /**
     * Update a given object that exists in the database.
     *
     * @param updatedObject Object containing new values to be persisted.
     * @return the object that has been updated.
     */
    @CacheEvict(cacheResolver = CachingConfiguration.CACHE_RESOLVER_NAME, allEntries = true)
    public T update(T updatedObject) throws DataNotFoundException {
        objectExistsInRepository(updatedObject.getId(), repository);
        S mappedEntity = repository.save(modelMapper.map(updatedObject, entityClassType));
        return modelMapper.map(mappedEntity, dtoClassType);
    }

    /**
     * Delete the object from the persistence layer with the given id.
     *
     * @param id the id of the object to delete.
     * @throws {@link DataNotFoundException} when an object cannot be found.
     */
    @CacheEvict(cacheResolver = CachingConfiguration.CACHE_RESOLVER_NAME, allEntries = true)
    public void delete(UUID id) throws DataNotFoundException {
        objectExistsInRepository(id, repository);
        repository.deleteById(id);
    }

    /**
     * Schedule the eviction of all entries from this cache.
     */
    @Scheduled(fixedRate = 30 * ONE_MINUTE)
    @CacheEvict(cacheResolver = CachingConfiguration.CACHE_RESOLVER_NAME, allEntries = true)
    public void clearCache() {
    }

    private void objectExistsInRepository(UUID id, JpaRepository<S, UUID> repository) throws DataNotFoundException {
        if (repository.findById(id).isEmpty()) {
            throw new DataNotFoundException();
        }
    }
}