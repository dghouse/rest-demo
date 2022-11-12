package ca.dgh.rest.demo.service;

import ca.dgh.rest.demo.model.Province;
import ca.dgh.rest.demo.model.dto.ProvinceDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This class extends a {@link AbstractDTOAwareService} and provides access to all {@link Province} objects.
 */
@Service
public class ProvinceService extends AbstractDTOAwareService<Province, ProvinceDTO> {
    /**
     * A one argument constructor that requires a repository that manages {@link Province}.
     * @param repository a repository that manages {@link Province}
     * @param lookupModelMapper the {@link ModelMapper} that will convert between the {@link Province} and {@link ProvinceDTO} classes.
     */
    public ProvinceService (JpaRepository<Province, UUID> repository,
                           @Qualifier("modelMapper") ModelMapper lookupModelMapper) {
        super(repository, lookupModelMapper, Province.class, ProvinceDTO.class);
    }
}
