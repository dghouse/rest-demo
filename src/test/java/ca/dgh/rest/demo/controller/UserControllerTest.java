package ca.dgh.rest.demo.controller;

import ca.dgh.rest.demo.TestFactory;
import ca.dgh.rest.demo.model.User;
import ca.dgh.rest.demo.model.dto.UserDTO;
import ca.dgh.rest.demo.service.DataFileService;
import ca.dgh.rest.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserControllerTest extends AbstractControllerTest<User, UserDTO> {

    @Mock
    UserService userService;

    @Mock
    DataFileService dataFileService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        super.setup(TestFactory.getUserDTO(),
                new UserController(dataFileService, userService),
                userService,
                "/api/user/");
    }
}
