package ca.dgh.rest.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
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
