package ca.dgh.rest.demo.service;

import ca.dgh.rest.demo.model.User;
import ca.dgh.rest.demo.model.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This class extends a {@link AbstractDTOAwareService} and provides access to all {@link User} objects.
 */
@Service
public class UserService extends AbstractDTOAwareService<User, UserDTO> {

    /**
     * A one argument constructor that requires a repository that manages {@link User}.
     *
     * @param repository  a repository that manages {@link User} objects.
     * @param modelMapper the {@link ModelMapper} that will convert between the {@link User} and {@link UserDTO} classes.
     */
    public UserService(JpaRepository<User, UUID> repository,
                       @Qualifier("modelMapper") ModelMapper modelMapper) {
        super(repository, modelMapper, User.class, UserDTO.class);
    }
}
