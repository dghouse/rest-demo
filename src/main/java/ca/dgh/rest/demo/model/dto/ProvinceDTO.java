package ca.dgh.rest.demo.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
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
