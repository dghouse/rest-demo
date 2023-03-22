package ca.dgh.rest.demo.service;

import ca.dgh.rest.demo.configuration.CachingConfiguration;
import ca.dgh.rest.demo.model.Province;
import ca.dgh.rest.demo.model.dto.ProvinceDTO;
import ca.dgh.rest.demo.repository.ProvinceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static javax.management.timer.Timer.ONE_MINUTE;

/**
 * This class extends a {@link AbstractDTOAwareService} and provides access to all {@link Province} objects.
 */
@Service
public class ProvinceService extends AbstractDTOAwareService<Province, ProvinceDTO> {

    /**
     * The repository object used to retrieve a {@link Province} from the persistence layer.
     */
    ProvinceRepository provinceRepository;

    /**
     * A one argument constructor that requires a repository that manages {@link Province}.
     *
     * @param repository        a repository that manages {@link Province}
     * @param lookupModelMapper the {@link ModelMapper} that will convert between the {@link Province} and {@link ProvinceDTO} classes.
     */
    public ProvinceService(ProvinceRepository repository,
                           @Qualifier("modelMapper") ModelMapper lookupModelMapper) {
        super(repository, lookupModelMapper, Province.class, ProvinceDTO.class);
        this.provinceRepository = repository;
    }

    /**
     * Get a province by a given name. To make life easier, we actually attempt to match both French and English names.
     * <p />
     * Note that this has its own caching logic and copies the inherited eviction logic.
     * @param name the name fo the province to retrieve.
     * @return the {@link Province} object that matches the given province name.
     */
    @Cacheable(cacheResolver = CachingConfiguration.CACHE_RESOLVER_NAME)
    public Optional<Province> getProvinceByName(String name) {
        return this.provinceRepository.findOneProvinceByNameEnIgnoreCaseOrNameFrIgnoreCase(name,name);
    }

    /**
     * Schedule the eviction of all entries from this cache.
     */
    @Scheduled(fixedRate = 30 * ONE_MINUTE)
    @CacheEvict(cacheResolver = CachingConfiguration.CACHE_RESOLVER_NAME, allEntries = true)
    @Override
    public void clearCache() {
        // All the work in this method is actually done by the annotations.
    }
}
