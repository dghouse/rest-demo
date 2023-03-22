package ca.dgh.rest.demo.repository;

import ca.dgh.rest.demo.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, UUID> {

    /**
     * Really this would be a lot nicer to do with a CriteraAPI query, but it will do for now.
     * @param nameEn the name of a province in English to match
     * @param nameFr the name of a province in French to match
     * @return a Province object that matches the English or French names.
     */
    Optional<Province> findOneProvinceByNameEnIgnoreCaseOrNameFrIgnoreCase(String nameEn, String nameFr);

}
