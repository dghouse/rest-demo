package ca.dgh.rest.demo.service;

import ca.dgh.rest.demo.TestFactory;
import ca.dgh.rest.demo.mapper.ModelMapperConfiguration;
import ca.dgh.rest.demo.model.User;
import ca.dgh.rest.demo.model.dto.UserDTO;
import ca.dgh.rest.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

public class UserServiceTest extends AbstractDTOAwareServiceTest<User, UserDTO> {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ModelMapperConfiguration modelMapperConfiguration = new ModelMapperConfiguration();
        ModelMapper modelMapper = modelMapperConfiguration.createModelMapper();
        super.setup(new UserService(userRepository, modelMapper),
                userRepository,
                modelMapper,
                TestFactory.getUser(),
                User.class, UserDTO.class);
    }
}
