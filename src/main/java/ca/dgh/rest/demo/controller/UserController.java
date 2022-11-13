package ca.dgh.rest.demo.controller;

import ca.dgh.rest.demo.exception.DataNotFoundException;
import ca.dgh.rest.demo.model.dto.UserDTO;
import ca.dgh.rest.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/user")
public class UserController implements Controller<UserDTO>{

    /**
     *
     */
    private final UserService userService;

    /**
     *
     * @param userService The service used to access {@link UserDTO} objects in the service layer.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }
    /**
     * Get all objects available in the persistence layer.
     *
     * @return all objects available in the persistence layer.
     */
    @GetMapping("/")
    @Override
    public List<UserDTO> getAll() {
        return userService.getAll();
    }

    /**
     * Get an object from the persistence layer that matches the given {@link UUID}.
     *
     * @param id id of the object to retrieve from the persistence layer.
     * @return an object from the persistence layer that matches the given {@link UUID}.
     */
    @GetMapping("/{id}")
    @Override
    public Optional<UserDTO> getById(@PathVariable("id") UUID id) {
        return userService.getById(id);
    }

    /**
     * Create an object in the persistence layer with the information in the given object.
     *
     * @param object object containing the information to be persisted.
     * @return the newly created object.
     */
    @PostMapping("/")
    @Override
    public UserDTO create(@Valid  UserDTO object) {
        return userService.create(object);
    }

    /**
     * Update an object in the persistence layer with the information in the given object.
     *
     * @param object object containing the information to be updated in the persistence layer.
     * @return the newly updated object.
     * @throws DataNotFoundException thrown when the give object is not found.
     */
    @PutMapping("/")
    @Override
    public UserDTO update(@Valid UserDTO object) throws DataNotFoundException {
        return userService.update(object);
    }

    /**
     * Delete an object in the persistence layer identified by the given {@link UUID}
     *
     * @param id object containing the information to be updated in the persistence layer.
     * @throws DataNotFoundException thrown when the give object is not found.
     */
    @DeleteMapping("/{id}")
    @Override
    public void delete(@PathVariable("id") UUID id) throws DataNotFoundException {
        userService.delete(id);
    }
}
