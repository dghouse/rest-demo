package ca.dgh.rest.demo.service;

import ca.dgh.rest.demo.exception.DataNotFoundException;
import ca.dgh.rest.demo.model.AbstractEntity;
import ca.dgh.rest.demo.model.dto.AbstractDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * This class tests services that extend the {@link AbstractDTOAwareService} abstract class. Tests are confined to
 * methods that are implemented in that abstract class. If child classes implement additional logic, that logic should
 * be tested elsewhere.
 *
 * @param <S> the entity expected from the {@link JpaRepository}.
 * @param <T> the data transfer object used to communicate with methods that call service implementations.
 */
abstract class AbstractDTOAwareServiceTest<S extends AbstractEntity, T extends AbstractDTO> {

    /**
     * The repository class that has been injected into the service. We will use {@link Mockito} to mock responses from
     * this repository.
     */
    JpaRepository<S, UUID> mockedRepository;

    /**
     * The service to be tested by this class.
     */
    AbstractDTOAwareService<S, T> service;

    S testEntity;

    /**
     * A test DTO that has been created using the {@link ModelMapper} and provided test entity.
     */
    T testDTO;

    /**
     * The Class type of the entity used in this service.
     */
    Class<S> entityType;

    /**
     * The Class type of the DTO produced by this service.
     */
    Class<T> dtoType;

    /**
     * Set up the local objects that will be used by the testing methods in this class. Please note that callers should
     * ensure any mocked objects should be done in the child class.
     *
     * @param abstractDTOAwareService The instance of the {@link AbstractDTOAwareService} service to be tested.
     * @param repository The mocked repository that will be used by the service.
     * @param modelMapper The {@link ModelMapper} that will provide mapping capabilities between {@link AbstractEntity} and
     *                    the {@link AbstractDTO} object.
     * @param testEntity The test entity to use for this service.
     * @param entityType The class type of the {@link AbstractEntity} object.
     * @param dtoType The class type of the {@link AbstractDTO} object.
     */
    void setup(AbstractDTOAwareService<S, T> abstractDTOAwareService, JpaRepository<S, UUID> repository,
               ModelMapper modelMapper, S testEntity, Class<S> entityType, Class<T> dtoType) {
        this.service = abstractDTOAwareService;
        this.mockedRepository = repository;
        this.testEntity = testEntity;
        this.testDTO = modelMapper.map(testEntity, dtoType);
        this.entityType = entityType;
        this.dtoType = dtoType;
    }

    /**
     * Test that a caller will get a list of {@link AbstractDTO} when calling the getAll method.
     */
    @Test
    void success_getAll() {
        Mockito.when(mockedRepository.findAll()).thenReturn(List.of(testEntity));
        List<T> dtoList = this.service.getAll();
        Assertions.assertEquals(1, dtoList.size());
    }

    /**
     * Test that a caller will get a {@link AbstractDTO} when calling the getById method.
     */
    @Test
    void success_getById() {
        Mockito.when(mockedRepository.findById(testEntity.getId())).thenReturn(Optional.of(testEntity));
        Optional<T> result = this.service.getById(testDTO.getId());
        Assertions.assertTrue(result.isPresent());
    }

    /**
     * Test that a caller will get an empty {@link Optional} object when asking for an object that doesn't exist.
     */
    @Test
    void success_getById_noResult() {
        final UUID randomUUID = UUID.randomUUID();
        Mockito.when(mockedRepository.findById(randomUUID)).thenReturn(Optional.empty());
        Optional<T> result = this.service.getById(randomUUID);
        Assertions.assertTrue(result.isEmpty());

    }

    /**
     * Test that a caller will receive a {@link AbstractDTO} with an id when attempting to create an entry.
     */
    @Test
    void success_create() {
        testDTO.setId(null);
        Mockito.when(mockedRepository.save(any(entityType))).thenReturn(testEntity);
        T newDTO = this.service.create(testDTO);
        verify(this.mockedRepository, times(1)).save(any(entityType));
        Assertions.assertNotNull(newDTO);
        Assertions.assertNotNull(newDTO.getId());
    }

    /**
     * Test that a caller will receive a {@link AbstractDTO} with an id when attempting to update an entry.
     *
     * @throws DataNotFoundException when no data has been found.
     */
    @Test
    void success_update() throws DataNotFoundException {
        Assertions.assertNotNull(testDTO.getId(), "The provided testDTO should have a UUID");
        Assertions.assertEquals(testDTO.getId(), testEntity.getId(),
                "The testDTO and testEntities should have the same id");
        Mockito.when(mockedRepository.save(any(entityType))).thenReturn(testEntity);
        Mockito.when(mockedRepository.findById(testEntity.getId())).thenReturn(Optional.ofNullable(testEntity));

        T updatedDTO = this.service.update(testDTO);
        verify(this.mockedRepository, times(1)).save(any(entityType));
        Assertions.assertEquals(testDTO.getId(), updatedDTO.getId());
    }

    /**
     *
     */
    //@Test
    void fail_update() {

    }

    /**
     * Test that the delete method was called when a caller attempts to delete an object in the persistence layer.
     * @throws DataNotFoundException when no data has been found.
     */
    @Test
    void success_delete() throws DataNotFoundException {
        Assertions.assertNotNull(testDTO.getId(), "The provided testDTO should have a UUID");
        Mockito.when(mockedRepository.findById(testEntity.getId())).thenReturn(Optional.of(testEntity));
        this.service.delete(testDTO.getId());
        verify(this.mockedRepository, times(1)).deleteById(any(UUID.class));
    }
}
