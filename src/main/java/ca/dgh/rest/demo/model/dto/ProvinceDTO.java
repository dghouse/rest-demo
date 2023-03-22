package ca.dgh.rest.demo.model.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProvinceDTO extends AbstractDTO implements LookupDTO {
    private UUID id;
    @Size(max = 4)
    private String code;
    private String nameEn;
    private String nameFr;
}
