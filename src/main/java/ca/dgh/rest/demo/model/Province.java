package ca.dgh.rest.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "LK_PROVINCE")
public class Province implements AbstractEntity {
    @Id
    @GeneratedValue
    private UUID id;
    @Size(max = 4)
    private String code;
    private String nameEn;
    private String nameFr;
}
