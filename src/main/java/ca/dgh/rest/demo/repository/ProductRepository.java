package ca.dgh.rest.demo.repository;

import ca.dgh.rest.demo.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Province, UUID> {
}
