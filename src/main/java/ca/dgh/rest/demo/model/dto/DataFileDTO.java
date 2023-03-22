package ca.dgh.rest.demo.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DataFileDTO {

    @NotEmpty
    String firstName;

    @NotEmpty
    String lastName;

    @NotEmpty
    String province;

}
