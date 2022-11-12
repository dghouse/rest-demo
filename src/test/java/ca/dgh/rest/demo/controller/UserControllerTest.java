package ca.dgh.rest.demo.controller;

import ca.dgh.rest.demo.TestFactory;
import ca.dgh.rest.demo.model.User;
import ca.dgh.rest.demo.model.dto.UserDTO;
import ca.dgh.rest.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserControllerTest extends AbstractControllerTest<User, UserDTO> {

    @Mock
    UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        super.setup(TestFactory.getUserDTO(),
                new UserController(userService),
                userService,
                "/api/user/");
    }
}
