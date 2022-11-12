package ca.dgh.rest.demo.controller;

import ca.dgh.rest.demo.exception.DataNotFoundException;
import ca.dgh.rest.demo.model.dto.ProvinceDTO;
import ca.dgh.rest.demo.service.ProvinceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/province")
public class ProvinceController implements Controller<ProvinceDTO> {

    /**
     *
     */
    private ProvinceService provinceService;

    /**
     *
     * @param provinceService
     */
    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    /**
     * Get all objects available in the persistence layer.
     *
     * @return all objects available in the persistence layer.
     */
    @GetMapping("/")
    public List<ProvinceDTO> getAll() {
        return provinceService.getAll();
    }

    /**
     * Get an object from the persistence layer that matches the given {@link UUID}.
     *
     * @param id id of the object to retrieve from the persistence layer.
     * @return an object from the persistence layer that matches the given {@link UUID}.
     */
    @GetMapping("/{id}")
    public Optional<ProvinceDTO> getById(@PathVariable("id") UUID id) {
        return provinceService.getById(id);
    }

    /**
     * Create an object in the persistence layer with the information in the given object.
     *
     * @param object object containing the information to be persisted.
     * @return the newly created object.
     */
    @PostMapping("/")
    public ProvinceDTO create(ProvinceDTO object) {
        return provinceService.create(object);
    }

    /**
     * Update an object in the persistence layer with the information in the given object.
     *
     * @param object object containing the information to be updated in the persistence layer.
     * @return the newly updated object.
     */
    @PutMapping("/")
    public ProvinceDTO update(ProvinceDTO object) throws DataNotFoundException {
        return provinceService.update(object);
    }

    /**
     * Delete an object in the persistence layer identified by the given {@link UUID}
     *
     * @param id object containing the information to be updated in the persistence layer.
     */
    @DeleteMapping("/{id}")
    public void delete(UUID id) throws DataNotFoundException {
        provinceService.delete(id);
    }
}
