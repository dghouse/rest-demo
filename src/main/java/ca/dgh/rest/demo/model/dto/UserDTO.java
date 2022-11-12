package ca.dgh.rest.demo.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserDTO extends AbstractDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private UUID provinceId;
}
