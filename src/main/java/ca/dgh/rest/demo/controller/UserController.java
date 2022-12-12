package ca.dgh.rest.demo.controller;

import ca.dgh.rest.demo.exception.DataNotFoundException;
import ca.dgh.rest.demo.model.dto.UserDTO;
import ca.dgh.rest.demo.service.DataFileService;
import ca.dgh.rest.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
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
     */
    private final DataFileService dataFileService;

    /**
     *
     * @param userService The service used to access {@link UserDTO} objects in the service layer.
     */
    public UserController(DataFileService dataFileService, UserService userService) {
        this.userService = userService;
        this.dataFileService = dataFileService;
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
    public UserDTO create(@RequestBody @Valid UserDTO object) {
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
    public UserDTO update(@RequestBody @Valid UserDTO object) throws DataNotFoundException {
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "503", content = @Content(mediaType = "application/json"))})
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "This method provides a means to upload a file that is to be associated with an interaction.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> upload(
            @RequestPart("file") @Parameter(description = "File to store") MultipartFile multipartFile
    ) throws IOException {
        dataFileService.processDataFile(multipartFile.getInputStream());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
